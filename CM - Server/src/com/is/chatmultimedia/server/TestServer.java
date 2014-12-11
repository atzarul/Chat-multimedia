package com.is.chatmultimedia.server;

public class TestServer {

	public static void main(String[] args) {
		Server server = Server.getInstance();
		System.out.println("Server started. Listening for connections...");
		server.start();

		while (true)
			;
	}

}
