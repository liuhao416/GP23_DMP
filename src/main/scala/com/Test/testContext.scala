package com.Test

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
  * Description：xxxx<br/>
  * Copyright (c) ， 2018， Jansonxu <br/>
  * This program is protected by copyright laws. <br/>
  * Date： 2019年09月22日
  *
  * @author 刘皓
  * @version : 1.0
  */
object testContext {

  def main(args: Array[String]): Unit = {
    //创建sparksession对象
    val spark = SparkSession.builder()
      .appName("testContext")
      .master("local[*]")
      .getOrCreate()

    //读取json 数据
     val rdd: RDD[String] = spark.read.textFile("F:\\data\\test\\json.txt").rdd
    import spark.implicits._
    val rdd1: RDD[String] = rdd.map(row => {
      val str: String = row.toString()
      val name: String = textUtil.getBusinessarea(str)
      name
    })
    //并统计每个businessarea的总数。

    val value: RDD[String] = rdd1.flatMap(_.split(","))
    val res: RDD[(String, Int)] = value.map((_,1)).reduceByKey(_+_)

    res.foreach(println)



  }

}
