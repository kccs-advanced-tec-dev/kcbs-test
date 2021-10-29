/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.jaxrs;

import java.io.Serializable;
import java.util.ArrayList;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/10/20<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCCS M.Takahashi<br>
 * 変更理由	新規作成<br>
 * ===============================================================================<br>
 */
/**
 * websocketのサーバー側
 */
@ServerEndpoint(value = "/ws")
public class WebSocketEndPoint implements Serializable {

    /**
     * Message receiver method
     *
     * @param message
     * @return
     */
    static ArrayList<Session> sessions = new ArrayList<>();

    @OnMessage
    public void messageReceiver(String message, Session session) {
        for (Session s : sessions) {
            if (s == session) {
                s.getUserProperties().put("goki", message);
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable th, Session session) {
        sessions.remove(session);
        System.out.println("An error occurred in websocket：" + th.getMessage());
    }

    public static ArrayList<Session> getSessions() {
        return sessions;
    }

    public static void setSessions(ArrayList<Session> sessions) {
        WebSocketEndPoint.sessions = sessions;
    }
}
