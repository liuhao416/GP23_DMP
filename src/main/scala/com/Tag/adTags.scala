package com.Tag

import java.util

import com.typesafe.config.{Config, ConfigFactory}
import com.util.{AmapUtil, UserId}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{Admin, Connection, ConnectionFactory}
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, Row, SparkSession}


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


//    //调用HBASEapi文件
//    val config: Config = ConfigFactory.load()
//    //获取表名
//    val tableName: String = config.getString("tableName")
//    //创建Hadoop
//    val hadoopConfiguration: Configuration = sparkSession.sparkContext.hadoopConfiguration
//    //配置HBASE连接
//    hadoopConfiguration.set("hbase.zookeeper.quorum",config.getString("HBASE.Host"))
//
//    //获取connection连接
//    val hbConn: Connection = ConnectionFactory.createConnection(hadoopConfiguration)
//    val hbadmin: Admin = hbConn.getAdmin
//
//    if(!hbadmin.tableExists(TableName.valueOf(tableName))){
//      println("当前表可用")
//      // 创建表对象
//      val tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName))
//      // 创建列簇
//      val hColumnDescriptor = new HColumnDescriptor("tags")
//      // 将创建好的列簇加入表中
//      tableDescriptor.addFamily(hColumnDescriptor)
//      hbadmin.createTable(tableDescriptor)
//      hbadmin.close()
//      hbConn.close()
//    }
//      val  conf =new  JobConf(hadoopConfiguration)
//      //指定输出类型
//    conf.setOutputFormat(classOf[TableOutputFormat])
//      //指定输出哪张表
//    conf.set(TableOutputFormat.OUTPUT_TABLE,tableName)


    //获取数据
    import sparkSession.implicits._
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


    //获取用户信息     设置一个自定义方法    getUserId
    //调用方法获取用户信息
    rdd.map(row=>{
      //获取用户的唯一ID
     val userid: String = UserId.getUserId(row)
      //广告标签
     val ad: List[(String, Int)] = TagsAd.makeTags(row)
      //商圈
      val BusinessList: List[(String, Int)] = BusinessTag.makeTags(row)
     // 媒体的标签
    val tagsAPP: List[(String, Int)] = TagsAPP.makeTags(row,broadcast)
      //设备标签
    val tagDriver: List[(String, Int)] = tagClient.makeTags(row)
      //地区标签
      val tagspc: List[(String, Int)] = tagPC.makeTags(row)
   //关键字标签
      val keyword: List[(String, Int)] = tagWord.makeTags(row,stopword)

      (userid,ad++BusinessList++tagsAPP++tagDriver++tagspc++keyword)
    }).reduceByKey((list1,list2)=>{
      (list1:::list2).groupBy(_._1)
        .mapValues(_.foldLeft[Int](0)(_+_._2))
        .toList
    }).foreach(println)
  }
}
