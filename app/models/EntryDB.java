package models;

import controllers.Application;
import play.db.ebean.Model;
import controllers.Secured;
import org.mindrot.jbcrypt.BCrypt;
import play.mvc.Http;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Adds a new entry to the database. Adds the url information related to entry.
 * Adds the keywords associated with entry.
 */
public class EntryDB extends Model {

  /**
   * Get a specific User from the database using email.
   *
	 * @param email
	 *            The email address associated with the user.
   * @return The User object or Null if not found.
   */
  public static UserInfo getUser(String email) {
		UserInfo userFromDB = UserInfo.find().where().eq("email", email)
				.findUnique();
    return userFromDB;
  }

  /**
   * Get a specific User from the database using ID.
   *
	 * @param id
	 *            The email address associated with the user.
   * @return The User object or Null if not found.
   */
  public static UserInfo getUser(long id) {
    UserInfo userFromDB = UserInfo.find().byId(id);
    return userFromDB;
  }

  /**
   * Check if an email address is associated with an existing user.
   *
	 * @param email
	 *            The email address to check.
   * @return True if exists, otherwise false.
   */
  public static boolean isUser(String email) {
    int count = UserInfo.find().where().eq("email", email).findRowCount();
    return count >= 1;
  }

  /**
   * Create a new user and save them to the database with encrypted password.
   *
	 * @param email
	 *            Email Address
	 * @param password
	 *            The password to save with the user.
	 * @param firstName
	 *            The first name of the user.
	 * @param lastName
	 *            The last name of the user.
   */
	public static void addNewUser(String email, String password,
			String firstName, String lastName) {
    System.out.println("INSIDE ADD USER");
		UserInfo user = new UserInfo(email, BCrypt.hashpw(password,
				BCrypt.gensalt(12)), firstName, lastName);
    user.setImage(Application.buildCloud());
    user.save();
  }

  /**
   * Create a new user and save them to the database with encrypted password.
	 * 
	 * @param email
	 *            Email Address
	 * @param id
	 *            The id associated with the user.
   */
  public static void updateUserImage(Long id, String email) {
    System.out.println("INSIDE UPDATE USER");
		UserInfo user = UserInfo.find().select("email").where()
				.eq("email", email).findUnique();
    user.setImage(Application.buildCloud());
    user.update();
  }

  /**
   * Returns true if email and password are valid credentials.
   *
	 * @param email
	 *            The email.
	 * @param password
	 *            The password.
	 * @return True if email is a valid user email and password is valid for
	 *         that email.
   */
  public static boolean isValid(String email, String password) {
		return ((email != null) && (password != null) && isUser(email) && BCrypt
				.checkpw(password, getUser(email).getPassword()));
  }

  /**
   * Adds entry to the database.
	 * 
	 * @param entryType
	 *            the type of entry.
	 * @param keywords
	 *            the keywords associated with the entry.
	 * @param urlType
	 *            the type of url
	 * @param url
	 *            the url.
	 * @param keywordRelevance
	 *            the relevance of each keyword.
	 * @param context
	 *            the Http context.
   */

	public static void addUrlEntry(String entryType,
			ArrayList<String> keywords, ArrayList<Double> keywordRelevance,
                              String urlType, String url, String urlSnippet,
															boolean ogImagePresent, String urlOGImage, Http.Context context) {

		/*
		 * for(double rel: keywordRelevance) {
		 * System.out.println("Rele---"+rel); }
		 */

		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
		String time = new SimpleDateFormat("h:mm a").format(new Date()).toString();

		System.out.println("date--"+date+"---time---"+time);


    ArrayList<Keywords> keywordList = new ArrayList<Keywords>();
    int i = 0;
    for (String keywordString : keywords) {
			keywordList
					.add(new Keywords(keywordString, keywordRelevance.get(i)));
      i++;
    }

    UrlInfo urlInfo = new UrlInfo(urlType, url, urlSnippet, date,time,ogImagePresent,urlOGImage);
    String email = Secured.getUser(context);
    UserInfo userInfo = getUser(email);
    //userInfo.setImage(Application.buildCloud());
		UrlEntry entry = new UrlEntry(entryType, keywordList,
				urlInfo, userInfo);
    //get logged in users email.
    System.out.println("email of logged in ---" + Secured.getUser(context));
    entry.setEmail(email);

    entry.setUrlInfo(urlInfo);
    entry.setUserInfo(userInfo);
    urlInfo.setEntry(entry);

    entry.setKeywords(keywordList);

    entry.save();
    urlInfo.setUrlEntryId(entry.getEntryId());
    urlInfo.save();
    i = 0;
    for (String keywordString : keywords) {
			Keywords keywords1 = new Keywords(keywordString,
					keywordRelevance.get(i));
			keywords1.setUrlEntry(entry);
      keywords1.setKeywordEntryId(entry.getEntryId());
      keywords1.save();
      i++;
    }
  }


