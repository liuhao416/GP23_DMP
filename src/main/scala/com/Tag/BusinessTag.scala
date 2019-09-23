package com.Tag

import ch.hsr.geohash.GeoHash
import com.util.{AmapUtil, JedisConnectionPool, String2Type, Tag}
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.Row

/**
  * Description：xxxx<br/>
  * Copyright (c) ， 2018， Jansonxu <br/>
  * This program is protected by copyright laws. <br/>
  * Date： 2019年09月21日
  *
  * @author 刘皓
  * @version : 1.0
  */
object BusinessTag  extends Tag{
  override def makeTags(args: Any*): List[(String, Int)] = {
    var list = List[(String, Int)]()

    //获取数据
    val row: Row = args(0).asInstanceOf[Row]

    //获取经纬度
    if (
      String2Type.toDouble(row.getAs[String]("long")) >= 73
        && String2Type.toDouble(row.getAs[String]("long")) <= 136
        && String2Type.toDouble(row.getAs[String]("lat")) >= 3
        && String2Type.toDouble(row.getAs[String]("lat")) <= 53
    ) {
      val long: Double = row.getAs[String]("long").toDouble
      val lat: Double = row.getAs[String]("lat").toDouble

      //获取商圈的名称
      val business: String = getBusiness(long,lat)

      if(StringUtils.isNoneBlank(business)){
        val str =business.split(",")
        str.foreach(str=>{
          list:+=(str,1)
        })
      }

    }
    list
  }
    /**
      * 获取商圈的信息
      */
    def getBusiness(long: Double, lat: Double): String = {
      //GeoHash码
      val geoHash: String = GeoHash.geoHashStringWithCharacterPrecision(lat, long, 6)
      //数据库查询当前商圈信息
      var business = redis_queryBusiness(geoHash)
      //去高德请求
      if(business == null){
        business=AmapUtil.getBusinessFromAmap(long,lat)
        //将高德获得的商圈存储数据库
        if(business!=null && business.length>0){
          redis_insertBusiness(geoHash,business)
        }
      }
      business
    }


    /**
      * 数据库获取商圈信息
      */
    def redis_queryBusiness(geoHash: String): String = {
      val jedis = JedisConnectionPool.getConnection()
      val business: String = jedis.get(geoHash)

      jedis.close()
      business
    }

    /**
      * 将商圈保存数据库
      */
    def redis_insertBusiness(getHash: String, business: String): Unit = {
      val jedis = JedisConnectionPool.getConnection()
      jedis.set(getHash, business)
      jedis.close()
    }
  }
