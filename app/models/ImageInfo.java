package models;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

/**
 * Entity class for uploaded image information.
 */

@Entity
public class ImageInfo extends Model {
  @Id
  private long imageId;
  @Lob
  private byte[] image;
  private String imageName = "";
  private long imageEntryId;

  @OneToOne
  private ImageEntry entry;

  /**
   * Constructor to initialize the attributes.
   * @param image the image uploaded by the user.
   */
  public ImageInfo(byte[] image, String imageName) {
    this.image = image;
    this.imageName = imageName;
  }

  /**
   * Adds the entries.
   * @param entry the uploaded images of the user.
   */
  public void addEntry(ImageEntry entry) {
    this.entry = entry;
  }

  /**
   * The EBean ORM finder method for database queries.
   *
   * @return The finder method.
   */
  public static Finder<Long, ImageInfo> find() {
    return new Finder<Long, ImageInfo>(Long.class, ImageInfo.class);
  }

  /**
   * Gets the image id.
   * @return the image id.
   */
  public long getImageId() {
    return imageId;
  }
  /**
   * Sets the image id.
   * @param imageId the image id.
   */
  public void setImageId(long imageId) {
    this.imageId = imageId;
  }
  /**
   * Gets the image .
   * @return the image.
   */
  public byte[] getImage() {
	    return image;
  }
  /**
   * Sets the image.
   * @param image the image.
   */
  public void setImage(byte[] image) {
	 this.image = image;   
  }

  /**
   * Gets the image name .
   * @return the image name.
   */
  public String getImageName() {
	    return imageName;
  }
  /**
   * Sets the image name.
   * @param imageName the image name.
   */
  public void setImageName(String imageName) {
	 this.imageName = imageName;   
  }

  /**
   * Gets the entry instances.
   * @return the entry instances.
   */
  public ImageEntry getEntry() {
    return entry;
  }
  /**
   * Sets the entry instances.
   * @param entry the entry instances.
   */
  public void setEntry(ImageEntry entry) {
    this.entry = entry;
  }
  /**
   * Gets the entry id for this image.
   * @return the entry id.
   */
  public long getImageEntryId() {
    return imageEntryId;
  }
  /**
   * Gets the entry id for this image.
   * @param imageEntryId the entry id.
   */
  public void setImageEntryId(long imageEntryId) {
    this.imageEntryId = imageEntryId;
  }
}
