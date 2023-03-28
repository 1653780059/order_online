package com.example.order_online.socket;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.order_online.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 16537
 * @Classname WebSocketService
 * @Description
 * @Version 1.0.0
 * @Date 2022/11/18 9:16
 */
@Component
@ServerEndpoint("/socket/{username}")
@Slf4j
public class WebSocketService {
    public static ConcurrentHashMap<String, Session> sessionMap=new ConcurrentHashMap<>();


    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){
        log.info("用户已连接"+username);
        session.getUserProperties().put("username",username);
        sessionMap.put(username,session);
    }
    @OnClose
    public void onClose(Session session){
        final String username =(String) session.getUserProperties().get("username");
        sessionMap.remove(username);
    }
    @OnMessage
    public void onMessage(String message,Session session){

    }
    @OnError
    public void onError(Session session,Throwable error){
        log.error("发生错误"+error.getMessage());
    }

    public static void sendInfo(String message,String username){
        if(StrUtil.isNotBlank(username) &&sessionMap.containsKey(username)){
            System.out.println("websocket"+message);
            Session session = sessionMap.get(username);
            sendMessage(message,session);
        }
    }

    private static void sendMessage(String message,Session session){
        try{
            session.getBasicRemote().sendText(message);
        }catch (Exception e){
            log.error("执行异常",e);
        }
    }
}
