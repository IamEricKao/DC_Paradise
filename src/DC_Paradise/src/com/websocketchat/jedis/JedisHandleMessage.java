package com.websocketchat.jedis;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;



public class JedisHandleMessage {
	//1.����s�u��
	private static JedisPool pool = JedisPoolUtil.getJedisPool();
	
	
	public static List<String> getHistoryMsg(String sender, String receiver) {
		//4.�]�p�ҭn�x�s��key name
		//���d��key���]�p�ĥ�(�o�e�̷|���s��:�����̷|���s��)
		String key = new StringBuilder(sender).append(":").append(receiver).toString();
		//2.����Y�ӳs��
		Jedis jedis = null;
		jedis = pool.getResource();
		//3.��J�K�X
		jedis.auth("123456");
	
//		lrange���o��start~stop�d��value Redis��key���  �}�l�Gindex[0] �̫�Gllen�^�Ǹ�(key)list������ -1(���ޭȱq1�}�l)  
		//history=�Ҧ��s�bkey�̭�(��Ѭ���)����
		List<String> historyData = jedis.lrange(key, 0, jedis.llen(key) - 1);
		jedis.close();
		return historyData;
	}

	public static void saveChatMessage(String sender, String receiver, String message) {
		// ������ӻ��A���n�U�s�۾��v��ѰO��
		String senderKey = new StringBuilder(sender).append(":").append(receiver).toString();
		String receiverKey = new StringBuilder(receiver).append(":").append(sender).toString();
		//2.����Y�ӳs��
		Jedis jedis = pool.getResource();
		//3.��J�K�X
		jedis.auth("123456");
		//rpush ��(�T��)�q�k�䴡�J��
		jedis.rpush(senderKey, message);
		jedis.rpush(receiverKey, message);

		jedis.close();
	}

}
