package models;

import org.mindrot.jbcrypt.BCrypt;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class for users.
 */
@Entity
public class UserInfo extends Model {
    @Id
    private long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    /** The image data. */
    @Lob
    private byte[] image;
    @OneToMany(mappedBy = "userInfo")
    private List<UrlEntry> entries = new ArrayList<>();
    @OneToMany(mappedBy = "userInfo")
    private List<NoteEntry> noteEntries = new ArrayList<>();

  /**
   *
   */
     public UserInfo() {

     }
    /**
     * Create a new UserInfo object.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     */
    public UserInfo(String email, String password, String firstName, String lastName) {

      this.email = email;
      this.password = password;
      this.firstName = firstName;
      this.lastName = lastName;
    }

    /**
     * The EBean ORM finder method for database queries on Entries.
     *
     * @return The finder method for Contacts.
     */
    public static Finder<Long, UserInfo> find() {
      return new Finder<Long, UserInfo>(Long.class, UserInfo.class);
    }

    /**
     * Set the ID.
     *
     * @param id The ID.
     */
    public void setId(long id) {
      this.id = id;
    }

    /**
     * Set the email.
     *
     * @param email The email address.
     */
    public void setEmail(String email) {
      this.email = email;
    }

    /**
     * Set the password, encrypted.
     *
     * @param password The password.
     */
    public void setPassword(String password) {
      this.password = BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Set the associated Contact.
     * @param entry The entry.
     */
    public void setEntries(List<UrlEntry> entry) {
      this.entries = entry;
    }
   
    /**
     * Set the associated Contact.
     * @param entry The entry.
     */
    public void setNoteEntries(List<NoteEntry> entry) {
      this.noteEntries = entry;
    }

    /**
     * Returns the id value to the caller.
     * @return id long value.
     */
    public long getId() {
      return id;
    }

    /**
     * Get the email address.
     *
     * @return String email address.
     */
    public String getEmail() {
      return email;
    }

    /**
     * Get the encrypted password.
     * NOTE:  We do NOT return a decrypted password for security reasons.
     *
     * @return The encrypted password.
     */
    public String getPassword() {
      return password;
    }

    /**
     * Get the associated url entries.
     * @return The associated entries.
     */
    public List<UrlEntry> getEntry() {
      return entries;
    }

    /**
     * Get the associated note entries.
     * @return The associated entries.
     */
    public List<NoteEntry> getNoteEntry() {
      return noteEntries;
    }

    /**
     * Add a new entry to the list of entries associated with this User.
       * @param entry The entry to Add.
     */
    public void addEntry(UrlEntry entry) {
      entries.add(entry);
    }

  /**
     * Add a new note entry to the list of entries associated with this User.
     * @param entry The entry to Add.
     */
    public void addNoteEntry(NoteEntry entry) {
      noteEntries.add(entry);
    }

  /**
   * Gets the entries for this user.
   * @return the entries for this user.
   */

  public List<UrlEntry> getEntries() {
    return entries;
  }

  /**
   * Gets the note entries for this user.
   * @return the entries for this user.
   */

  public List<NoteEntry> getNoteEntries() {
    return noteEntries;
  }

  /**
   * Gets the keyword cloud image.
   * @return the image
   */
  public byte[] getImage() {
    return image;
  }

  /**
   * Sets the keyword cloud image.
   * @param image the image.
   */
  public void setImage(File image) {
    this.image = new byte[(int) image.length()];
    /* write the image data into the byte array */
    InputStream inStream = null;
    try {
      inStream = new BufferedInputStream(new FileInputStream(image));
      inStream.read(this.image);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      if (inStream != null) {
        try {
          inStream.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    this.save();
  }

  /**
   * Gets the first name of the user.
   * @return first name.
   */
  public String getFirstName() {
    return firstName;
  }
  /**
   * Sets the first name of the user.
   * @param firstName first name of the user.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  /**
   * Gets the last name of the user.
   * @return last name.
   */
  public String getLastName() {
    return lastName;
  }
  /**
   * Gets the first name of the user.
   * @param lastName last name of the user.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
