package controllers;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import models.EntryDB;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import play.mvc.Controller;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Handles the html data from the entered URL.
 */
public class ProcessUrlData extends Controller  {

  /**
   * ArrayList to store associated keywords.
   */
  public static ArrayList<String> keywords = new ArrayList<String>();
  /**
   * ArrayList to store relevance of each keyword.
   */
  public static ArrayList<Double> keywordRelevance = new ArrayList<Double>();
  /**
   * Stores the type of entry - url or text.
   */
  public static String entryType = "url";
  /**
   * Stores the type of url - image or non-image.
   */
  public static String urlType = "";
  /**
   * Store the API key for Alchemy API.
   */
  public static String apiKey = "f97fec645fc4998b6c91d2113ad5a1d8b7189cec";

  /**
   * Extracts all the information from URL and stores it in the DB.
   * @param url the url entered by the user.
   */
  public static void processUrl(String url) {
    //check if url points to an image.
    if (isImage(url)) {
      keywords = new ArrayList<String>();
      urlType = "image";
      extractImageInfo(url);
      Collections.copy(keywords, removeWhiteSpaces(keywords));
      EntryDB.addUrlEntry(entryType, keywords, keywordRelevance, urlType, url, ctx());
      //keywords.clear();
    }
    else {
      keywords = new ArrayList<>();
      urlType = "text";

      //call function to extract keywords from meta data from url.
      extractMetaData(url);
      //to extract info of main image n the page
      extractMainImageInfo(url);
      Collections.copy(keywords, removeWhiteSpaces(keywords));
      EntryDB.addUrlEntry(entryType, keywords, keywordRelevance, urlType, url, ctx());
      //keywords.clear();
    }
  }

  /**
   * Extracts the meta data from html.
   * @param url The url entered by the user.
   */
  private static void extractMetaData(String url) {
    boolean keywordPresent = true;
    boolean descPresent = true;
    try {
      Document doc = Jsoup.connect(url).get();

      //get meta keyword content
      System.out.print("BEFORE META KEYWO");
      try {
        String keywords = doc.select("meta[name=keywords]").first().attr("content");
        System.out.print("AFTER META KEYWO");
        ProcessUrlData.keywords = new ArrayList<String>(Arrays.asList(keywords.split(",")));
        System.out.println("length---" + ProcessUrlData.keywords.size());
        if(ProcessUrlData.keywords.size() > 0) {
        for (int i = 0; i < ProcessUrlData.keywords.size(); i++) {
                keywordRelevance.add(i, 0.1);
        }
        } else{
        	keywordPresent = false;
        }
        
      }
      catch (Exception e) {
        keywordPresent = false;
        e.printStackTrace();
        System.out.println("CATCH 111");
      }
      //get meta description content
      try {
        String description = doc.select("meta[name=description]").get(0).attr("content");
        System.out.println("description----" + description);
        if(description != null && !description.isEmpty()) {
        extractKeywords(description);
        } else{
        	descPresent = false;
        }
        
      }
      catch (Exception e) {
        System.out.print("INSIDE DESC CATCH");
        descPresent = false;
        e.printStackTrace();
      }
      if (!keywordPresent && !descPresent) {
        extractKeywordsFromUrl(url);
      }
    }
    catch (Exception e) {
      System.out.println("INSIDE META DATA CATCH");
      System.out.println("Exception : " + e);
    }

  }

  /**
   * Extracts the keywords from description of a website.
   * Uses  an open-source library. http://unirest.io/java.
   * @param description the description of the website.
   */
  private static void extractKeywords(String description) {
    try {
      System.out.println("EXTRACT KEYWORD ");
      String endpoint = "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords";
      //String endpoint = " http://gateway-a.watsonplatform.net/calls/text/TextGetRankedKeywords";
      String maxRetrieve = "5";
      String extractMode = "strict";
      String contentType = "application/x-www-form-urlencoded";
      String headerAccept = "application/json";
      String outputMode = "json";
      HttpResponse<JsonNode> response = Unirest.post(endpoint)
          .header("Content-Type", contentType)
          .header("Accept", headerAccept)
          .field("text", description)
          .field("apikey", apiKey)
          .field("maxRetrieve", maxRetrieve)
          .field("keywordExtractMode", extractMode)
          .field("outputMode", outputMode)
          .asJson();
      JSONObject respObj = response.getBody().getObject();
      if (respObj.getString("status").equals("OK")) {
        JSONArray array = respObj.getJSONArray("keywords");
        for (int i = 0; i < array.length(); i++) {
        	System.out.println("from new api---"+array.getJSONObject(i).getString("text"));
          keywords.add(array.getJSONObject(i).getString("text"));
          System.out.print("----rele---"+array.getJSONObject(i).getString("relevance"));
          keywordRelevance.add(Double.parseDouble(array.getJSONObject(i).getString("relevance")));
        }
      }
    }
    catch (Exception e) {
      System.out.println("EXTRACT KEYWORD EXCEPTION");
      UnsupportedEncodingException en;
      e.printStackTrace();
    }
  }

