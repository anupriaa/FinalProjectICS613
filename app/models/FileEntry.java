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
public class FileEntry extends Model {
  //private fields for all attributes.
  @Id
  @Column(name = "entry_id", nullable = false, unique = true)
  private long entryId;
  //Entry type can be either url, text, audio, video, document or image.
  private String entryType = "";
  //timestamp format is yyyyMMdd_HHmmss.
  private String timestamp = "";
  //email of the user.
  private String email = "";

  @OneToOne(mappedBy = "entry", cascade = CascadeType.PERSIST)
  private FileInfo fileInfo;

  @OneToMany(mappedBy = "entry", cascade = CascadeType.PERSIST)
  private ArrayList<Keywords> keywords;

  @ManyToOne(cascade = CascadeType.PERSIST)
  private UserInfo userInfo;

  /**
   * Constructor to initialize the entry.
   * @param entryType the type of entry.
   * @param keywords List of Keyword instances.
   * @param fileInfo  Instance of entity FileInfo.
   * @param userInfo  Instance of entity UserInfo.
   */
  public FileEntry(String entryType, ArrayList<Keywords> keywords, FileInfo fileInfo, UserInfo userInfo) {
    this.entryType = entryType;
    this.keywords = keywords;
    this.fileInfo = fileInfo;
    this.userInfo = userInfo;
  }

  /**
   * The EBean ORM finder method for database queries.
   * @return The finder method.
   */
  public static Finder<Long, FileEntry> find() {
    return new Finder<Long, FileEntry>(Long.class, FileEntry.class);
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
   * Gets the fileInfo instances.
   * @return the fileInfo.
   */
  public FileInfo getFileInfo() {
    return fileInfo;
  }
  /**
   * Sets the fileInfo instances.
   * @param fileInfo the fileInfo.
   */
  public void setFileInfo(FileInfo fileInfo) {
    this.fileInfo = fileInfo;
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
