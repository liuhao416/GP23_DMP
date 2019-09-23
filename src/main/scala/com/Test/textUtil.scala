package com.Test

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import com.util.HttpUtil
import org.apache.spark.rdd.RDD
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
object textUtil {

  def  getBusinessarea(str:String):String={

    //解析json
    val jsonObject=JSON.parseObject(str)
    //判断当前状态是否为1
    val status=jsonObject.getIntValue("status")
    if(status == 0)return ""
    //如果不为空
    val jsonObject1: JSONObject = jsonObject.getJSONObject("regeocode")
    if(jsonObject1==null)return ""
    val jSONArray: JSONArray = jsonObject1.getJSONArray("pois")

    if (jSONArray==null)return ""


    //定义集合取值
    val  result=collection.mutable.ListBuffer[String]()
    //循环数组
    for(item <- jSONArray.toArray()) {
      if (item.isInstanceOf[JSONObject]) {
        val json: JSONObject = item.asInstanceOf[JSONObject]
        val businessarea: String = json.getString("businessarea")

        result.append(businessarea)

      }
    }
    //businessarea名字
    result.mkString(",")
  }
}