  /**
   * Extracts the keywords from description of a website.
   * Uses  an open-source library. http://unirest.io/java.
   * @param url the url of the website.
   */
  private static void extractKeywordsFromUrl(String url) {
    try {
      System.out.println("EXTRACT KEYWORD URL");
      String endpoint = "http://access.alchemyapi.com/calls/url/URLGetRankedKeywords";
      String maxRetrieve = "5";
      String extractMode = "strict";
      String contentType = "application/x-www-form-urlencoded";
      String headerAccept = "application/json";
      String outputMode = "json";
      HttpResponse<JsonNode> response = Unirest.post(endpoint)
          .header("Content-Type", contentType)
          .header("Accept", headerAccept)
          .field("text", url)
          .field("apikey", apiKey)
          .field("maxRetrieve", maxRetrieve)
          .field("keywordExtractMode", extractMode)
          .field("outputMode", outputMode)
          .asJson();
      JSONObject respObj = response.getBody().getObject();
      if (respObj.getString("status").equals("OK")) {
        JSONArray array = respObj.getJSONArray("keywords");
        for (int i = 0; i < array.length(); i++) {
          keywords.add(array.getJSONObject(i).getString("text"));
          keywordRelevance.add(Double.parseDouble(array.getJSONObject(i).getString("relevance")));
        }
      }
    }
    catch (Exception e) {
      System.out.println("EXTRACT KEYWORD EXCEPTION");
      UnsupportedEncodingException en;
      e.printStackTrace();
    }
  }

  /**
   * Checks if entered url points to an image.
   * @param url the url entered by the user.
   * @return true if points to an image, false otherwise.
   */
  public static boolean isImage(String url) {
    boolean isImage = false;
    try {
      URLConnection connection = new URL(url).openConnection();
      String contentType = connection.getHeaderField("Content-Type");
      //System.out.println("contentType--"+contentType);
      isImage = contentType.startsWith("image/");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return isImage;
  }

  /**
   * Extracts image information from an image url.
   * Uses Alchemy API.
   * @param url url entered by the user.
   */
  public static void extractImageInfo(String url) {
    try {
      String endpoint = "http://access.alchemyapi.com/calls/url/URLGetRankedImageKeywords";
      String contentType = "application/x-www-form-urlencoded";
      String headerAccept = "application/json";
      String outputMode = "json";
      HttpResponse<JsonNode> response = Unirest.post(endpoint)
          .header("Content-Type", contentType)
          .header("Accept", headerAccept)
          .field("url", url)
          .field("apikey", apiKey)
          .field("outputMode", outputMode)
          .asJson();
      JSONObject respObj = response.getBody().getObject();
      if (respObj.getString("status").equals("OK")) {
          JSONArray array = respObj.getJSONArray("imageKeywords");
        for (int i = 0; i < array.length(); i++) {
          keywords.add(array.getJSONObject(i).getString("text"));
          keywordRelevance.add(Double.parseDouble(array.getJSONObject(i).getString("score")));
        }
      }
    }
    catch (Exception e) {
      UnsupportedEncodingException en;
      e.printStackTrace();
    }
  }

  /**
   * Extracts the information from the main image of non-image url.
   * @param url the url entered by user.
   */
  public static void extractMainImageInfo(String url) {
    try {
      String imageUrl = "";
      String endpoint = "http://access.alchemyapi.com/calls/url/URLGetImage";
      String contentType = "application/x-www-form-urlencoded";
      String headerAccept = "application/json";
      String outputMode = "json";
      HttpResponse<JsonNode> response = Unirest.post(endpoint)
          .header("Content-Type", contentType)
          .header("Accept", headerAccept)
          .field("url", url)
          .field("apikey", apiKey)
          .field("outputMode", outputMode)
          .asJson();
      JSONObject respObj = response.getBody().getObject();
      imageUrl = respObj.getString("image");
      if (!imageUrl.equals("")  || imageUrl == null) {
        extractImageInfo(imageUrl);
      }
    }
    catch (Exception e) {
      UnsupportedEncodingException en;
      e.printStackTrace();
    }
  }

  /**
   * Removes the leading and trailing white spaces from each keyword.
   * @param keywords the keywords extracted.
   * @return the trimmed keywords.
   */
  public static ArrayList<String> removeWhiteSpaces(ArrayList<String> keywords) {
    ArrayList<String> keywordList = new ArrayList<String>();
    for (String keyword: keywords) {
      keywordList.add(keyword.trim().toLowerCase());
    }
    return keywordList;
  }
}
