package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * Entity class for note information.
 */

@Entity
public class NoteInfo extends Model {
  @Id
  private long noteId;
  @Column(columnDefinition = "TEXT")
  private String note = "";

  private String noteTitle = "";
  private long noteEntryId;
  private String date = "";
  private String time ="";

  @OneToOne
  private NoteEntry entry;

  /**
   * Constructor to initialize the attributes.
   * @param note the note entered bu the user.
   * @param noteTitle the title of the note.
   * @param date the  date.
   * @param time the time of the entry.
   */
  public NoteInfo(String note, String noteTitle, String date, String time) {

    this.note = note;
    this.noteTitle = noteTitle;
    this.date = date;
    this.time = time;
  }

  /**
   * Adds the entries.
   * @param entry the notes of the user.
   */
  public void addEntry(NoteEntry entry) {
    this.entry = entry;
  }

  /**
   * The EBean ORM finder method for database queries.
   *
   * @return The finder method.
   */
  public static Finder<Long, NoteInfo> find() {
    return new Finder<Long, NoteInfo>(Long.class, NoteInfo.class);
  }

  /**
   * Gets the note id.
   * @return the note id.
   */
  public long getNoteId() {
    return noteId;
  }
  /**
   * Sets the note id.
   * @param noteId the note id.
   */
  public void setNoteId(long noteId) {
    this.noteId = noteId;
  }
  /**
   * Gets the note title .
   * @return the note title.
   */
  public String getNoteTitle() {
    return noteTitle;
  }
  /**
   * Sets the note title.
   * @param noteTitle the note title.
   */
  public void setNoteTitle(String noteTitle) {
    this.noteTitle = noteTitle;
  }
  /**
   * Gets the note .
   * @return the note.
   */
  public String getNote() {
    return note;
  }
  /**
   * Sets the note.
   * @param note the note.
   */
  public void setNote(String note) {
    this.note = note;
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
   * Gets the entry instances.
   * @return the entry instances.
   */
  public NoteEntry getEntry() {
    return entry;
  }
  /**
   * Sets the entry instances.
   * @param entry the entry instances.
   */
  public void setEntry(NoteEntry entry) {
    this.entry = entry;
  }
  /**
   * Gets the entry id for this note.
   * @return the entry id.
   */
  public long getNoteEntryId() {
    return noteEntryId;
  }
  /**
   * Gets the entry id for this note.
   * @param noteEntryId the entry id.
   */
  public void setNoteEntryId(long noteEntryId) {
    this.noteEntryId = noteEntryId;
  }
  /**
   * To get the most relevant keyword for each note.
   * @param noteEntryId The entryId for the keyword.
   * @return the most relevant keyword for the url.
   */
  public String getMostRelevantKeyword(long noteEntryId){
    String relevantKeyword = "";
    List<Keywords> keywords = Keywords.find()
        .select("keyword")
        .where()
        .eq("keywordEntryId", noteEntryId)
        .order().desc("keywordRelevance")
        .findList();
    relevantKeyword = keywords.get(0).getKeyword();
    return relevantKeyword;
  }
}
