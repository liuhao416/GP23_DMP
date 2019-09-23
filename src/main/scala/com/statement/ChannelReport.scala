package com.statement

import com.util.ReqUtils1
import org.apache.commons.lang3.StringUtils
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
  * Description：xxxx<br/>
  * Copyright (c) ， 2018， Jansonxu <br/>
  * This program is protected by copyright laws. <br/>
  * Date： 2019年09月18日
  *
  * @author 刘皓
  * @version : 1.0
  */
object ChannelReport {

  def main(args: Array[String]): Unit = {
    //创建sparksession对象
    val spark=SparkSession.builder()
      .appName("ChannelReport")
      .master("local[*]")
      .getOrCreate()


    //读取字典文件数据

    val rdd: collection.Map[String, String] = spark.sparkContext.textFile("F:\\data\\app_dict.txt")
      .map(_.split("\\s"))
      .filter(_.length >= 5)
      .map(arr => {
        (arr(4), arr(1))
      }).collectAsMap()

    //进行广播
     val broadcast: Broadcast[collection.Map[String, String]] = spark.sparkContext.broadcast(rdd)


    //读取日志信息
 val rdd1: RDD[Row] = spark.read.parquet("F:\\data\\project.parquet").rdd

    import spark.implicits._
   rdd1.map(line => {

     var appName = line.getAs[String]("appname")
     if(StringUtils.isBlank(appName)){
       appName = broadcast.value.getOrElse(line.getAs[String]("appid"),"unknow")
     }
      val requestmode: Int = line.getAs[Int]("requestmode")
      val processnode: Int = line.getAs[Int]("processnode")
      val iseffective: Int = line.getAs[Int]("iseffective")
      val isbilling: Int = line.getAs[Int]("isbilling")
      val isbid: Int = line.getAs[Int]("isbid")
      val iswin: Int = line.getAs[Int]("iswin")
      val adorderid: Int = line.getAs[Int]("adorderid")
      val winprice: Double = line.getAs[Double]("winprice")
      val adpayment: Double = line.getAs[Double]("adpayment")

      //省市
      val provincename: String = line.getAs[String]("provincename")
      val cityname: String = line.getAs[String]("cityname")

      val list1: List[Double] = ReqUtils1.reqNum(requestmode, processnode)
      val list2: List[Double] = ReqUtils1.clickNum(requestmode, iseffective)
      val list3: List[Double] = ReqUtils1.advNum(iseffective, isbilling, isbid, iswin, adorderid, winprice, adpayment)

      val list: List[Double] = list1 ++ list2 ++ list3
     (appName,list)
    }).reduceByKey((list1,list2)=>{
     list1.zip(list2).map(t=>t._1+t._2)
   }).map(t=>t._1+","+t._2.mkString(","))

      .saveAsTextFile("F:\\data\\MediaAnalysis")


    }


}
