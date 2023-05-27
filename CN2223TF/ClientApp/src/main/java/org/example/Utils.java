package org.example;

import Contract.Image;
import Contract.ImageId;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;

import javax.imageio.ImageIO;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public static byte[] imageToByteArray(Path p) throws IOException {
        return Files.readAllBytes(p);
    }

}
