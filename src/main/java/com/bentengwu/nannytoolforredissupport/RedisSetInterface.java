package com.bentengwu.nannytoolforredissupport;

import java.util.Set;

/**
 * 用于抽象REDIS中SET的操作方法.
 * TODO 等待storeservice实现这个接口。 这里提取了所有的ＳＥＴ的相关操作。　很多方法还是比较实用的。
 *@类名称：RedisSetInterface.java
 *@文件路径：com.bentengwu.service.system
 *@author：email: <a href="bentengwu@163.com"> thender </a> 
 *@Date 2015-11-25 下午8:24:00 
 * @since 1.0.2
 */

public interface RedisSetInterface {	
	
	
	/**
	 * 
	 * # 添加单个元素

				redis> SADD bbs "discuz.net"
				(integer) 1
				
				# 添加重复元素
				
				redis> SADD bbs "discuz.net"
				(integer) 0
				
				# 添加多个元素
				
				redis> SADD bbs "tianya.cn" "groups.google.com"
				(integer) 2
				
				redis> SMEMBERS bbs
				1) "discuz.net"
				2) "groups.google.com"
				3) "tianya.cn"
	 * 
	 * 
	 * 
	 * 将一个或多个member元素加入到集合key当中，已经存在于集合的member元素将被忽略。
	 * 假如key不存在，则创建一个只包含member元素作成员的集合。
	 * 当key不是集合类型时，返回一个错误。
	 * 
	 * 
	 * Note 在Redis2.4版本以前，SADD只接受单个member值。
	 * 
	 * O(N)，N是被添加的元素的数量。
	 * 
	 *<br />
	 *@date 2015-11-25 下午8:34:17
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param key
	 *@param vals
	 *@return	被添加到集合中的新元素的数量，不包括被忽略的元素。
	 *@since 1.0
	 */
	public Integer sadd(String key, String...members);
	
	
	/**
	 * 
	 * # 测试数据

				redis> SMEMBERS languages
				1) "c"
				2) "lisp"
				3) "python"
				4) "ruby"
				
				# 移除单个元素
				
				redis> SREM languages ruby
				(integer) 1
				
				# 移除不存在元素
				
				redis> SREM languages non-exists-language
				(integer) 0
				
				# 移除多个元素
				
				redis> SREM languages lisp python c
				(integer) 3
				
				redis> SMEMBERS languages
				(empty list or set)
	 * 
	 * 
	 * 
	 * 移除集合key中的一个或多个member元素，不存在的member元素会被忽略。

		当key不是集合类型，返回一个错误。
		
		
	 * O(N)，N为给定member元素的数量。
	 * 
	 * 
	 *  在Redis2.4版本以前，SREM只接受单个member值
	 *  
	 *  
	 *<br />
	 *@date 2015-11-25 下午8:33:41
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param key
	 *@param vals
	 *@return	被成功移除的元素的数量，不包括被忽略的元素。
	 *@since 1.0
	 */
	public Integer srem(String key, String...members);
	
	
	/**
	 * 
	 * # 情况1：空集合

			redis> EXISTS not_exists_key    # 不存在的key视为空集合
			(integer) 0
			
			redis> SMEMBERS not_exists_key
			(empty list or set)
			
			
			# 情况2：非空集合
			
			redis> SADD programming_language python
			(integer) 1
			
			redis> SADD programming_language ruby
			(integer) 1
			
			redis> SADD programming_language c
			(integer) 1
			
			redis> SMEMBERS programming_language
			1) "c"
			2) "ruby"
			3) "python"
	 * 
	 * 
	 * 返回集合key中的所有成员。

			时间复杂度:
			O(N)，N为集合的基数。
			返回值:
			集合中的所有成员。
	 *<br />
	 *@date 2015-11-25 下午8:33:13
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param key
	 *@return	集合中的所有成员。
	 *@since 1.0
	 */
	public Set<String> smembers(String key);
	
	
	/**
	 * 判断member元素是否是集合key的成员。
	 * 时间复杂度:
			O(1)
	 * redis> SMEMBERS joe's_movies
		1) "hi, lady"
		2) "Fast Five"
		3) "2012"
		
		redis> SISMEMBER joe's_movies "bet man"
		(integer) 0
		
		redis> SISMEMBER joe's_movies "Fast Five"
		(integer) 1
	 *<br />
	 *@date 2015-11-25 下午8:32:20
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param key
	 *@param val
	 *@return	如果member元素是集合的成员，返回1。
				如果member元素不是集合的成员，或key不存在，返回0。
	 *@since 1.0.2
	 */
	public Integer sismember(String key,String member);
	
	
	
