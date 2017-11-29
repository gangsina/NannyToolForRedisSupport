package com.bentengwu.nannytoolforredissupport;

import redis.clients.jedis.Jedis;

/**
 * 用于jedis的接口执行.
 *@author：email: <a href="bentengwu@163.com"> thender </a> 
 *@Date 2016年9月21日 下午3:59:50 
 */
public abstract class JedisExecuter {
	public abstract Object execute(Jedis jedis);
}
