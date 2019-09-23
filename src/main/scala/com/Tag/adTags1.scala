package com.Tag

import com.typesafe.config.{Config, ConfigFactory}
import com.util.UserId
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.{Admin, Connection, ConnectionFactory, Put}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.mapred.JobConf
import org.apache.spark
import org.apache.spark.graphx.{Edge, Graph, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}


/**
  * Description：xxxx<br/>
  * Copyright (c) ， 2018， Jansonxu <br/>
  * This program is protected by copyright laws. <br/>
  * Date： 2019年09月19日
  *
  * @author 刘皓
  * @version : 1.0
  */
/**
  * 上下文标签主类
  */
object adTags {

  def main(args: Array[String]): Unit = {


    //创建sparksession对象
    val  sparkSession=SparkSession.builder()
      .config("spark.serializer","org.apache.spark.serializer.KryoSerializer")
      .config("org.apache.spark.sql.compression","snappy")
      .appName("adTags")
      .master("local[*]")
      .getOrCreate()


    //调用HBASEapi文件
    val config: Config = ConfigFactory.load()
    //获取表名
    val tableName: String = config.getString("tableName")
    //创建Hadoop
    val hadoopConfiguration: Configuration = sparkSession.sparkContext.hadoopConfiguration
    //配置HBASE连接
    hadoopConfiguration.set("hbase.zookeeper.quorum",config.getString("HBASE.Host"))

    //获取connection连接
    val hbConn: Connection = ConnectionFactory.createConnection(hadoopConfiguration)
    val hbadmin: Admin = hbConn.getAdmin

    if(!hbadmin.tableExists(TableName.valueOf(tableName))){
      println("当前表可用")
      // 创建表对象
      val tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName))
      // 创建列簇
      val hColumnDescriptor = new HColumnDescriptor("tags")
      // 将创建好的列簇加入表中
      tableDescriptor.addFamily(hColumnDescriptor)
      hbadmin.createTable(tableDescriptor)
      hbadmin.close()
      hbConn.close()
    }
      val  conf =new  JobConf(hadoopConfiguration)
      //指定输出类型
    conf.setOutputFormat(classOf[TableOutputFormat])
      //指定输出哪张表
    conf.set(TableOutputFormat.OUTPUT_TABLE,tableName)


    //获取数据
    val rdd: RDD[Row] = sparkSession.read.parquet("F:\\data\\project.parquet").rdd
    //获取字典文件
     val  doscRDD= sparkSession.sparkContext.textFile("F:\\data\\project\\app_dict.txt")
       .map(_.split("\\s"))
       .filter(_.length>5)
       .map(arr=>(arr(4),arr(1)))
       .collectAsMap()
    //广播字典
    val broadcast = sparkSession.sparkContext.broadcast(doscRDD)

    //获取停留文件
    val stopwordRDD  = sparkSession.sparkContext.textFile("F:\\data\\project\\stopwords.txt")
      .map((_,0))
      .collectAsMap()
    //广播字典
    val stopword = sparkSession.sparkContext.broadcast(stopwordRDD)

    val alluserId=rdd.map(row=>{
      //获取所有的ID
      val userId: List[String] = UserId.getUserId1(row)
      (userId,row)
    })
      //构建点集合
    val verties: RDD[(Long, List[(String, Int)])] = alluserId.flatMap(row => {
      //获取所有数据
      val rows = row._2

      //广告标签
      val ad: List[(String, Int)] = TagsAd.makeTags(rows)
      //商圈
      val BusinessList: List[(String, Int)] = BusinessTag.makeTags(rows)
      // 媒体的标签
      val tagsAPP: List[(String, Int)] = TagsAPP.makeTags(rows, broadcast)
      //设备标签
      val tagDriver: List[(String, Int)] = tagClient.makeTags(rows)
      //地区标签
      val tagspc: List[(String, Int)] = tagPC.makeTags(rows)
      //关键字标签
      val keyword: List[(String, Int)] = tagWord.makeTags(rows, stopword)
      //获取所有的标签

      val tagList: List[(String, Int)] = ad ++ BusinessList ++ tagsAPP ++ tagDriver ++ tagspc ++ keyword

      val VD: List[(String, Int)] = row._1.map((_, 0)) ++ tagList


      //保证其中一个ID携带着本人数据
      row._1.map(uId => {
        if (row._1.head.equals(uId)) {
          (uId.hashCode.toLong, VD)
        } else {
          (uId.hashCode.toLong, List.empty)
        }
      })
    })
   // verties.foreach(println)
    val edges: RDD[Edge[Int]] = alluserId.flatMap(row => {
      row._1.map(uId => Edge(row._1.head.hashCode.toLong, uId.hashCode.toLong, 0))

    })
    //构建图
    val graph = Graph(verties,edges)

    val vertices: VertexRDD[VertexId] = graph.connectedComponents().vertices

    vertices.join(verties).map({
      case  (uid,(cnId,tagsAndUserId))=>{
        (cnId,tagsAndUserId)
      }
    })
      .reduceByKey(
        (list1,list2)=>{
          (list1++list2)
            .groupBy(_._1)
            .mapValues(_.map(_._2).sum)
            .toList
        }
      )
      .map{
        case (userId,userTags) =>{
          // 设置rowkey和列、列名
          val put = new Put(Bytes.toBytes(userId))
          put.addImmutable(Bytes.toBytes("tags"),Bytes.toBytes("day"),Bytes.toBytes(userTags.mkString(",")))
          (new ImmutableBytesWritable(),put)
        }
      }.saveAsHadoopDataset(conf)

    sparkSession.stop()
  }
}
