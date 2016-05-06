package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Entity class for the keywords associated with each entry.
 */
@Entity
public class Keywords extends Model {
  @Id
  private long id;
  private String keyword = "";
  private long keywordEntryId;
  private double keywordRelevance;
  @ManyToOne
  private UrlEntry urlEntry;
  
  @ManyToOne
  private NoteEntry noteEntry;

  @ManyToOne
  private FileEntry fileEntry;


  /**
   * Constructor to initialize attributes.
   * @param keyword the keywords associated with the entries.
   * @param keywordRelevance the relevance of each keyword.
   */
  public Keywords(String keyword, double keywordRelevance) {
    this.keyword = keyword;
    this.keywordRelevance = keywordRelevance;
  }

  /**
   * Adds the url entries.
   * @param entry the entry list.
   */
  public void addEntry(UrlEntry entry) {
    this.urlEntry = entry;
  }
  
  /**
   * Adds the note entries.
   * @param entry the entry list.
   */
  public void addEntry(NoteEntry entry) {
    this.noteEntry = entry;
  }

  /**
   * Adds the file entries.
   * @param entry the entry list.
   */
  public void addEntry(FileEntry entry) {
    this.fileEntry = entry;
  }

   /**
   * The EBean ORM finder method for database queries.
   * @return The finder method.
   */
  public static Model.Finder<Long, Keywords> find() {
    return new Model.Finder<Long, Keywords>(Long.class, Keywords.class);
  }

  /**
   * Gets the Id.
   * @return the Id.
   */
  public long getId() {
    return id;
  }

  /**
   * Sets the Id.
   * @param id the Id.
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Gets the url entry instance for this keyword.
   * @return the entry.
   */
  public UrlEntry getUrlEntry() {
    return urlEntry;
  }

  /**
   * Sets the url entry instance.
   * @param entry the entry instance.
   */
  public void setUrlEntry(UrlEntry entry) {
    this.urlEntry = entry;
  }

  /**
   * Gets the note entry instance for this keyword.
   * @return the entry.
   */
  public NoteEntry getNoteEntry() {
    return noteEntry;
  }

  /**
   * Sets the entry instance.
   * @param entry the entry instance.
   */
  public void setNoteEntry(NoteEntry entry) {
    this.noteEntry = entry;
  }

  /**
  * Gets the file entry instance for this keyword.
  * @return the entry.
  */
 public FileEntry getFileEntry() {
   return fileEntry;
 }

 /**
  * Sets the file entry instance.
  * @param entry the entry instance.
  */
 public void setFileEntry(FileEntry entry) {
   this.fileEntry = entry;
 }

 
  /**
   * Gets the keywords.
   * @return the keywords.
   */
  public String getKeyword() {
    return keyword;
  }

  /**
   * Sets the keywords.
   * @param keyword the keywords.
   */
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  /**
   * Gets the entry id from entry table.
   * @return the entry id for this keyword.
   */
  public long getKeywordEntryId() {
    return keywordEntryId;
  }
  /**
   * Sets the entry id for this keyword..
   * @param keywordEntryId the entry id for this keyword.
   */
  public void setKeywordEntryId(long keywordEntryId) {
    this.keywordEntryId = keywordEntryId;
  }
  /**
   * Gets the keywordRelevance for this keyword..
   * @return  keywordRelevance the keywordRelevance for this keyword.
   */
  public double getKeywordRelevance() {
    return keywordRelevance;
  }
  /**
   * Sets the keywordRelevance for this keyword..
   * @param keywordRelevance the keywordRelevance for this keyword.
   */
  public void setKeywordRelevance(int keywordRelevance) {
    this.keywordRelevance = keywordRelevance;
  }
}
