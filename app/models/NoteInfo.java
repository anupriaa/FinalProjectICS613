package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Entity class for note information.
 */

@Entity
public class NoteInfo extends Model {
  @Id
  private long noteId;
  private String note = "";
  private String noteTitle = "";
  private long noteEntryId;

  @OneToOne
  private NoteEntry entry;

  /**
   * Constructor to initialize the attributes.
   * @param note the note entered bu the user.
   */
  public NoteInfo(String note, String noteTitle) {

    this.note = note;
    this.noteTitle = noteTitle;
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
}
