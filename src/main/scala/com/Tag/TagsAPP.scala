package com.Tag

import java.util

import com.util.Tag
import org.apache.commons.lang3.StringUtils
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
/**
  * 媒体标签
  */
object TagsAPP extends Tag {
  override def makeTags(args: Any*): List[(String, Int)] = {
    /**
      * 2)App 名称（标签格式： APPxxxx->1）xxxx 为 App 名称，
      * 使用缓存文件 appname_dict 进行名称转换；APP 爱奇艺->1
      */
    //创建list
    var list = List[(String, Int)]()
    val row: Row = args(0).asInstanceOf[Row]
    val appdocs: Broadcast[collection.Map[String, String]] = args(1).asInstanceOf[Broadcast[collection.Map[String, String]]]
    //获取appname   appid
    val appid: String = row.getAs[String]("appid")
    val appname: String = row.getAs[String]("appname")

    if (StringUtils.isNoneBlank(appname)) {
      list :+= ("APP" + appname, 1)
    } else {
      list :+= ("APP" + appdocs.value.getOrElse(appid, "其他"), 1)
    }
    list
  }
}