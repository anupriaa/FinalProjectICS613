package models;

import controllers.Secured;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class for url information.
 */

@Entity
public class UrlInfo extends Model {
  @Id
  private long urlId;
  private String urlType = "";
  private String url = "";
  private String urlSnippet = "";
  private String date = "";
  private String time ="";
  private boolean ogImagePresent = false;
  private String urlOGImage = "";
  private long urlEntryId;

  @OneToOne
  private UrlEntry entry;

  /**
   * Constructor to initialize the attributes.
   * @param urlType the type of url.
   * @param url the url.
   * @param urlSnippet the snippet for the url.
   * @param date the  date.
   * @param time the time of the entry.
   * @param ogImagePresent if og image is present or not for the image.
   * @param urlOGImage the url of thr og image.
   */
  public UrlInfo(String urlType, String url, String urlSnippet, String date, String time, boolean ogImagePresent, String urlOGImage) {
    this.urlType = urlType;
    this.url = url;
    this.urlSnippet = urlSnippet;
    this.date = date;
    this.time = time;
    this.ogImagePresent = ogImagePresent;
    this.urlOGImage = urlOGImage;
  }

  /**
   * Adds the entries.
   * @param entry the contact list.
   */
  public void addEntry(UrlEntry entry) {
    this.entry = entry;
  }

  /**
   * The EBean ORM finder method for database queries.
   *
   * @return The finder method.
   */
  public static Finder<Long, UrlInfo> find() {
    return new Finder<Long, UrlInfo>(Long.class, UrlInfo.class);
  }

  /**
   * Gets the url id.
   * @return the url id.
   */
  public long getUrlId() {
    return urlId;
  }
  /**
   * Sets the url id.
   * @param urlId the url id.
   */
  public void setUrlId(long urlId) {
    this.urlId = urlId;
  }
  /**
   * Gets the url type.
   * @return the url Type.
   */
  public String getUrlType() {
    return urlType;
  }
  /**
   * Sets the url type.
   * @param urlType the url type.
   */
  public void setUrlType(String urlType) {
    this.urlType = urlType;
  }
  /**
   * Gets the url .
   * @return the url.
   */
  public String getUrl() {
    return url;
  }
  /**
   * Sets the url.
   * @param url the url.
   */
  public void setUrl(String url) {
    this.url = url;
  }
  /**
   * Gets the url Snippet.
   * @return the urlSnippet.
   */
  public String getUrlSnippet() {
    return urlSnippet;
  }
  /**
   * Sets the urlSnippet.
   * @param urlSnippet the urlSnippet.
   */
  public void setUrlSnippet(String urlSnippet) {
    this.urlSnippet = urlSnippet;
  }
  /**
   * Gets the date.
   * @return the date.
   */
  public String getDate() {
    return date;
  }
  /**
   * Sets the date.
   * @param date the date.
   */
  public void setDate(String date) {
    this.date = date;
  }
  /**
   * Gets the Time .
   * @return the Time .
   */
  public String getTime() {
    return time;
  }
  /**
   * Sets the Time .
   * @param time the Time .
   */
  public void setTime(String time) {
    this.time = time;
  }
  /**
   * Gets the url of the OG image .
   * @return the url of the OG image .
   */
  public String getUrlOGImage() {
    return urlOGImage;
  }
  /**
   * Sets the url of the OG image .
   * @param urlOGImage the url of the OG image .
   */
  public void setUrlOGImage(String urlOGImage) {
    this.urlOGImage = urlOGImage;
  }
  /**
   * If og image is present for the url or not .
   * @return the true if OG image is present.
   */
  public boolean GetIsOGImage() {
    return ogImagePresent;
  }
  /**
   * Sets the uogImagePresent.
   * @param ogImagePresent the url of the OG image .
   */
  public void setIsOGImage(boolean ogImagePresent) {
    this.ogImagePresent = ogImagePresent;
  }
  /**
   * Gets the entry instances.
   * @return the entry instances.
   */
  public UrlEntry getEntry() {
    return entry;
  }
  /**
   * Sets the entry instances.
   * @param entry the entry instances.
   */
  public void setEntry(UrlEntry entry) {
    this.entry = entry;
  }
  /**
   * Gets the entry id for this url.
   * @return the entry id.
   */
  public long getUrlEntryId() {
    return urlEntryId;
  }
  /**
   * Gets the entry id for this url.
   * @param urlEntryId the entry id.
   */
  public void setUrlEntryId(long urlEntryId) {
    this.urlEntryId = urlEntryId;
  }
  /**
   * To get the most relevant keyword for showing in the links.
   * @param urlEntryId The entryId for the keyword.
   * @return the most relevant keyword for the url.
   */
  public String getMostRelevantKeyword(long urlEntryId){
    String relevantKeyword = "";
    try{
      List<Keywords> keywords = Keywords.find()
          .select("keyword")
          .where()
          .eq("keywordEntryId", urlEntryId)
          .order().desc("keywordRelevance")
          .findList();
      relevantKeyword = keywords.get(0).getKeyword();
    }catch(Exception e){
      e.printStackTrace();
      relevantKeyword = " ";
    }
    return relevantKeyword;
  }
}
