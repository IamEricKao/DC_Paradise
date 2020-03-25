package com.websocketchat.jedis;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;



public class JedisHandleMessage {
	//1.獲取連線池
	private static JedisPool pool = JedisPoolUtil.getJedisPool();
	
	
	public static List<String> getHistoryMsg(String sender, String receiver) {
		//4.設計所要儲存的key name
		//此範例key的設計採用(發送者會員編號:接收者會員編號)
		String key = new StringBuilder(sender).append(":").append(receiver).toString();
		//2.獲取某個連接
		Jedis jedis = null;
		jedis = pool.getResource();
		//3.輸入密碼
		jedis.auth("123456");
	
//		lrange取得的start~stop範圍的value Redis的key表格  開始：index[0] 最後：llen回傳該(key)list的長度 -1(索引值從1開始)  
		//history=所有存在key裡面(聊天紀錄)的值
		List<String> historyData = jedis.lrange(key, 0, jedis.llen(key) - 1);
		jedis.close();
		return historyData;
	}

	public static void saveChatMessage(String sender, String receiver, String message) {
		// 對雙方來說，都要各存著歷史聊天記錄
		String senderKey = new StringBuilder(sender).append(":").append(receiver).toString();
		String receiverKey = new StringBuilder(receiver).append(":").append(sender).toString();
		//2.獲取某個連接
		Jedis jedis = pool.getResource();
		//3.輸入密碼
		jedis.auth("123456");
		//rpush 值(訊息)從右邊插入值
		jedis.rpush(senderKey, message);
		jedis.rpush(receiverKey, message);

		jedis.close();
	}

}
