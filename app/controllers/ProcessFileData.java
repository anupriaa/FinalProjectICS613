package controllers;



import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import models.EntryDB;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import play.mvc.Controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Handles the html data from the uploaded image.
 */
public class ProcessFileData extends Controller  {

	
  /**
   * ArrayList to store associated keywords.
   */
  public static ArrayList<String> keywords = new ArrayList<String>();
  /**
   * ArrayList to store relevance of each keyword.
   */
  public static ArrayList<Double> keywordRelevance = new ArrayList<Double>();
  /**
   * Stores the type of entry - url or text or file.
   */
  public static String entryType = "";
  /**
   * Store the API key for Alchemy API.
   */
  public static String apiKey = "f97fec645fc4998b6c91d2113ad5a1d8b7189cec";

  /**
   * Extracts all the information from the image and stores it in the DB.
   * @param file the image entered by the user.
   */
  public static void processFile(File file, String fileName, String fileType) {
	  System.out.println("INSIDE PROCESSImage");
    entryType = "file";
    keywords = new ArrayList<String>();
    String name = FilenameUtils.removeExtension(fileName);
    if(name.contains("_")){
      name = replaceUnderscoreWithComma(name);
      extractKeywords(name);
    }else if(name.contains(" ")){
      name = ProcessNoteData.replaceSpaceWithComma(name);
      //to extract keywords from file name
      extractKeywords(name);
    } else {
      extractKeywords(name);
    }
    if(keywords.isEmpty()){
      System.out.println("file keyword empty");
      System.out.println(name.length());
      if(name.length() < 50){
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(name.split("\\s*,\\s*")));
        for(String str : items)
        {
          System.out.println(str);
          keywords.add(str);
          keywordRelevance.add(0.1);
        }
      }

    }
     //to extract keywords from the image.
    if(fileType.equalsIgnoreCase("image")) {
      extractKeywords(file);
      //extractTaggedFaces(file);
    }
    Collections.copy(keywords, ProcessUrlData.removeWhiteSpaces(keywords));
    EntryDB.addFileEntry(entryType, keywords, keywordRelevance, fileToByte(file), fileName, fileType, ctx());
    keywords.clear();
  }
  /**
   * Removes the leading and trailing white spaces from each keyword.
   * @param fileName the name of the file uploaded.
   * @return modified filename.
   */
  private static String replaceUnderscoreWithComma(String fileName) {
    return fileName.replace('_',',');
  }
  /**
   * Removes the leading and trailing white spaces from each keyword.
   * @param fileName the name of the file uploaded.
   * @return modified filename.
   */
  private static String replaceDotWithComma(String fileName) {
    return fileName.replace('.',',');
  }
  /**
   * Converts the file image to a byte array.
   * @param fileToConvert File uploaded
   * @return byte array with image data.
   */
  private static byte[]fileToByte(File fileToConvert){
	  byte[] image = new byte[(int) fileToConvert.length()];
	    /* write the image data into the byte array */
	    InputStream inStream = null;
	    try {
	      inStream = new BufferedInputStream(new FileInputStream(fileToConvert));
	      inStream.read(image);
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }
	    finally {
	      if (inStream != null) {
	        try {
	          inStream.close();
	        }
	        catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
	    }
	    return(image);
  }
  /**
   *
   */
  /**
   * Extracts the keywords from the name of the file uploaded.
   * Uses  an open-source library. http://unirest.io/java.
   * @param fileName the name of the file.
   */
  private static void extractKeywords(String fileName) {
    try {
      System.out.println("EXTRACT KEYWORD from filename--"+fileName);
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
          .field("text", fileName)
          .field("apikey", apiKey)
          .field("maxRetrieve", maxRetrieve)
          .field("keywordExtractMode", extractMode)
          .field("outputMode", outputMode)
          .asJson();
      JSONObject respObj = response.getBody().getObject();
      if (respObj.getString("status").equals("OK")) {
        System.out.println("EXTRACT KEYWORD from filename--ok");
        JSONArray array = respObj.getJSONArray("keywords");
        System.out.println("array.length()--"+array.length());
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
  /**
   * Extracts the keywords from the image entered.
   * Uses  an open-source library. http://unirest.io/java.
   * @param image the image uploaded by the user.
   */
  /*private static void extractTaggedFaces(File image){
    AlchemyVision service = new AlchemyVision();
    service.setApiKey(apiKey);

    ImageFaces faces = service.recognizeFaces(image, true);
    System.out.println("faces--"+faces);

    *//*ImageKeywords keywords = service.getImageKeywords(image, true, true);
    System.out.println("keywordsapi---"+keywords);*//*
  }*/
  /**
   * Extracts the keywords from the image entered.
   * Uses  an open-source library. http://unirest.io/java.
   * @param image the image uploaded by the user.
   */
  private static void extractKeywords(File image) {
    try {
      String file ="";
      try {
        StringBuilder sb = new StringBuilder();
        DataInputStream input = new DataInputStream( new FileInputStream( image ) );
        try {
          while( true ) {
            sb.append( Integer.toBinaryString( input.readByte() ) );
          }
        } catch( EOFException eof ) {
        } catch( IOException e ) {
          e.printStackTrace();
        }
        //System.out.println(sb.toString());
        file = sb.toString();
      } catch( FileNotFoundException e2 ) {
        e2.printStackTrace();
      }
      System.out.println("EXTRACT KEYWORD from image");
      String endpoint = "http://gateway-a.watsonplatform.net/calls/image/ImageGetRankedImageFaceTags";
      //String maxRetrieve = "5";
      //String extractMode = "strict";
      String contentType = "application/x-www-form-urlencoded";
      String headerAccept = "application/json";
      String outputMode = "json";
      String imagePostMode = "raw";
      HttpResponse<JsonNode> response = Unirest.post(endpoint)
          .header("Content-Type", contentType)
          .header("Accept", headerAccept)
          .field("image", file)
          .field("imagePostMode", imagePostMode)
          .field("apikey", apiKey)
          .field("outputMode", outputMode)
          .asJson();

    }
    catch (Exception e) {
      System.out.println("EXTRACT KEYWORD EXCEPTION");
      UnsupportedEncodingException en;
      e.printStackTrace();
    }
  }

}
