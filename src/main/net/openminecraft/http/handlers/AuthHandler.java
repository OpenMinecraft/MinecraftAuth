package net.openminecraft.http.handlers;

import net.openminecraft.auth.Microsoft;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class AuthHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange e) {
        System.out.println("Webserver : Received connection to /auth");

        String params = e.getRequestURI().getQuery();

        Map<String, String> parsedParams = new HashMap<>();
        for (String param : params.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                parsedParams.put(entry[0], entry[1]);
            } else {
                parsedParams.put(entry[0], "");
            }
        }

        if (parsedParams.containsKey("code")) {
            Microsoft.login(parsedParams.get("code"));
        }

        try {


            OutputStream os = e.getResponseBody();
            StringWriter writer = new StringWriter();
            String response = "Vous pouvez fermer cet onglet.";
            writer.write(response);

            e.sendResponseHeaders(200, response.getBytes().length);

            e.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
