package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

/**
 * Entity class for uploaded file information.
 */

@Entity
public class FileInfo extends Model {
  @Id
    private long fileId;
  @Lob
  private byte[] file;
  private String fileName = "";
  private String fileType = "";
  private String date = "";
  private String time ="";
  private long fileEntryId;

  @OneToOne
  private FileEntry entry;

  /**
   * Constructor to initialize the attributes.
   * @param file the file uploaded by the user.
   * @param fileName the name of the uploaded file.
   * @param fileType the extension of the uploaded file.
   * @param date the  date.
   * @param time the time of the entry.
   */
  public FileInfo(byte[] file, String fileName, String fileType, String date, String time) {
    this.file = file;
    this.fileName = fileName;
    this.fileType = fileType;
    this.date = date;
    this.time = time;
  }

  /**
   * Adds the entries.
   * @param entry the uploaded file of the user.
   */
  public void addEntry(FileEntry entry) {
    this.entry = entry;
  }

  /**
   * The EBean ORM finder method for database queries.
   *
   * @return The finder method.
   */
  public static Finder<Long, FileInfo> find() {
    return new Finder<Long, FileInfo>(Long.class, FileInfo.class);
  }

  /**
   * Gets the file id.
   * @return the file id.
   */
   public long getFileId() {
   return fileId;
   }

   /* Sets the file id.
   * @param fileId the file id.
   */
  public void setFileId(long fileId) {
    this.fileId = fileId;
  }
  /**
   * Gets the file .
   * @return the file.
   */
  public byte[] getFile() {
	    return file;
  }
  /**
   * Sets the file.
   * @param file the file.
   */
  public void setFile(byte[] file) {
	 this.file = file;
  }

  /**
   * Gets the file name .
   * @return the file name.
   */
  public String getFileName() {
	    return fileName;
  }
  /**
   * Sets the file name.
   * @param fileName the file name.
   */
  public void setFileName(String fileName) {
	 this.fileName = fileName;
  }
  /**
   * Gets the file type .
   * @return the file type.
   */
  public String getFileType() {
    return fileType;
  }
  /**
   * Sets the file name.
   * @param fileType the file name.
   */
  public void setFileType(String fileType) {
    this.fileType = fileType;
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
  public FileEntry getEntry() {
    return entry;
  }
  /**
   * Sets the entry instances.
   * @param entry the entry instances.
   */
  public void setEntry(FileEntry entry) {
    this.entry = entry;
  }
  /**
   * Gets the entry id for this file.
   * @return the entry id.
   */
  public long getFileEntryId() {
    return fileEntryId;
  }
  /**
   * Gets the entry id for this file.
   * @param fileEntryId the entry id.
   */
  public void setFileEntryId(long fileEntryId) {
    this.fileEntryId = fileEntryId;
  }
}
