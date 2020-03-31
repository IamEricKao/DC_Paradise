package com.websocketchat.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class JedisPoolUtil {

	private static JedisPool pool = null;
	
	
	private JedisPoolUtil() {
	}
//	獲取連線
	public static  JedisPool getJedisPool() {
		
		if (pool == null) {
			synchronized (JedisPoolUtil.class) {
				if (pool == null) {
					JedisPoolConfig config = new JedisPoolConfig();
					config.setMaxTotal(8);// 可用連線例項的最大數目,如果賦值為-1,表示不限制.
					config.setMaxIdle(8); //控制一個Pool最多有多少個狀態為idle(空閒的)jedis例項,預設值也是8
					config.setMaxWaitMillis(10000);// 等待可用連線的最大時間,單位毫秒,預設值為-1,表示永不超時/如果超過等待時間,則直接丟擲異常
					pool = new JedisPool(config, "localhost", 6379);//和指定的Redis進行通信
				}
			}
		}
		return pool;

	} 

	// 可在ServletContextListener的contextDestroyed裡呼叫此方法註銷Redis連線池
	public static void shutdownJedisPool() {
		if (pool != null)
			pool.destroy();
	}
	
	
}
