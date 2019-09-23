package com.util

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.Row
import py4j.StringUtil


/**
  * Description：xxxx<br/>
  * Copyright (c) ， 2018， Jansonxu <br/>
  * This program is protected by copyright laws. <br/>
  * Date： 2019年09月19日
  *
  * @author 刘皓
  * @version : 1.0
  */
object UserId {

      def getUserId(row: Row): String = {
            /**
              * imei: String,	Imei（移动设备识别码）
              * mac: String,	Mac（苹果设备）
              * idfa: String,	Idfa（广告标识符）
              * imeimd5: String,	imei_md5
              * macmd5: String,	mac_md5
              * idfamd5: String,	idfa_md5
              * openudidmd5: String,	openudid_md5
              * androididmd5: String,	androidid_md5
              */

            //判断用户的ID
            row match {
                  case v if StringUtils.isNotBlank(v.getAs[String]("imei")) => "IM"+v.getAs[String]("imei")
                  case v if StringUtils.isNotBlank(v.getAs[String]("mac"))=>"MC"+v.getAs[String]("mac")
                  case v if StringUtils.isNotBlank(v.getAs[String]("idfa")) => "ID"+v.getAs[String]("idfa")
                  case v if StringUtils.isNotBlank(v.getAs[String]("imeimd5"))=>"IM"+v.getAs[String]("imeimd5")
                  case v if StringUtils.isNotBlank(v.getAs[String]("macmd5")) => "MC"+v.getAs[String]("macmd5")
                  case v if StringUtils.isNotBlank(v.getAs[String]("idfamd5"))=>"ID"+v.getAs[String]("idfamd5")
                  case v if StringUtils.isNotBlank(v.getAs[String]("openudidmd5")) => "OD"+v.getAs[String]("openudidmd5")
                  case v if StringUtils.isNotBlank(v.getAs[String]("androididmd5"))=>"AD"+v.getAs[String]("androididmd5")
                  case t if StringUtils.isNotBlank(t.getAs[String]("imeisha1")) => "IM"+t.getAs[String]("imeisha1")
                  case t if StringUtils.isNotBlank(t.getAs[String]("macsha1")) => "MC"+t.getAs[String]("macsha1")
                  case t if StringUtils.isNotBlank(t.getAs[String]("idfasha1")) => "ID"+t.getAs[String]("idfasha1")
                  case t if StringUtils.isNotBlank(t.getAs[String]("openudidsha1")) => "OD"+t.getAs[String]("openudidsha1")
                  case t if StringUtils.isNotBlank(t.getAs[String]("androididsha1")) => "AD"+t.getAs[String]("androididsha1")
                  case _ =>"其他"
            }
      }
                 def  getUserId1(v:Row):List[String]={
                  //创建一个list存储数据

                  var  list=List[String]()
                       if(StringUtils.isNotBlank(v.getAs[String]("imei"))) list:+="IM"+v.getAs[String]("imei")
                       if(StringUtils.isNotBlank(v.getAs[String]("mac"))) list:+="MC"+v.getAs[String]("mac")
                       if(StringUtils.isNotBlank(v.getAs[String]("idfa"))) list:+="ID"+v.getAs[String]("idfa")
                       if(StringUtils.isNotBlank(v.getAs[String]("openudid"))) list:+="OD"+v.getAs[String]("openudid")
                       if(StringUtils.isNotBlank(v.getAs[String]("androidid"))) list:+="AD"+v.getAs[String]("androidid")
                       if(StringUtils.isNotBlank(v.getAs[String]("imeimd5"))) list:+="IM"+v.getAs[String]("imeimd5")
                       if(StringUtils.isNotBlank(v.getAs[String]("macmd5"))) list:+="MC"+v.getAs[String]("macmd5")
                       if(StringUtils.isNotBlank(v.getAs[String]("idfamd5"))) list:+="ID"+v.getAs[String]("idfamd5")
                       if(StringUtils.isNotBlank(v.getAs[String]("openudidmd5"))) list:+="OD"+v.getAs[String]("openudidmd5")
                       if(StringUtils.isNotBlank(v.getAs[String]("androididmd5"))) list:+="AD"+v.getAs[String]("androididmd5")
                       if(StringUtils.isNotBlank(v.getAs[String]("imeisha1"))) list:+="IM"+v.getAs[String]("imeisha1")
                       if(StringUtils.isNotBlank(v.getAs[String]("macsha1"))) list:+="MC"+v.getAs[String]("macsha1")
                       if(StringUtils.isNotBlank(v.getAs[String]("idfasha1"))) list:+="ID"+v.getAs[String]("idfasha1")
                       if(StringUtils.isNotBlank(v.getAs[String]("openudidsha1"))) list:+="OD"+v.getAs[String]("openudidsha1")
                       if(StringUtils.isNotBlank(v.getAs[String]("androididsha1"))) list:+="AD"+v.getAs[String]("androididsha1")
                       list

      }
}
