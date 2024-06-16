package eu.xap3y.playerservercore.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;

public class Hastebin {
   public String postBackup(String text, boolean raw) throws IOException {
      byte[] postData = text.getBytes(StandardCharsets.UTF_8);
      int postDataLength = postData.length;
      String requestURL = "https://paste.md-5.net/documents";
      URL url = new URL(requestURL);
      HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
      conn.setDoOutput(true);
      conn.setInstanceFollowRedirects(false);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("User-Agent", "Hastebin Java Api");
      conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
      conn.setUseCaches(false);
      String response = null;

      try {
         DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
         wr.write(postData);
         BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         response = reader.readLine();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

      if (response.contains("\"key\"")) {
         response = response.substring(response.indexOf(":") + 2, response.length() - 2);
         String postURL = raw ? "https://paste.md-5.net/raw/" : "https://paste.md-5.net/";
         response = postURL + response;
      }

      return response;
   }

   public String post(String text, boolean raw) throws IOException {
      byte[] postData = text.getBytes(StandardCharsets.UTF_8);
      int postDataLength = postData.length;
      String requestURL = "https://hastebin.com/documents";
      URL url = new URL(requestURL);
      HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
      conn.setDoOutput(true);
      conn.setInstanceFollowRedirects(false);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("User-Agent", "Hastebin Java Api");
      conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
      conn.setUseCaches(false);
      String response = null;

      try {
         DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
         wr.write(postData);
         BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         response = reader.readLine();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

       assert response != null;
       if (response.contains("\"key\"")) {
         response = response.substring(response.indexOf(":") + 2, response.length() - 2);
         String postURL = raw ? "https://hastebin.com/raw/" : "https://hastebin.com/";
         response = postURL + response;
      }

      return response;
   }

   public String getPaste(boolean useBackup, String ID) {
      String URLString = (useBackup ? "https://paste.md-5.net/raw/" : "https://hastebin.com/raw/") + ID + "/";
      StringBuilder output = new StringBuilder();

      try {
         URL URL = new URL(URLString);
         Scanner sc = new Scanner(URL.openStream());

         while (sc.hasNextLine()) {
            output.append(sc.nextLine()).append("\n");
         }

         return output.toString();
      } catch (IOException var7) {
         return null;
      }
   }
}
