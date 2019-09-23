package com.Test

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Description：xxxx<br/>
  * Copyright (c) ， 2018， Jansonxu <br/>
  * This program is protected by copyright laws. <br/>
  * Date： 2019年09月22日
  *
  * @author 刘皓
  * @version : 1.0
  */
object testContext1 {

    def main(args: Array[String]): Unit = {

     //sparksession对象
      val  spark =SparkSession.builder()
        .appName("testContext1")
        .master("local[*]")
        .getOrCreate()


      //获取数据
      val rdd: RDD[String] = spark.read.textFile("F:\\data\\test\\json.txt").rdd

      //获取type类型
      rdd.map(row=>{
        val str: String = row.toString
            val type1: String = testUtil1.getType(str)
        type1
      }).flatMap(_.split(","))

    }


}
