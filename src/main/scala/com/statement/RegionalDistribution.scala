package com.statement

import java.util.Properties

import com.typesafe.config.ConfigFactory
import com.util.ReqUtils1
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
object RegionalDistribution {
  def main(args: Array[String]): Unit = {
    //创建sparksession对象
    val  spark=SparkSession.builder()
//      .config("spark.serializer","org.apache.spark.serializer.KryoSerializer")
//      .config("spark.sql.parquet.compression.codec","snappy")
      .appName("RegionalDistribution")
      .master("local[2]")
      .getOrCreate()

    //读取数据

    val rdd: RDD[Row] = spark.read.parquet("F:\\data\\project.parquet").rdd

    //对数据进行拆分   得到有用的数据
    //REQUESTMODE	PROCESSNODE	ISEFFECTIVE	ISBILLING	ISBID	ISWIN	ADORDEERID,winprice,adpayment
    import spark.implicits._
    val df: DataFrame = rdd.map(line => {
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

      ((provincename, cityname), list)


    }).reduceByKey((list1, list2) => {
      val tuples: List[(Double, Double)] = list1.zip(list2)
      tuples.map(t => t._1 + t._2)
    }).map(t => t._1 + "," + t._2.mkString(","))

      .toDF()



      //.saveAsTextFile("F:\\data\\RegionalDistribution7.txt")



//
      //通过config进行对conf文件的解析

    val  load=ConfigFactory.load()



    //创建properties
    val  properties=new  Properties()

   properties.setProperty("user",load.getString("jdbc.user"))

   properties.setProperty("password",load.getString("jdbc.password"))
      df.write.jdbc(load.getString("jdbc.url"),load.getString("jdbc.tableName"),properties)

    spark.stop()
  }


}
