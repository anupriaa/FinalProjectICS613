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
public class ProcessNoteData extends Controller  {

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
  public static String entryType = "text";
  /**
   * Store the API key for Alchemy API.
   */
  public static String apiKey = "f97fec645fc4998b6c91d2113ad5a1d8b7189cec";

  /**
   * Extracts all the information from the note and stores it in the DB.
   * @param note the note entered by the user.
   */
  public static void processNote(String note, String noteTitle) {
	  System.out.println("INSIDE PROCESSNOTE");
      keywords = new ArrayList<>();
      entryType = "text";
      //to extract keywords from the note.
      extractKeywords(replaceSpaceWithComma(note));
      Collections.copy(keywords, ProcessUrlData.removeWhiteSpaces(keywords));
      EntryDB.addNoteEntry(entryType, keywords, keywordRelevance,note, noteTitle, ctx());
      keywords.clear();
    }

  /**
   * Removes the leading and trailing white spaces from each keyword.
   * @param note the note entered.
   * @return modified note.
   */
  public static String replaceSpaceWithComma(String note) {
    System.out.println("note before----"+note);
    System.out.println("note after---"+note.replace(' ', ','));
    return note.replace(' ',',');
  }
  
  /**
   * Extracts the keywords from the note entered.
   * Uses  an open-source library. http://unirest.io/java.
   * @param note the note entered by the user.
   */
  private static void extractKeywords(String note) {
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
          .field("text", note)
          .field("apikey", apiKey)
          .field("maxRetrieve", maxRetrieve)
          .field("keywordExtractMode", extractMode)
          .field("outputMode", outputMode)
          .asJson();
      JSONObject respObj = response.getBody().getObject();
      if (respObj.getString("status").equals("OK")) {
        JSONArray array = respObj.getJSONArray("keywords");
        for (int i = 0; i < array.length(); i++) {
        	System.out.println("note keywords returned"+array.getJSONObject(i).getString("text"));
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

}
