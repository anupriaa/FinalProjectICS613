package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Entity class for url information.
 */

@Entity
public class UrlInfo extends Model {
  @Id
  private long urlId;
  private String urlType = "";
  private String url = "";
  private long urlEntryId;

  @OneToOne
  private UrlEntry entry;

  /**
   * Constructor to initialize the attributes.
   * @param urlType the type of url.
   * @param url the url.
   */
  public UrlInfo(String urlType, String url) {
    this.urlType = urlType;
    this.url = url;
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
   * Gets the url.
   * @param url the url.
   */
  public void setUrl(String url) {
    this.url = url;
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
}
