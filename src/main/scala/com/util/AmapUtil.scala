package com.util

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}

/**
  * Description：xxxx<br/>
  * Copyright (c) ， 2018， Jansonxu <br/>
  * This program is protected by copyright laws. <br/>
  * Date： 2019年09月21日
  *
  * @author 刘皓
  * @version : 1.0
  */
/**
  * 从高德地图获得商圈信息
  */
object AmapUtil {

  def  getBusinessFromAmap(long:Double,lat:Double):String={
    //
    val  location=long+","+lat
    //获取URL
    val  url="https://restapi.amap.com/v3/geocode/regeo?location="+location+"&key=cb3ecd6a11c90806b7d57aa0b3dff1f4"
    //调用Http接口发送请求
    val jsonstr: String = HttpUtil.get(url)
    //解析json
    val jsonObject=JSON.parseObject(jsonstr)
    //判断当前状态是否为1
    val status=jsonObject.getIntValue("status")
    if(status == 0)return ""
    //如果不为空
    val jsonObject1: JSONObject = jsonObject.getJSONObject("regeocode")
    if(jsonObject1==null)return ""
    val jSONObject2: JSONObject = jsonObject1.getJSONObject("addressComponent")
    if(jSONObject2==null)return ""
    val jSONArray: JSONArray = jSONObject2.getJSONArray("businessAreas")

    if (jSONArray==null)return ""

  //定义集合取值
    val  result=collection.mutable.ListBuffer[String]()
    //循环数组
    for(item <- jSONArray.toArray()){
      if(item.isInstanceOf[JSONObject]){
        val json: JSONObject = item.asInstanceOf[JSONObject]
        val name: String = json.getString("name")

        result.append(name)
      }
    }
    //商圈名字
    result.mkString(",")
  }
}
