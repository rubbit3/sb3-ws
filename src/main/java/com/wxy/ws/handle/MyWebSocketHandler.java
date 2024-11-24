package com.wxy.ws.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wxy.ws.MyMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

     private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 解析器

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获取客户端发送的 JSON 消息
        String jsonMessage = message.getPayload();
        System.out.println("Received JSON message: " + jsonMessage);

        // 将 JSON 转换为 Java 对象
        try {
            MyMessage myMessage = objectMapper.readValue(jsonMessage, MyMessage.class);
            System.out.println("Parsed message: " + myMessage);

            // 构建响应消息
            MyMessage response = new MyMessage();
            response.setType("response");
            response.setContent("Hello, " + myMessage.getContent());

            // 发送响应消息
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        } catch (Exception e) {
            System.err.println("Error parsing JSON message: " + e.getMessage());
            session.sendMessage(new TextMessage("Invalid JSON format!"));
        }
    }
}
