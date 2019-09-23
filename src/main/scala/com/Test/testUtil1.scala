package com.Test

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
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
object testUtil1 {

  def  getType(str: String):String= {

//    //解析json
//    val jsonObject=JSON.parseObject(str)
//    //判断当前状态是否为1
//    val status=jsonObject.getIntValue("status")
//    if(status == 0)return ""
//    //如果不为空
//    val jsonObject1: JSONObject = jsonObject.getJSONObject("regeocode")
//    if(jsonObject1==null)return ""
//    val jSONArray: JSONArray = jsonObject1.getJSONArray("pois")
//
//    if (jSONArray==null)return ""
//
//
//    //定义集合取值
//    val  result=collection.mutable.ListBuffer[String]()
//    //循环数组
//    for(item <- jSONArray.toArray()) {
//      if (item.isInstanceOf[JSONObject]) {
//        val json: JSONObject = item.asInstanceOf[JSONObject]
//        val businessarea: String = json.getString("businessarea")
//
//        result.append(businessarea)
//
//      }
//    }
//    //businessarea名字
//    result.mkString(",")

    //使用fastjson解析数据
    //val jsonObject=JSON.parseObject(str)
    val jSONObject: JSONObject = JSON.parseObject(str)
    //获取状态
    //val status=jsonObject.getIntValue("status")
    val status: Int = jSONObject.getIntValue("status")
    //判断状态是否合法
    //判断当前状态是否为1
    //    if(status == 0)return ""
    if (status == 0) return ""
    //获取第一层json值
    //val jsonObject1: JSONObject = jsonObject.getJSONObject("regeocode")
    val jSONObject1: JSONObject = jSONObject.getJSONObject("regeocode")
    //判断json值是否符合条件
    ////    if(jsonObject1==null)return ""
    if (jSONObject1 == null) return ""
    //判断第二层是否合法
    val jSONArray: JSONArray = jSONObject1.getJSONArray("pois")
    //判断是否合法
    if (jSONArray == null) return ""
    //使用集合对数据进行存储
    val list = collection.mutable.ListBuffer[String]()
    //for(item <- jSONArray.toArray()) {
      //      if (item.isInstanceOf[JSONObject]) {
      //        val json: JSONObject = item.asInstanceOf[JSONObject]
      //        val businessarea: String = json.getString("businessarea")
      //
      //        result.append(businessarea)
      //
    for (item <- jSONArray.toArray()) {
      if (item.isInstanceOf[JSONObject]) {
        val jSONObject2: JSONObject = item.asInstanceOf[JSONObject]
        val type1: String = jSONObject2.getString("type")
         list.append(type1)
      }
    }
    list.mkString(",")
  }
}
