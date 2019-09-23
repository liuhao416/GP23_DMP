package com.util

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

/**
  * Description：xxxx<br/>
  * Copyright (c) ， 2018， Jansonxu <br/>
  * This program is protected by copyright laws. <br/>
  * Date： 2019年09月21日
  *
  * @author 刘皓
  * @version : 1.0
  */
object JedisConnectionPool {
  //创建连接
  val config = new JedisPoolConfig()

  config.setMaxTotal(20)

  config.setMaxIdle(10)

  private val pool = new JedisPool(config,"NODE01",6379,10000)

  def getConnection():Jedis={
    pool.getResource
  }
}
