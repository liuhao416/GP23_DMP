package com.adTest

/**
  * 打标签接口
  */
trait Tag {

  def makeTags(args:Any*):List[(String,Int)]
}
