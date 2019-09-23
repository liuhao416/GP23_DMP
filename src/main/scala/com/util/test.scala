package com.util

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Description：xxxx<br/>
  * Copyright (c) ， 2018， Jansonxu <br/>
  * This program is protected by copyright laws. <br/>
  * Date： 2019年09月21日
  *
  * @author 刘皓
  * @version : 1.0
  */
object test {
  def main(args: Array[String]): Unit = {
    //测试类

    val  sparkSession=SparkSession.builder()
      .appName("test")
      .master("local[*]")
      .getOrCreate()

    val  arr=Array("https://restapi.amap.com/v3/geocode/regeo?location=116.310003,39.991957&key=tcb3ecd6a11c90806b7d57aa0b3dff1f4&radius=1000&extensions=all")
    import sparkSession.implicits._
    val rdd: RDD[String] = sparkSession.sparkContext.makeRDD(arr)

    rdd.map(t=>{
      HttpUtil.get(t)
    }).foreach(println)
  }
}