	/**
	 * 
	 * redis> SMEMBERS tool
			1) "pc"
			2) "printer"
			3) "phone"
			
			redis> SCARD tool
			(integer) 3
			
			redis> SMEMBERS fake_set
			(empty list or set)
			
			redis> SCARD fake_set
			(integer) 0
			
			
	 * 返回集合key的基数(集合中元素的数量)。
	 * 时间复杂度:O(1)
	 *<br />
	 *@date 2015-11-25 下午8:38:48
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param key
	 *@return		集合的基数。
					当key不存在时，返回0。
	 *@since 1.0.2
	 */
	public Integer scard(String key);
	
	
	
	/**
	 * redis> SMEMBERS songs
		1) "Billie Jean"
		2) "Believe Me"
		
		redis> SMEMBERS my_songs
		(empty list or set)
		
		redis> SMOVE songs my_songs "Believe Me"
		(integer) 1
		
		redis> SMEMBERS songs
		1) "Billie Jean"
		
		redis> SMEMBERS my_songs
		1) "Believe Me"
		
		
		将member元素从source集合移动到destination集合。
		SMOVE是原子性操作。
		
		如果source集合不存在或不包含指定的member元素，则SMOVE命令不执行任何操作，仅返回0。否则，member元素从source集合中被移除，并添加到destination集合中去。
		
		当destination集合已经包含member元素时，SMOVE命令只是简单地将source集合中的member元素删除。
		
		当source或destination不是集合类型时，返回一个错误。
		
		
		时间复杂度:O(1)
		
	 *<br />
	 *@date 2015-11-25 下午8:42:38
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param source
	 *@param destination
	 *@param member
	 *@return		如果member元素被成功移除，返回1。
	 *				如果member元素不是source集合的成员，并且没有任何操作对destination集合执行，那么返回0。
	 *@since 1.0
	 */
	public Integer smove(String source,String destination,String member);
	
	
	/**
	 * redis> SMEMBERS my_sites
		1) "huangz.iteye.com"
		2) "sideeffect.me"
		3) "douban.com/people/i_m_huangz"
		
		redis> SPOP my_sites
		"huangz.iteye.com"
		
		redis> SMEMBERS my_sites
		1) "sideeffect.me"
		2) "douban.com/people/i_m_huang"
	 *移除并返回集合中的一个随机元素。
	 *
	 *时间复杂度:O(1)
	 *
	 *<br />
	 *@date 2015-11-25 下午8:45:27
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param key
	 *@return		被移除的随机元素。 当key不存在或key是空集时，返回nil。
	 *@since 1.0
	 */
	public String spop(String key);
	
	
	/**
	 * redis> SMEMBERS joe's_movies
		1) "hi, lady"
		2) "Fast Five"
		3) "2012"
		
		redis> SRANDMEMBER joe's_movies
		"Fast Five"
		
		redis> SMEMBERS joe's_movies    # 集合中的元素不变
		1) "hi, lady"
		2) "Fast Five"
		3) "2012"
	 * 
	 * 返回集合中的一个随机元素。
	 * 该操作和SPOP相似，但SPOP将随机元素从集合中移除并返回，而SRANDMEMBER则仅仅返回随机元素，而不对集合进行任何改动。
	 * 时间复杂度:O(1)
	 *<br />
	 *@date 2015-11-25 下午8:47:55
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param key
	 *@return	被选中的随机元素。 当key不存在或key是空集时，返回nil。
	 *@since 1.0
	 */
	public String srandmember(String key);
	
	
	
