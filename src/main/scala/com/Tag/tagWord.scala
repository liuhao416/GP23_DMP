package com.Tag

import com.util.Tag
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.Row

/**
  * Description：xxxx<br/>
  * Copyright (c) ， 2018， Jansonxu <br/>
  * This program is protected by copyright laws. <br/>
  * Date： 2019年09月22日
  *
  * @author 刘皓
  * @version : 1.0
  */
object tagWord extends Tag{
  override def makeTags(args: Any*): List[(String, Int)] = {
    //创建接受数据的list
    var  list=List[(String,Int)]()
    //接受数据
    val row: Row = args(0).asInstanceOf[Row]

    //获取停留文档中的数据
    val stopword: Broadcast[collection.Map[String, Int]] = args(1).asInstanceOf[Broadcast[collection.Map[String, Int]]]

    //获取关键字字段
    //84	keywords: String,	关键字
    //并进行判断
       row.getAs[String]("keywords")
      .split("\\|")
      .filter(word => word.length >= 3 && !stopword.value.contains(word))
      .foreach(word=>list:+=("K"+word,1))

list
  }
}
