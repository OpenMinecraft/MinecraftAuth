package net.openminecraft.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Microsoft {
    private static final String clientId = "f78fd367-7f9e-46c0-a6d1-62c88c0b6578";

    private static final String loginUrl = "https://login.live.com/oauth20_authorize.srf?client_id=f78fd367-7f9e-46c0-a6d1-62c88c0b6578&response_type=code&scope=XboxLive.signin&redirect_uri=http%3A%2F%2F127.0.0.1%3A25345%2Fauth";

    private static final String authTokenUrl = "https://login.live.com/oauth20_token.srf";

    private static final String xblAuthUrl = "https://user.auth.xboxlive.com/user/authenticate";

    private static final String xstsAuthUrl = "https://xsts.auth.xboxlive.com/xsts/authorize";

    private static final String mcLoginUrl = "https://api.minecraftservices.com/authentication/login_with_xbox";

    private static final String mcStoreUrl = "https://api.minecraftservices.com/entitlements/mcstore";

    private static final String mcProfileUrl = "https://api.minecraftservices.com/minecraft/profile";

    public static void login(String code) {
        System.out.println("Logging in with code " + code);

        JsonObject tokenObj = getAccessToken(code);
        System.out.println("Access Token : " + tokenObj);
        JsonObject xblObj = xblAuth(tokenObj.get("access_token").getAsString());
        System.out.println("XBL : " + xblObj);
    }

    private static JsonObject getAccessToken(String code) {
        try {

            String formData = "grant_type=authorization_code&redirect_uri=http%3A%2F%2F127.0.0.1%3A25345%2Fauth&client_id=" + clientId +
                    "&code=" + code;

            URL uri = new URL(authTokenUrl);
            HttpURLConnection con = (HttpURLConnection) uri.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("charset", "utf-8");
            con.setRequestProperty("Content-Length", Integer.toString(formData.getBytes(StandardCharsets.UTF_8).length));
            con.setDoInput(true);
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(formData.getBytes(StandardCharsets.UTF_8));

            BufferedReader br;
            if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String response = "";
            for (String line; (line = br.readLine()) != null; response += line);
            return new Gson().fromJson(response, JsonObject.class);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static JsonObject xblAuth(String token) {
        try {
            System.out.println("pouet");
            String payload = "{\n" +
                    "    \"Properties\": {\n" +
                    "        \"AuthMethod\": \"RPS\",\n" +
                    "        \"SiteName\": \"user.auth.xboxlive.com\",\n" +
                    "        \"RpsTicket\": \""+token+"\"\n" +
                    "    },\n" +
                    "    \"RelyingParty\": \"http://auth.xboxlive.com\",\n" +
                    "    \"TokenType\": \"JWT\"\n" +
                    " }";

            System.out.println(payload);

            URL uri = new URL(xblAuthUrl);
            HttpURLConnection con = (HttpURLConnection) uri.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("charset", "utf-8");
            con.setRequestProperty("Content-Length", Integer.toString(payload.getBytes(StandardCharsets.UTF_8).length));
            con.setDoInput(true);
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(payload.getBytes(StandardCharsets.UTF_8));

            System.out.println(con.getResponseCode());

            BufferedReader br;
            if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }



            String response = "";
            for (String line; (line = br.readLine()) != null; response += line);
            System.out.println(response);

            return new Gson().fromJson(response, JsonObject.class);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
