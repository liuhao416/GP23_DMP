package com.Tag

import com.util.Tag
import org.apache.commons.lang3.StringUtils
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
  * rtbprovince: String,	rtb 省
  * rtbcity: String,	rtb 市
  * 地域标签（省标签格式：ZPxxx->1, 地市标签格式: ZCxxx->1）xxx 为省或市名称
  */
object tagPC extends Tag{
  override def makeTags(args: Any*): List[(String, Int)] = {
      //获取数据
    val row: Row = args(0).asInstanceOf[Row]

    //创建接受数据的list
    var  list =List[(String,Int)]()

    //获取省市的字段

    val rtbprovince: String = row.getAs[String]("rtbprovince")
    if(StringUtils.isNoneBlank(rtbprovince)) {
      list :+= ("ZP" + rtbprovince, 1)
    }
    val rtbcity: String = row.getAs[String]("rtbcity")
    if(StringUtils.isNoneBlank(rtbcity)) {
      list :+= ("ZP" + rtbcity, 1)
    }
    list
  }
}
