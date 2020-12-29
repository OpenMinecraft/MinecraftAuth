package net.openminecraft;

import net.openminecraft.http.WebServer;

public class Main {
    public static void main(String[] args) {
        new Thread(() -> {
            WebServer server = new WebServer(25345);
        }).start();
    }
}
