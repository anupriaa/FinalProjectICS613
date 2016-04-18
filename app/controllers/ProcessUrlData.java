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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import java.io.*;
import org.apache.commons.lang.StringEscapeUtils;
import com.google.gson.Gson;

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
   * Stores the snippet of the url.
   */
  public static String urlSnippet = null;
  /**
   * Stores the og image url of the url.
   */
  public static String urlOGImage = null;
  /**
   * Is true if there is an OG image of the url.
   */
  public static boolean ogImagePresent = false;
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
      urlSnippet = "Url of an Image.";
      ogImagePresent = true;
      urlOGImage = url;
      extractImageInfo(url);
      Collections.copy(keywords, removeWhiteSpaces(keywords));
      EntryDB.addUrlEntry(entryType, keywords, keywordRelevance, urlType, url,urlSnippet,ogImagePresent, urlOGImage, ctx());
      //keywords.clear();
    }
    else {
      keywords = new ArrayList<String>();
      urlType = "text";
      urlSnippet = extractSnippet(url);
      //call function to extract keywords from meta data from url.
      extractMetaData(url);
      System.out.println("urlOGImage----" + urlOGImage);
      //to extract info of main image n the page
      extractMainImageInfo(url);
      Collections.copy(keywords, removeWhiteSpaces(keywords));
      EntryDB.addUrlEntry(entryType, keywords, keywordRelevance, urlType, url, urlSnippet, ogImagePresent, urlOGImage, ctx());
      //keywords.clear();
    }
  }

  /**
   * Gets the snippet of the url.
   * @param url The url entered by the user.
   */
  private static String extractSnippet(String url) {
    System.out.println("Inside snippet");
    String snippet =null;
    String query = "http://www.google.com/search?q=info%3A"+url+"&c2coff=0&client=google-csbe&output=xml_no_dtd&cx=000227575832440845158:ypu8drl3ars&alt=json";
    try{
        URL url1 = new URL(query);
        HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String xml = org.apache.commons.io.IOUtils.toString(br);
        System.out.println("message----"+xml);
        snippet = parseXMLString(xml);
       conn.disconnect();
      }catch(Exception e){
        e.printStackTrace();
      }
    return snippet;
  }

  /**
   * Parses the xml string returned by google api.
   * @param xml the xml to be parsed.
   * @throws IOException
   */
  public static String parseXMLString(String xml){
    String snippet = null;
    try {
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      InputSource is = new InputSource();
      is.setCharacterStream(new StringReader(xml));

      org.w3c.dom.Document doc = db.parse(is);
      snippet = doc.getElementsByTagName("S").item(0).getTextContent();
      //System.out.println("snippet--"+StringEscapeUtils.unescapeHtml(snippet));
      System.out.println(Jsoup.parse(snippet).text());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return (Jsoup.parse(snippet).text());
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
        System.out.print("content--"+keywords);
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
      //get meta og image
      try {
        String ogImageUrl = doc.select("meta[property=og:image]").first().attr("content");
        if(ogImageUrl != null && !ogImageUrl.isEmpty()) {
           ogImagePresent = true;
          urlOGImage = ogImageUrl;
        } else{
          ogImagePresent = false;
          urlOGImage = null;
        }
      }
      catch (Exception e) {
        System.out.print("INSIDE OG image CATCH");
        ogImagePresent = false;
        urlOGImage = null;
        e.printStackTrace();
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