	/**
	 * 
	 * redis> SMEMBERS group_1
			1) "LI LEI"
			2) "TOM"
			3) "JACK"   # <-
			
			redis> SMEMBERS group_2
			1) "HAN MEIMEI"
			2) "JACK"   # <-
			
			redis> SINTER group_1 group_2
			1) "JACK"
	 * 
	 * 返回所有集合的交集。
	 * 时间复杂度: O(N * M)，N为给定集合当中基数最小的集合，M为给定集合的个数。
	 *<br />
	 *@date 2015-11-25 下午8:52:22
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param sets  一个或者多个SET集合
	 *@return  不存在的key被视为空集。 返回所有集合的交集
	 *@since 1.0
	 */
	public Set<String> sinter(String key,String...keys); 
	
	
	
	/**
	 * redis> SMEMBERS songs
			1) "good bye joe"   # <-
			2) "hello,peter"
			
			redis> SMEMBERS my_songs
			1) "good bye joe"   # <-
			2) "falling"
			
			redis> SINTERSTORE song_and_my_song songs my_songs
			(integer) 1
			
			redis> SMEMBERS song_and_my_song
			1) "good bye joe"
	 *<br />
	 *
	 *此命令等同于SINTER，但它将结果保存到destination集合，而不是简单地返回结果集。

		如果destination集合已经存在，则将其覆盖。
		
		destination可以是key本身。
		
		时间复杂度: O(N * M)，N为给定集合当中基数最小的集合，M为给定集合的个数。
		返回值:
	 *
	 *@date 2015-11-25 下午8:57:56
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param destination
	 *@param keys
	 *@return	结果集中的成员数量。
	 *@since 1.0
	 */
	public Integer sinterstore(String destination,String key,String...keys);
	
	
	/**
	 * 返回一个集合的全部成员，该集合是所有给定集合的并集。
	 * 不存在的key被视为空集。
	 * 时间复杂度:O(N)，N是所有给定集合的成员数量之和。
	 *<br />
	 *@date 2015-11-25 下午9:02:43
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param keys 
	 *@return	并集成员的列表。
	 *@since 1.0
	 */
	public Set<String> sunion(String key,String...keys);
	
	
	/**
	 * 此命令等同于SUNION，但它将结果保存到destination集合，而不是简单地返回结果集。
	 * 如果destination已经存在，则将其覆盖。
	 * destination可以是key本身。
	 * 
	 * 时间复杂度: O(N)，N是所有给定集合的成员数量之和。
	 *<br />
	 *@date 2015-11-25 下午9:03:47
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param destination
	 *@param keys
	 *@return	结果集中的元素数量。
	 *@since 1.0
	 */
	public Integer sunionstore(String destination, String key ,String...keys);
	
	
	
	/**
	 * redis> SMEMBERS peter's_movies
		1) "bet man"
		2) "start war"
		3) "2012"   # <-
		
		redis> SMEMBERS joe's_movies
		1) "hi, lady"
		2) "Fast Five"
		3) "2012"   # <-
		
		redis> SDIFF peter's_movies joe's_movies
		1) "bet man"
		2) "start war"
	 * 返回一个集合的全部成员，该集合是所有给定集合的差集 。
	 * 不存在的key被视为空集。
	 * O(N)，N是所有给定集合的成员数量之和。
	 *<br />
	 *@date 2015-11-25 下午9:06:21
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param key	原始set(被减数)
	 *@param keys	一个或者多个减数
	 *@return	交集成员的列表。
	 *@since 1.0
	 */
	public Set<String> sdiff(String key,String...keys);
	
	
	/**
	 * 此命令等同于SDIFF，但它将结果保存到destination集合，而不是简单地返回结果集。
	 * 如果destination集合已经存在，则将其覆盖。
	 * destination可以是key本身。
	 * 
	 * O(N)，N是所有给定集合的成员数量之和。
	 *<br />
	 *@date 2015-11-25 下午9:07:37
	 *@author <a href="bentengwu@163.com">thender</a>
	 *@param destination 被覆盖存储的SET，如果存在。不存在则创建
	 *@param key  被减数
	 *@param keys　一个或者多个减数
	 *@return	结果集中的元素数量。
	 *@since 1.0
	 */
	public Integer sdiffstore(String destination,String key,String...keys);
}