	/**
	 * Adds entry to the database.
	 * 
	 * @param entryType
	 *            the type of entry.
	 * @param keywords
	 *            the keywords associated with the entry.
	 * @param note
	 *            the note.
	 * @param keywordRelevance
	 *            the relevance of each keyword.
	 * @param context
	 *            the Http context.
	 */

	public static void addNoteEntry(String entryType,
			ArrayList<String> keywords, ArrayList<Double> keywordRelevance,
			String note, String noteTitle, Http.Context context) {

		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
		String time = new SimpleDateFormat("h:mm a").format(new Date()).toString();

		/*String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());*/

		ArrayList<Keywords> keywordList = new ArrayList<Keywords>();
		int i = 0;
		for (String keywordString : keywords) {
			keywordList
					.add(new Keywords(keywordString, keywordRelevance.get(i)));
			i++;
		}

		NoteInfo noteInfo = new NoteInfo(note, noteTitle,date, time);
		String email = Secured.getUser(context);
		UserInfo userInfo = getUser(email);
		// userInfo.setImage(Application.buildCloud());
		NoteEntry entry = new NoteEntry(entryType, keywordList,
				noteInfo, userInfo);
		// get logged in users email.
		System.out.println("email of logged in ---" + Secured.getUser(context));
		entry.setEmail(email);

		entry.setNoteInfo(noteInfo);
		entry.setUserInfo(userInfo);
		noteInfo.setEntry(entry);

		entry.setKeywords(keywordList);

		entry.save();
		noteInfo.setNoteEntryId(entry.getEntryId());
		noteInfo.save();
		i = 0;
		for (String keywordString : keywords) {
			Keywords keywords1 = new Keywords(keywordString,
					keywordRelevance.get(i));
			keywords1.setNoteEntry(entry);
			keywords1.setKeywordEntryId(entry.getEntryId());
			keywords1.save();
			i++;
		}
	}

	/**
	 * Adds file entry to the database.
	 * 
	 * @param entryType
	 *            the type of entry.
	 * @param keywords
	 *            the keywords associated with the entry.
	 * @param file
	 *            the image.
	 * @param keywordRelevance
	 *            the relevance of each keyword.
	 * @param context
	 *            the Http context.
	 * @param fileName the name of the file.
	 * @param fileType the extension of the file.
	 */

	public static void addFileEntry(String entryType,
			ArrayList<String> keywords, ArrayList<Double> keywordRelevance,
			byte[] file, String fileName, String fileType, Http.Context context) {

		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
		String time = new SimpleDateFormat("h:mm a").format(new Date()).toString();

		ArrayList<Keywords> keywordList = new ArrayList<Keywords>();
		int i = 0;
		for (String keywordString : keywords) {
			keywordList
					.add(new Keywords(keywordString, keywordRelevance.get(i)));
			i++;
		}

		FileInfo fileInfo = new FileInfo(file, fileName, fileType, date, time);
		String email = Secured.getUser(context);
		UserInfo userInfo = getUser(email);
		// userInfo.setImage(Application.buildCloud());
		FileEntry entry = new FileEntry(entryType, keywordList,
				fileInfo, userInfo);
		// get logged in users email.
		System.out.println("image email of logged in ---" + Secured.getUser(context));
		entry.setEmail(email);

		entry.setFileInfo(fileInfo);
		entry.setUserInfo(userInfo);
		fileInfo.setEntry(entry);

		entry.setKeywords(keywordList);

		entry.save();
		fileInfo.setFileEntryId(entry.getEntryId());
		fileInfo.save();
		i = 0;
		for (String keywordString : keywords) {
			Keywords keywords1 = new Keywords(keywordString,
					keywordRelevance.get(i));
			keywords1.setFileEntry(entry);
			keywords1.setKeywordEntryId(entry.getEntryId());
			keywords1.save();
			i++;
		}
	}

}
