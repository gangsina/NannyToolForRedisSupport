package com.bentengwu.nannytoolforredissupport;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * REDIS 访问的抽象类. 
 *@author：email: <a href="bentengwu@163.com"> thender </a> 
 *@Date 2016-6-9 下午1:14:15 
 */
public abstract class RAbstractService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private JedisPool jedisPool;
	
	private long activeCount = 0L;
	private long totalCount = 0L;
	private long totalFreeCount = 0L;
	
	protected final static String OK = "OK";

	public RAbstractService() {
		logDebug();
	}
	
	
	public Object execute(JedisExecuter executer){
		Jedis jedis = getJedis();
		try{
			return executer.execute(jedis);
		}finally{
			free(jedis);
		}
	}
	
	
	/**
	 * 获取资源
	 *<br />
	 *@date 2016-6-9 下午1:18:38
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@return 获取资源
	 */
	protected Jedis getJedis(){
		synchronized(this){
			activeCount++;
			totalCount++;
		}
		return jedisPool.getResource();
	}
	
	/**
	 * 释放资源
	 *<br />
	 *@date 2016-6-9 下午1:21:47
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param jedis
	 */
	protected void free(Jedis jedis){
		synchronized(this) {
			activeCount--;
			totalFreeCount++;
		}
		jedis.close();
	}
	
	
	/**
	 * 校验密码
	 *<br />
	 *@date 2015-7-7 下午3:11:59
	 *@author <a href="bentengwu@163.com">伟宏</a>
	 *@param password	密码
	 *@return	校验密码. 成功为true 失败为false.
	 *@since 1.0
	 */
	public boolean auth(String password)
	{
		Jedis jedis = getJedis();
		try {
			return jedis.auth(password).equals(OK);
		} finally {
			free(jedis);
		}
	}
	
	
	
	/**
	 * 日志当前的连接池信息
	 *<br />
	 *@date 2016-6-9 下午1:41:53
	 *@author <a href="bentengwu@163.com">thender</a>
	 */
	public void logDebug()
	{
		Thread log = new Thread(new Runnable() {
			private Logger logger = LoggerFactory.getLogger(this.getClass());
			@Override
			public void run() {
				while(true)
				{
					try {
						TimeUnit.MINUTES.sleep(1);
						if(getJedisPool()==null)
						{
							logger.warn("redis 连接失败,请检查配置!");
							continue;
						}
						logger.info("redis 连接日志: [当前连接数:{},检测到的活跃连接数:{}, 项目启动后总连接数:{}, 项目启动后总释放数:{} ]",
								getJedisPool().getNumActive(),
								activeCount,totalCount,totalFreeCount);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						
					}
				}
			}
		});
	}
	
	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	
	public static final String KEY_NONE="none";
	public static final String KEY_STRING="string";
	public static final String KEY_LIST="list";
	public static final String KEY_SET="set";
	public static final String KEY_ZSET="zset";
	public static final String KEY_HASH="hash";
	
	/**
	 * 读取Redis中的mainkey集合.
	 *<br />
	 *@date 2015-6-30 下午2:03:48
	 *@author <a href="bentengwu@163.com">伟宏</a>
	 *@param keyPattern 规则.
	 *@return	 Redis中的mainkey集合
	 *@since 1.0
	 */
	public Set<String> findKey(String keyPattern)
	{
		Jedis jedis = getJedis();
		try {
			return jedis.keys(keyPattern);
		} finally {
			free(jedis);
		}
	}
	
        /**
         * 判断是否存在
         * @param keyPattern
         * @return  true 存在
         *          false不存在
         */
        public boolean exists(String keyPattern)
        {
                Jedis jedis = getJedis();
		try {
			return jedis.exists(keyPattern);
		} finally {
			free(jedis);
		}
        }
        
	
	/**
	 * 读取mainkey的类型
	 * Return the type of the value stored at key in form of a string. 
	 * The type can be one of "none", "string", "list", "set","zset". 
	 * "none" is returned if the key does not exist. Time complexity: O(1)
	 *<br />
	 *@date 2015-6-30 下午2:05:33
	 *@author <a href="bentengwu@163.com">伟宏</a>
	 *@param key	mainkey
	 *@return	返回对应的类型
	 *@since 1.0
	 */
	public String getKeyType(String key)
	{
		Jedis jedis = getJedis();
		try {
			return jedis.type(key);
		} finally {
			free(jedis);
		}
	}
	
	
	/**
	 * 
		以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
		
		可用版本：
		    >= 1.0.0
		时间复杂度：
		    O(1)
	 *<br />
	 *@date 2015-6-30 下午2:07:21
	 *@author <a href="bentengwu@163.com">伟宏</a>
	 *@param key
	 *@return 
	 *		 当 key 不存在时，返回 -2 。
		    当 key 存在但没有设置剩余生存时间时，返回 -1 。
		    否则，以秒为单位，返回 key 的剩余生存时间。
	 *@since 1.0
	 */
	public long getKeyTTL(String key)
	{
		Jedis jedis = getJedis();
		try {
			return jedis.ttl(key);
		} finally {
			free(jedis);
		}
	}
	
	/**
	 * 读取key的长度
	 *<br />
	 *@date 2015-6-30 下午2:18:37
	 *@author <a href="bentengwu@163.com">伟宏</a>
	 *@param key
	 *@return	如果key不存在  -1
	 *			如果key是字符串 1
	 *			其他按照长度读取它的值.
	 *@since 1.0
	 */
	public Long getKeyLength(String key)
	{
		String type = getKeyType(key);
		if(StringUtils.equalsIgnoreCase(type, KEY_STRING))
		{
			return 1L;
		}else if(StringUtils.equalsIgnoreCase(type, KEY_SET))
		{
			return getSetLen(key);
		}else if(StringUtils.equalsIgnoreCase(type, KEY_LIST))
		{
			return getListLen(key);
		}else if(StringUtils.equalsIgnoreCase(type, KEY_HASH))
		{
			return getMapLen(key);
		}else{
			return -1L;
		}
	}
	
	/**
	 * 删除对应mainkey.
	 *<br />
	 *@date 2015-7-3 上午8:57:21
	 *@author <a href="bentengwu@163.com">伟宏</a>
	 *@param keys	一个或者多个key.
	 *@return
	 *@since 1.0
	 */
	public Long deleteKeys(String...keys)
	{
		if(keys==null || keys.length==0)
			return 0L;
		Jedis jedis = getJedis();
		try {
			long rmNum = jedis.del(keys);
			return rmNum;
		} finally {
			free(jedis);
		}
		
	}
	
	
	/*读取list的长度*/
	public  Long getListLen(final String key){
		return (Long)execute(new JedisExecuter() {
			@Override
			public Object execute(Jedis jedis) {
				return jedis.llen(key);
			}
		});
	}
	
	/*读取Set的长度*/
	public  Long getSetLen(final String key){
		return (Long)execute(new JedisExecuter() {
			@Override
			public Object execute(Jedis jedis) {
				return jedis.scard(key);
			}
		});
	}
	
	/*读取map的key数*/
	public  Long getMapLen(final String key){
		return (Long)execute(new JedisExecuter() {
			@Override
			public Object execute(Jedis jedis) {
				return jedis.hlen(key);
			}
		});
	}
}	
