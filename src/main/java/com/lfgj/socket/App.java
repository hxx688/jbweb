package com.lfgj.socket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

public class App {
	public static void main(String[] args) throws InterruptedException
    {
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(9092);

        SocketIOServer server = new SocketIOServer(config);
        CharteventListener listner = new CharteventListener();
        listner. setServer(server);
        // chatevent为事件名称
        server.addEventListener("chatevent", ChatObject.class, listner);
        //启动服务
        server.start();
        Thread.sleep(Integer.MAX_VALUE) ;
        server.stop();
    }
}
