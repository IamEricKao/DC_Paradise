package com.websocketchat.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.websocketchat.jedis.JedisHandleMessage;
import com.websocketchat.model.ChatMessage;
import com.websocketchat.model.State;

import redis.clients.jedis.Jedis;


@ServerEndpoint("/FriendWS/{userName}")
public class FriendWS {
	private static Map<String, Session> sessionsMap = new ConcurrentHashMap<>();
	Gson gson = new Gson();
	ChatMessage chatMessage;
	String sender, receiver;
	
	@OnOpen
	public void onOpen(@PathParam("userName") String userName, Session userSession) throws IOException {
		
		int maxBufferSize = 500 * 1024;
		userSession.setMaxTextMessageBufferSize(maxBufferSize);
		userSession.setMaxBinaryMessageBufferSize(maxBufferSize);		
		/* save the new user in the map */
		sessionsMap.put(userName, userSession);
		/* Sends all the connected users to the new user */
		Set<String> userNames = sessionsMap.keySet();
		State stateMessage = new State("open", userName, userNames);
		String stateMessageJson = gson.toJson(stateMessage);
		Collection<Session> sessions = sessionsMap.values();
		for (Session session : sessions) {
			if (session.isOpen()) {
				session.getAsyncRemote().sendText(stateMessageJson);
			}
		}

		String text = String.format("Session ID = %s, connected; userName = %s%nusers: %s", userSession.getId(),
				userName, userNames);
		System.out.println(text);
	}

	@OnMessage
	public void onMessage(Session userSession, String message) {
		System.out.println("message received: " + message);
		chatMessage = gson.fromJson(message, ChatMessage.class);
		sender = chatMessage.getSender();
		receiver = chatMessage.getReceiver();
		String messageType = chatMessage.getMessageType();
		System.out.println("messageType = " + messageType);		
		
		if ("history".equals(chatMessage.getType())) {
			List<String> historyData = JedisHandleMessage.getHistoryMsg(sender, receiver);
			String historyMsg = gson.toJson(historyData);
			ChatMessage cmHistory = new ChatMessage("history", sender, receiver, historyMsg, "");
			if (userSession != null && userSession.isOpen()) {
				//±À¼½
				ByteBuffer buffer_cmHistory = ByteBuffer.wrap((gson.toJson(cmHistory).getBytes()));
				userSession.getAsyncRemote().sendBinary(buffer_cmHistory);
				return;
			}
		}
		
		Session receiverSession = sessionsMap.get(receiver);
		System.out.println("receiverSession: " + receiverSession);
		if (receiverSession != null && receiverSession.isOpen()) {
			if(messageType.equals("image")) {
				int imageLength = chatMessage.getMessage().getBytes().length;
				System.out.println("image length = " + imageLength);
				receiverSession.getAsyncRemote().sendBinary(ByteBuffer.wrap(message.getBytes()));
				System.out.println("Message received image: " + message);
			}else { 
				receiverSession.getAsyncRemote().sendText(message);
				System.out.println("Message received Text: " + message);
			}
			JedisHandleMessage.saveChatMessage(sender, receiver, message);
		}
	}
	
	@OnMessage
	public void OnMessage(Session userSession, ByteBuffer bytes) {
		String message = new String(bytes.array());
		System.out.println("ByteBuffer Message received" + message);
	}
	
	@OnError
	public void onError(Session userSession, Throwable e) {
		System.out.println("Error: " + e.toString());
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		String userNameClose = null;
		Set<String> userNames = sessionsMap.keySet();
		for (String userName : userNames) {
			if (sessionsMap.get(userName).equals(userSession)) {
				userNameClose = userName;
				sessionsMap.remove(userName);
				break;
			}
		}

		if (userNameClose != null) {
			State stateMessage = new State("close", userNameClose, userNames);
			String stateMessageJson = gson.toJson(stateMessage);
			Collection<Session> sessions = sessionsMap.values();
			for (Session session : sessions) {
				session.getAsyncRemote().sendText(stateMessageJson);
			}
		}

		String text = String.format("session ID = %s, disconnected; close code = %d%nusers: %s", userSession.getId(),
				reason.getCloseCode().getCode(), userNames);
		System.out.println(text);
	}
}
