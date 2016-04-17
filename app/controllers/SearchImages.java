package controllers;

import models.ImageEntry;
import models.ImageInfo;
import models.UrlEntry;
import models.Keywords;
import models.UrlInfo;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import play.mvc.Controller;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Searches the images related to entered keyword.
 */
public class SearchImages extends Controller {
  /**
   * Queries the database for images related to the logged in user.
   * @return the list of images.
   */
  public static List<ImageInfo> searchAllImages() {

    //getSynonyms(queryKeywords);

    ArrayList<Long> finalIdList = new ArrayList<Long>();

    String email = Secured.getUser(ctx());
    List<ImageEntry> entryIdList = ImageEntry.find()
                            .select("entryId")
                            .where()
                            .eq("email", email)
                            .findList();
    for (ImageEntry entry : entryIdList) {
      finalIdList.add(entry.getEntryId());
    }
    System.out.println("finalIdList for images---" + finalIdList);
    List<ImageInfo> imageIdList = ImageInfo.find().select("imageId").where().in("imageEntryId", finalIdList).findList();
    /*ArrayList<Byte> images = new ArrayList<Byte>();
    for (ImageInfo imageInfo : imageList) {
      images.add(imageInfo.getImage());
    }
    System.out.println("urls in search----" + images);*/
    return imageIdList;
  }
}
