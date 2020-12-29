package net.openminecraft.http;

import com.sun.net.httpserver.HttpServer;
import net.openminecraft.http.handlers.AuthHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {
    public WebServer(int port) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
            server.createContext("/auth", new AuthHandler());
            server.start();
            System.out.println("Webserver started and listening on " + server.getAddress());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
