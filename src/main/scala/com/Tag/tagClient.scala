package com.Tag

import com.util.Tag
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
object tagClient  extends Tag{
  override def makeTags(args: Any*): List[(String, Int)] = {
    //获取数据
    val row: Row = args(0).asInstanceOf[Row]
    //list
    var  list =List[(String,Int)]()
    //获取设备
    //client: Int,	设备类型 （1：android 2：ios 3：wp）
    /**
      * ispid: Int,	运营商 id
      * networkmannername: String,	联网方式 String
      */
    val client: Int = row.getAs[Int]("client")
    client match{
      case 1 => list:+=("D00010001",1)
      case 2 => list:+=("D00010002",1)
      case 3 => list:+=("D00010003",1)
      case _ => list:+=("其他",1)
    }

    val networkmannername: String = row.getAs[String]("networkmannername")
    networkmannername match{
      case "WIFI"=>list:+=("D00020001",1)
      case "4G"=>list:+=("D00020002",1)
      case "3G"=>list:+=("D00020003",1)
      case "2G"=>list:+=("D00020004",1)
      case _=>list:+=("D00020005",1)
    }

    val ispname: String = row.getAs[String]("ispname")

    ispname  match {
      /**
        * 移 动 D00030001
        * 联 通 D00030002
        * 电 信 D00030003
        * _ D00030004
        */
      case "移动"  =>list:+=("D00030001",1)
      case "联通"  =>list:+=("D00030002",1)
      case "电信"  =>list:+=("D00030003",1)
      case _  =>list:+=("D00030004",1)

    }
    list
  }

}
