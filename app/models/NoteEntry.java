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
public class NoteEntry extends Model {
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
  private NoteInfo noteInfo;

  @OneToMany(mappedBy = "entry", cascade = CascadeType.PERSIST)
  private ArrayList<Keywords> keywords;

  @ManyToOne(cascade = CascadeType.PERSIST)
  private UserInfo userInfo;

  /**
   * Constructor to initialize the entry.
   * @param entryType the type of entry.
   * @param keywords List of Keyword instances.
   * @param noteInfo  Instance of entity NoteInfo.
   * @param userInfo  Instance of entity userInfo.
   */
  public NoteEntry(String entryType, ArrayList<Keywords> keywords, NoteInfo noteInfo, UserInfo userInfo) {
    this.entryType = entryType;
    this.keywords = keywords;
    this.noteInfo = noteInfo;
    this.userInfo = userInfo;
  }

  /**
   * The EBean ORM finder method for database queries.
   * @return The finder method.
   */
  public static Finder<Long, NoteEntry> find() {
    return new Finder<Long, NoteEntry>(Long.class, NoteEntry.class);
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
   * Gets the noteInfo instances.
   * @return the noteInfo.
   */
  public NoteInfo getNoteInfo() {
    return noteInfo;
  }
  /**
   * Sets the noteInfo instances.
   * @param noteInfo the noteInfo.
   */
  public void setNoteInfo(NoteInfo noteInfo) {
    this.noteInfo = noteInfo;
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
