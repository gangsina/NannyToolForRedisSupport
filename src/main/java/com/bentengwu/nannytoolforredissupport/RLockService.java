package com.bentengwu.nannytoolforredissupport;

import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author <a href="bentengwu@163.com">thender.xu</a>
 * @Date 2017/11/29 16:28.
 */
public class RLockService extends RAbstractService  {

    /**
     * @param key 以".lock" 的字符串.
     * @return
     */
    public boolean lock(final String key) {
        return (boolean) execute(new JedisExecuter() {
            @Override
            public Object execute(Jedis jedis) {
                String tmpKey = key ;
                if (!key.endsWith(".lock")) {
                    tmpKey = tmpKey + ".lock";
                }
                return jedis.setnx(tmpKey, Thread.currentThread().getName()).equals(OK);
            }
        });
    }

    /**
     * @param key   以".lock" 的字符串.
     * @return 解锁
     */
    public boolean unlock(final String key) {
         return (boolean)execute(new JedisExecuter() {
            @Override
            public Object execute(Jedis jedis) {
                String tmpKey = key ;
                if (!key.endsWith(".lock")) {
                    tmpKey = tmpKey + ".lock";
                }
                return jedis.del(tmpKey)>0;
            }
        });
    }

    /**
     * 利用内存db来实现锁竞争资源.
     * @param key      用于作为锁的键
     * @param executer 执行器.
     * @return 当没有执行的时候返回null.如果已经执行,按照执行器执行的代码结果为返回结果.
     */
    public Object lockExecute(final String key, final ExecuteWithLock executer) {
        return lockExecute(key, 10, executer);
    }

    /**
     * @param key 用于作为锁的键. 以".lock"结尾,当不是,会被加上
     * @param seconds 锁被保留的时间
     * @param executer 执行器
     * @return 当没有执行的时候返回null.如果已经执行,按照执行器执行的代码结果为返回结果.
     */
    public Object lockExecute(final String key,final int seconds, final ExecuteWithLock executer) {
        return lockExecute(new String[]{key}, seconds, executer);
    }

    /**
     * @param keys 用于作为锁的键. 以".lock"结尾,当不是,会被加上
     * @param seconds 锁被保留的时间
     * @param executer 执行器
     * @return 当没有执行的时候返回null.如果已经执行,按照执行器执行的代码结果为返回结果.
     */
    public Object lockExecute(final String[] keys,final int seconds, final ExecuteWithLock executer) {
        return execute(new JedisExecuter() {
            @Override
            public Object execute(Jedis jedis) {
                Set<String> set = new HashSet<String>();
                try {
                    for (String key : keys) {
                        String tmpKey = key;
                        if (!key.endsWith(".lock")) {
                            tmpKey = tmpKey + ".lock";
                        }
                        boolean bret = jedis.set(tmpKey, Thread.currentThread().getName(), "NX", "EX", seconds).equals(OK);
                        if (bret) {
                            set.add(tmpKey);
                        }else{
                            return null;
                        }
                    }
                   return executer.executeWithLock();
                } finally {
                    for (String key : set) {
                        jedis.del(key);
                    }
                }
            }
        });
    }
}
