package com.lfgj.common.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.rrgy.core.toolbox.kit.DateKit;

public class MyPushServer {
	ArrayList<Socket> sockets = new ArrayList<Socket>();

	public static void main(String[] args) {

		MyPushServer myPushServer = new MyPushServer();
		try {
			myPushServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() throws IOException {
		ServerSocket serverSocket = new ServerSocket(9080);
		System.out.println("服务启动成功");
		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println("server started... http:/" + socket.getLocalAddress() + ":" + socket.getPort());
			System.out.println(socket.getLocalSocketAddress() + ":" + socket.getLocalPort());
			sockets.add(socket);
			synchronized (ServerSocket.class) {
				ServerThread serverThread = new ServerThread();
				serverThread.start();
			}
		}
	}

	class ServerThread extends Thread {
		private BufferedWriter writer;

		@Override
		public void run() {
			while (true) {
				try {
					for (Socket socket : sockets) {
						try{
							writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
							writer.write(DateKit.getTime());
							writer.newLine();
							writer.flush();
							Thread.sleep(1000);
						}catch (Exception e) {
							e.getStackTrace();
							socket.close();
							sockets.remove(socket);
						}
					}
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		}
	}
}
