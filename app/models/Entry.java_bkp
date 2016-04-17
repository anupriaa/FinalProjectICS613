package models;


import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;

/**
 * Entity class for each entry.
 */
@Entity
public class Entry extends Model {
  //private fields for all attributes.
  @Id
  @Column(name = "entry_id", nullable = false, unique = true)
  private long entryId;
  //Entry type can be either url, text or image.
  private String entryType = "";
  //timestamp format is yyyyMMdd_HHmmss.
  private String timestamp = "";
  //email of the user.
  private String email = "";

  @OneToOne(mappedBy = "entry", cascade = CascadeType.PERSIST)
  private UrlInfo urlInfo;

  @OneToMany(mappedBy = "entry", cascade = CascadeType.PERSIST)
  private ArrayList<Keywords> keywords;

  @ManyToOne(cascade = CascadeType.PERSIST)
  private UserInfo userInfo;

  /**
   * Constructor to initialize the entry.
   * @param entryType the type of entry.
   * @param timestamp the time and date.
   * @param keywords List of Keyword instances.
   * @param urlInfo  Instance of entity UrlInfo.
   * @param userInfo  Instance of entity userInfo.
   */
  public Entry(String entryType, String timestamp, ArrayList<Keywords> keywords, UrlInfo urlInfo, UserInfo userInfo) {
    this.entryType = entryType;
    this.timestamp = timestamp;
    this.keywords = keywords;
    this.urlInfo = urlInfo;
    this.userInfo = userInfo;
  }

  /**
   * The EBean ORM finder method for database queries.
   * @return The finder method.
   */
  public static Finder<Long, Entry> find() {
    return new Finder<Long, Entry>(Long.class, Entry.class);
  }

  /**
   * Gets the Entry Id.
   * @return the Entry id.
   */
  public long getEntryId() {
    return entryId;
  }

  /**
   * Sets the Entry Id.
   * @param entryId the Entry Id.
   */
  public void setEntryId(long entryId) {
    this.entryId = entryId;
  }
  /**
   * Gets the Entry Type.
   * @return the Entry Type.
   */
  public String getEntryType() {
    return entryType;
  }
  /**
   * Sets the Entry Type.
   * @param entryType the Entry Type.
   */
  public void setEntryType(String entryType) {
    this.entryType = entryType;
  }
  /**
   * Gets the Time stamp.
   * @return the Time stamp.
   */
  public String getTimestamp() {
    return timestamp;
  }
  /**
   * Sets the Time stamp.
   * @param timestamp the Time stamp.
   */
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
  /**
   * Gets the urlInfo instances.
   * @return the urlInfo.
   */
  public UrlInfo getUrlInfo() {
    return urlInfo;
  }
  /**
   * Sets the urlInfo instances.
   * @param urlInfo the urlInfo.
   */
  public void setUrlInfo(UrlInfo urlInfo) {
    this.urlInfo = urlInfo;
  }
  /**
   * Gets the keyword instance list.
   * @return the keywords list.
   */
  public ArrayList<Keywords> getKeywords() {
    return keywords;
  }
  /**
   * Sets the list of keywords..
   * @param keywords the list of keywords.
   */
  public void setKeywords(ArrayList<Keywords> keywords) {
    this.keywords = keywords;
  }

  /**
   * Gets the user Information.
   * @return the instance of userInfo.
   */
  public UserInfo getUserInfo() {
    return userInfo;
  }

  /**
   * Sets the UserInfo.
   * @param userInfo The instance of UserInfo.
   */
  public void setUserInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
  }

  /**
   * Gets the email of the user.
   * @return the email of the user.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email of the user.
   * @param email the email of the user.
   */
  public void setEmail(String email) {
    this.email = email;
  }
}
