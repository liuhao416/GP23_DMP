package com.util

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.util.EntityUtils

/**
  * Description：xxxx<br/>
  * Copyright (c) ， 2018， Jansonxu <br/>
  * This program is protected by copyright laws. <br/>
  * Date： 2019年09月21日
  *
  * @author 刘皓
  * @version : 1.0
  */
object HttpUtil {

  def get(url:String):String={


  val client: CloseableHttpClient = HttpClients.createDefault()

    val httpGet = new HttpGet(url)

    //获取发送请求

    val httpResponse: CloseableHttpResponse = client.execute(httpGet)

    //处理返回后的结果
    //为了防止出现乱码进行处理

    EntityUtils.toString(httpResponse.getEntity,"UTF-8")

  }
}
