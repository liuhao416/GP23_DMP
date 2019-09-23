package com.util

/**
  * Description：xxxx<br/>
  * Copyright (c) ， 2018， Jansonxu <br/>
  * This program is protected by copyright laws. <br/>
  * Date： 2019年09月18日
  *
  * @author 刘皓
  * @version : 1.0
  */
object ReqUtils1 {
//请求数量
  def  reqNum(requestmode:Int,processnode:Int):List[Double]={
      //进行判断
    if (requestmode==1 && processnode==1){
      List[Double](1,0,0)
    }else if (requestmode==1  &&  processnode==2){
      List[Double](1,1,0)
    }else if (requestmode==1 &&  processnode==3){
      List[Double](1,1,1)
    }else{
      List(0,0,0)
    }
  }
  //广告数量
  def  clickNum(requestmode:Int,iseffective:Int):List[Double]={
    if(requestmode==2 && iseffective==1){
      List(1,0)
    }else if (requestmode==3  &&  iseffective==1){
      List(0,1)
    }else {
      List(0,0)
    }
  }

  //竞价

  def  advNum(iseffective:Int,isbilling:Int,
              isbid:Int,iswin:Int,adordeerid:Int,winprice:Double,adpayment:Double):List[Double]={
    if(iseffective ==1 && isbilling ==1 && isbid ==1){
      if(iseffective ==1 && isbilling ==1 && iswin ==1 && adordeerid !=0){
        List[Double](1,1,winprice/1000.0,adpayment/1000.0)
      }else{
        List[Double](1,0,0,0)
      }
    }else{
      List[Double](0,0,0,0)
    }

  }


}
