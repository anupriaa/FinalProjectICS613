package controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Thesaurus class to get synonyms.
 * Not using as of now.
 */
public class Thesaurus {
  /**
   * Main function.
   * @param args the arguments.
   */
  public static void main(String[] args) {
// NOTE: replace test_only with your own key
    new SendRequest("happy", "en_US", "78kRR6u1f8NfRAxzPCSq", "json");
  }
} // end of Thesaurus

/**
 * Sends the request to the api.
 */
class SendRequest {
  ArrayList<String> synonyms = new ArrayList<String>();
  final String endpoint = "http://thesaurus.altervista.org/thesaurus/v1";

  /**
   * Sends request to the api.
   * @param word the word whose synonyms will be fetched.
   * @param language the language, english in this case.
   * @param key the key.
   * @param output the output format.
   */
  public SendRequest(String word, String language, String key, String output) {
      try {
        URL serverAddress = new URL(endpoint + "?word=" + URLEncoder.encode(word, "UTF-8")
            + "&language=" + language + "&key=" + key + "&output=" + output);
        HttpURLConnection connection = (HttpURLConnection) serverAddress.openConnection();
        connection.connect();
        int rc = connection.getResponseCode();
      if (rc == 200) {
        String line = null;
        BufferedReader br = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
          sb.append(line + '\n');
        }
        JSONObject obj = (JSONObject) JSONValue.parse(sb.toString());
        JSONArray array = (JSONArray) obj.get("response");
        JSONObject list = new JSONObject();
        for (int i = 0; i < array.size(); i++) {
          list = (JSONObject) ((JSONObject) array.get(i)).get("list");
          //System.out.println(list.get("category") + ":" + list.get("synonyms"));
          System.out.println("list--" + list.toJSONString());
          synonyms.add(list.get("synonyms").toString());
        }
        for (String syn: synonyms) {
          System.out.println("sy---" + synonyms);
        }
      }
      else {
        System.out.println("HTTP error:" + rc);
      }
      connection.disconnect();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
} // end of SendRequest