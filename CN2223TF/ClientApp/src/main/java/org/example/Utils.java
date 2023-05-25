package org.example;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
    private static final String cFunctionURL = "https://europe-west1-cn2223-t1-g10.cloudfunctions.net/getRunningVms";
    public static String[] getRunningVMsIps() throws MalformedURLException {
        URL cFunction = new URL(cFunctionURL);
        StringBuffer response = new StringBuffer();
        try {
            HttpURLConnection con = (HttpURLConnection) cFunction.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if(status == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson parser = new Gson();
        String[] ips = parser.fromJson(response.toString(), String[].class);
        for (String ip : ips) {
            System.out.println(ip);
        }
        return ips;
    }

}
