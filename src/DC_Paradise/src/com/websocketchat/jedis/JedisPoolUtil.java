package com.websocketchat.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class JedisPoolUtil {

	private static JedisPool pool = null;
	
	
	private JedisPoolUtil() {
	}
//	����s�u
	public static  JedisPool getJedisPool() {
		
		if (pool == null) {
			synchronized (JedisPoolUtil.class) {
				if (pool == null) {
					JedisPoolConfig config = new JedisPoolConfig();
					config.setMaxTotal(8);// �i�γs�u�Ҷ����̤j�ƥ�,�p�G��Ȭ�-1,��ܤ�����.
					config.setMaxIdle(8); //����@��Pool�̦h���h�֭Ӫ��A��idle(�Ŷ���)jedis�Ҷ�,�w�]�Ȥ]�O8
					config.setMaxWaitMillis(10000);// ���ݥi�γs�u���̤j�ɶ�,���@��,�w�]�Ȭ�-1,��ܥä��W��/�p�G�W�L���ݮɶ�,�h�������Y���`
					pool = new JedisPool(config, "localhost", 6379);//�M���w��Redis�i��q�H
				}
			}
		}
		return pool;

	} 

	// �i�bServletContextListener��contextDestroyed�̩I�s����k���PRedis�s�u��
	public static void shutdownJedisPool() {
		if (pool != null)
			pool.destroy();
	}
	
	
}
