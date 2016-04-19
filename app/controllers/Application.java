package controllers;

import java.awt.Color;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;
import models.EntryDB;
import models.ImageEntry;
import models.ImageInfo;
import models.Keywords;
import models.UrlEntry;
import models.UrlInfo;
import models.UserInfo;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import views.formdata.ImageUploadFormData;
import views.formdata.LoginFormData;
import views.formdata.NoteFormData;
import views.formdata.SearchFormData;
import views.formdata.SignupFormData;
import views.formdata.UrlFormData;
import views.html.*;
import wordcloud.CollisionMode;
import wordcloud.WordCloud;
import wordcloud.WordFrequency;
import wordcloud.bg.RectangleBackground;
import wordcloud.font.scale.LinearFontScalar;
import wordcloud.nlp.FrequencyAnalizer;
import wordcloud.palette.ColorPalette;

/**
 * Provides controllers for this application.
 */
public class Application extends Controller {

  /**
   * True if there are any search results.
   * false other wise.
   */
   public static boolean isImageGalleryResult = false; 
  /**
   * True if there are any search results.
   * false other wise.
   */
  public static boolean isSearchResult = false;
  /**
   * True no entry is present for the logged in user.
   * false other wise.
   */
  public static boolean noEntryForUser = false;

  /**
   * Returns the home page.
   *
   * @return The resulting home page.
   */
  public static Result index() {
    session().clear();
    //List<Tag> tag = cloud();
    //cloud();
    return ok(Index.render("Home", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
  }
  /**
   * Returns the Faq page.
   *
   * @return The resulting Faq page.
   */
  public static Result faq() {
    return ok(Faq.render("Faq", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
  }

  /**
   * Returns the welcome page.
   *
   * @return The resulting welcome page.
   */
  public static Result welcome() {
    UserInfo user = UserInfo.find().select("firstName").where().eq("email", Secured.getUser(ctx())).findUnique();
    return ok(Welcome.render("Welcome", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
  }
  /**
   * Returns the add bookmarklet page.
   *
   * @return The resulting add bookmarklet page.
   */
  public static Result addBookmarklet() {
    //session().clear();
    //List<Tag> tag = cloud();
    //cloud();
    return ok(AddBookmarklet.render("AddBookmarklet", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
  }
  /**
   * Provides the Login page (only to unauthenticated users).
   *@param message The message.
   * @return The Login page.
   */
  public static Result login(String message) {
    Form<LoginFormData> formData = Form.form(LoginFormData.class);
    return ok(Login.render("Login", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData, message));
  }

  /**
   * Processes a login form submission from an unauthenticated user.
   * First we bind the HTTP POST data to an instance of LoginFormData.
   * The binding process will invoke the LoginFormData.validate() method.
   * If errors are found, re-render the page, displaying the error data.
   * If errors not found, render the page with the good data.
   *
   * @return The index page with the results of validation.
   */
  public static Result postLogin() {

    // Get the submitted form data from the request object, and run validation.
    Form<LoginFormData> formData = Form.form(LoginFormData.class).bindFromRequest();

    if (formData.hasErrors()) {
      for (String key : formData.errors().keySet()) {
        List<ValidationError> currentError = formData.errors().get(key);
        for (play.data.validation.ValidationError error : currentError) {
          if (!error.message().equals("")) {
            flash(key, error.message());
          }
        }
      }
      return badRequest(Login.render("Login", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData, ""));
    }
    else {
      // email/password OK, so now we set the session variable and only go to authenticated pages.
      session().clear();
      session("email", formData.get().email);
      if (checkNoOfEntries() == 0) {
        return redirect(routes.Application.welcome());
      }
      else {
        return redirect(routes.Application.search());
      }
    }
  }

  /**
   * Logs out (only for authenticated users) and returns them to the Index page.
   *
   * @return A redirect to the Index page.
   */
  @Security.Authenticated(Secured.class)
  public static Result logout() {
    session().clear();
    return redirect(routes.Application.index());
  }

  /**
   * Provides the Signup page (only to unauthenticated users).
   *
   * @return The Signup page.
   */
  public static Result signup() {
    Form<SignupFormData> formData = Form.form(SignupFormData.class);
    return ok(Signup.render("Signup", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData));
  }

  /**
   * Processes a Signup form submission from an unauthenticated user.
   * First we bind the HTTP POST data to an instance of SignupFormData.
   * The binding process will invoke the SignupFormData.validate() method.
   * If errors are found, re-render the page, displaying the error data.
   * If errors not found, take the user to the login screen and display message.
   *
   * @return The index page with the results of validation.
   */
  public static Result postSignup() {

    // Get the submitted form data from the request object, and run validation.
    Form<SignupFormData> formData = Form.form(SignupFormData.class).bindFromRequest();

    if (formData.hasErrors()) {
      for (String key : formData.errors().keySet()) {
        List<ValidationError> currentError = formData.errors().get(key);
        for (play.data.validation.ValidationError error : currentError) {
          if (!error.message().equals("")) {
            flash(key, error.message());
          }
        }
      }
      return badRequest(Signup.render("Signup", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData));
    }
    else {
      // email/password OK, so now we set the session variable and only go to authenticated pages.
      //session().clear();
      //session("email", formData.get().email);
      SignupFormData dataFromForm = formData.get();
      EntryDB.addNewUser(dataFromForm.email, dataFromForm.password, dataFromForm.firstName, dataFromForm.lastName);
      return redirect(routes.Application.login("Success"));
    }
  }

  /**
   * Returns the enter url page.
   * @return the url form data.
   */
  @Security.Authenticated(Secured.class)
  public static Result enterUrl() {
    System.out.print("INSIDE ENTERURL");
    UrlFormData data = new UrlFormData();
    Form<UrlFormData> urlFormData = Form.form(UrlFormData.class).fill(data);
    return ok(EnterUrl.render("EnterUrl", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), urlFormData));
  }

  /**
   * Returns the page to enter url.
   * temporary until button is added.
   * @return the form data.
   */
  @Security.Authenticated(Secured.class)
  public static Result postEnterUrl() {
    System.out.print("INSIDE POST URL");
    Form<UrlFormData> urlFormData = Form.form(UrlFormData.class).bindFromRequest();
    if (urlFormData.hasErrors()) {
      System.out.print("INSIDE URL HAS ERRORS");
      return badRequest(EnterUrl.render("EnterUrl", Secured.isLoggedIn(ctx()),
          Secured.getUserInfo(ctx()), urlFormData));
    }
    else {
      ArrayList<Long> entryIdList = new ArrayList<Long>();
      String url = Form.form().bindFromRequest().get("url");
      //Long userId = Long.parseLong(Form.form().bindFromRequest().get("UserId"));
      if (url != null) {
        System.out.println("url---" + url);
        List<UrlEntry> idList = UrlEntry.find()
            .select("entryId")
            .where()
            .eq("email", Secured.getUser(ctx()))
            .findList();
        for (UrlEntry entry : idList) {
          entryIdList.add(entry.getEntryId());
        }
        int rowCount = UrlInfo.find()
            .select("url")
            .where()
            .ieq("url", url)
            .in("urlEntryId", entryIdList)
            .findRowCount();
        System.out.println("rowcount== " + rowCount);
        if (rowCount == 0) {
          //call class that captures data and feeds it to db.
          ProcessUrlData.processUrl(url);
          return ok(EnterUrl.render("EnterUrl", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), urlFormData));
       }
        else {
         return badRequest(EnterUrl.render("EnterUrl", Secured.isLoggedIn(ctx()),
             Secured.getUserInfo(ctx()), urlFormData));
        }
      }
      else {
        return badRequest(EnterUrl.render("EnterUrl", Secured.isLoggedIn(ctx()),
            Secured.getUserInfo(ctx()), urlFormData));
      }
    }
  }

  /**
   * Returns the add note page.
   * @return the note form data.
   */
  @Security.Authenticated(Secured.class)
  public static Result addNote() {
    System.out.print("INSIDE Addnote");
    NoteFormData data = new NoteFormData();
    Form<NoteFormData> noteFormData = Form.form(NoteFormData.class).fill(data);
    return ok(AddNote.render("AddNote", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), noteFormData));
  }

  
  /**
   * Returns the page to add note.
   * @return the form data.
   */
  @Security.Authenticated(Secured.class)
  public static Result postAddNote() {
    System.out.print("INSIDE POST Addnote");
    Form<NoteFormData> noteFormData = Form.form(NoteFormData.class).bindFromRequest();
    if (noteFormData.hasErrors()) {
      System.out.print("INSIDE URL HAS ERRORS");
      return badRequest(AddNote.render("AddNote", Secured.isLoggedIn(ctx()),
          Secured.getUserInfo(ctx()), noteFormData));
    }
    else {
      String note = Form.form().bindFromRequest().get("note");
      //Long userId = Long.parseLong(Form.form().bindFromRequest().get("UserId"));
      if (note != null) {
        System.out.println("note---" + note);
        //call class that captures data and feeds it to db.
        ProcessNoteData.processNote(note);
        return ok(AddNote.render("AddNote", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), noteFormData));
      }
      else {
        return badRequest(AddNote.render("AddNote", Secured.isLoggedIn(ctx()),
            Secured.getUserInfo(ctx()), noteFormData));
      }
    }
  }
  
  /**
   * Returns the upload image page.
   * @return the uploaded image form data.
   */
  @Security.Authenticated(Secured.class)
  public static Result uploadImage() {
    System.out.print("INSIDE uploadimage");
    ImageUploadFormData data = new ImageUploadFormData();
    MultipartFormData mdata = request().body().asMultipartFormData();
    try{
    if(mdata.getFile("image") != null) {
    	System.out.println("11111");
    FilePart image = mdata.getFile("image");
    	data = new ImageUploadFormData(image);
    } 
    }catch(Exception e){
    	System.out.println("2222");
    	//data = new ImageUploadFormData();
    }
    UploadImageForm df = new UploadImageForm();
    Form<UploadImageForm> imageUploadFormData = Form.form(UploadImageForm.class).fill(df);
    //Form<ImageUploadFormData> imageUploadFormData = Form.form(ImageUploadFormData.class).fill(data);
    //flash("success", "File uploaded.");
    return ok(UploadImage.render("UploadImage", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), imageUploadFormData));
  }
  
  /**
   * Returns the page to upload image.
   * @return the form data.
   */
  @Security.Authenticated(Secured.class)
  public static Result postUploadImage() {
    System.out.print("INSIDE POST upload omage");
    /*MultipartFormData mdata = request().body().asMultipartFormData();
    ImageUploadFormData data = new ImageUploadFormData(mdata.getFile("image"));*/
    Form<UploadImageForm> uploadImageFormData = Form.form(UploadImageForm.class).bindFromRequest();
    //Form<ImageUploadFormData> uploadImageFormData = Form.form(ImageUploadFormData.class).bindFromRequest();
    if (uploadImageFormData.hasErrors()) {
      System.out.print("INSIDE IMAGE HAS ERRORS");
      return badRequest(UploadImage.render("UploadImage", Secured.isLoggedIn(ctx()),
          Secured.getUserInfo(ctx()), uploadImageFormData));
    }
    else {
      File image = uploadImageFormData.get().uploadedImage.getFile();
      String imageName = uploadImageFormData.get().uploadedImage.getFilename();
      String imageContent = uploadImageFormData.get().uploadedImage.getContentType();
      System.out.println("imagecontent--"+imageContent);
      System.out.println("image name---"+imageName);
      //Long userId = Long.parseLong(Form.form().bindFromRequest().get("UserId"));
      if (image != null) {
        //System.out.println("note---" + note);
        //call class that captures data and feeds it to db.
        ProcessImageData.processImage(image, imageName);
        return ok(UploadImage.render("UploadImage", Secured.isLoggedIn(ctx()), 
        		Secured.getUserInfo(ctx()), uploadImageFormData));
      }
      else {
        return badRequest(UploadImage.render("UploadImage", Secured.isLoggedIn(ctx()),
            Secured.getUserInfo(ctx()), uploadImageFormData));
      }
    }
  }
  public static class UploadImageForm {
      public FilePart uploadedImage;
      
      public String validate() {
          MultipartFormData data = request().body().asMultipartFormData();
          uploadedImage = data.getFile("uploadedImage");
          
          if (uploadedImage == null) {
              return "File is missing.";
          }
  
          return null;
      }
  } 
  /**
   * Returns the search page.
   * @return the searchFormData and an empty urlList
   */
  @Security.Authenticated(Secured.class)
  public static Result search() {
    //buildCloud();
    System.out.println("id----" + Secured.getUserInfo(ctx()).getId());
    System.out.println("EMAIL----" + Secured.getUser(ctx()));

    EntryDB.updateUserImage(Secured.getUserInfo(ctx()).getId(), Secured.getUser(ctx()));
    int entryCount = checkNoOfEntries();
    if (entryCount == 0) {
      noEntryForUser = true;
    }
    else {
      noEntryForUser = false;
    }
    isSearchResult = false;
    List<UrlInfo> urlList = new ArrayList<UrlInfo>();
    SearchFormData data = new SearchFormData();
    Form<SearchFormData> searchFormData = Form.form(SearchFormData.class).fill(data);
    return ok(Search.render("Search", Secured.isLoggedIn(ctx()),
        Secured.getUserInfo(ctx()), searchFormData, urlList, isSearchResult, noEntryForUser));
  }
  /**
   * Gets the image that has been uploaded to the database.
   * @return The image.
   */
  public static Result getImage() {
    UserInfo user = UserInfo.find()
                    .select("image")
                    .where()
                    .eq("email", Secured.getUser(ctx()))
                    .findUnique();
    if (user == null) {
      throw new RuntimeException("Could not find the image with associated user.");
    }

    return ok(user.getImage()).as("image");
  }

  /**
   * Returns the gallery page with results.
   * @return the gallery FormData and the image list.
   */
  @Security.Authenticated(Secured.class)
  public static Result getGalleryImageIds() {
	  System.out.println("Inside getGallleryImages  Id");
	  List<ImageInfo> imageIdList = new ArrayList<ImageInfo>();
    imageIdList = SearchImages.searchAllImages();
      if(!imageIdList.isEmpty())
    	  isImageGalleryResult = true;
    	
      return ok(ImageGallery.render("ImageGallery", Secured.isLoggedIn(ctx()),
            Secured.getUserInfo(ctx()), imageIdList, isImageGalleryResult));
  }

  /**
   * Gets the images that have been uploaded to the database.
   * @param id The id of the image to be fetched.
   * @return The image.
   */
  public static Result getGalleryImages(long id) {
    ImageInfo images = ImageInfo.find()
                      .select("image")
                      .where()
                      .in("imageId", id)
                      .findUnique();
    if (images == null) {
      throw new RuntimeException("Could not find the image with associated user.");
    }

    return ok(images.getImage()).as("image");
  }
  /**
   * deletes the selected image and refreshes the page.
   * @param id Id of the selected image.
   */
  @Security.Authenticated(Secured.class)
  public static Result deleteImages(long id){
    System.out.println("Inside delete---" + id);
    ImageInfo.find().ref(id).delete();
    ImageEntry.find().ref(id).delete();
    //getGalleryImageIds();
    System.out.println("Inside getGallleryImages  Id");
    List<ImageInfo> imageIdList = new ArrayList<ImageInfo>();
    imageIdList = SearchImages.searchAllImages();
    if(!imageIdList.isEmpty())
      isImageGalleryResult = true;

    return ok(ImageGallery.render("ImageGallery", Secured.isLoggedIn(ctx()),
        Secured.getUserInfo(ctx()), imageIdList, isImageGalleryResult));
  }
  /**
   * Returns the search page with results.
   * @return the searchFormData and the url list.
   */
  @Security.Authenticated(Secured.class)
  public static Result searchResult() {
    isSearchResult = true;
    noEntryForUser = false;
    List<UrlInfo> urlList = new ArrayList<UrlInfo>();

    Form<SearchFormData> searchFormData = Form.form(SearchFormData.class).bindFromRequest();
    if (searchFormData.hasErrors()) {
      return badRequest(Search.render("Search", Secured.isLoggedIn(ctx()),
          Secured.getUserInfo(ctx()), searchFormData, urlList, isSearchResult, noEntryForUser));
    }
    else {
      String queryData = Form.form().bindFromRequest().get("queryData");
      System.out.println("queryData in application---" + queryData);
      if (queryData != null) {
        ArrayList<String> queryKeywords = new ArrayList<String>();
        Collections.addAll(queryKeywords, queryData.toLowerCase().split("\\W"));
        System.out.println("query arraylist--" + queryKeywords);
        urlList = SearchEntries.searchUrl(queryKeywords);
        return ok(Search.render("Search", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), searchFormData,
            urlList, isSearchResult, noEntryForUser));
      }
      else {
        return badRequest(Search.render("Search", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()),
            searchFormData, urlList, isSearchResult, noEntryForUser));
      }
    }
  }
  /**
   * Returns the search page with results.
   * @return the searchFormData and the url list.
   */
  @Security.Authenticated(Secured.class)
  public static Result myLinks() {
    isSearchResult = true;
    List<UrlInfo> urlList = new ArrayList<UrlInfo>();
        urlList = SearchEntries.searchAllUrl();
        return ok(MyLinks.render("MyLinks", Secured.isLoggedIn(ctx()),
            Secured.getUserInfo(ctx()), urlList, isSearchResult));
  }

  /**
   * Deletes the selected url and keywords associated with it and refreshes the page.
   * @param id Id of the selected url.
   */
  @Security.Authenticated(Secured.class)
  public static Result deleteUrl(long id){
    System.out.println("Inside delete url---" + id);
    String query = "DELETE FROM keywords WHERE keyword_entry_id = "+id;
    SqlUpdate down = Ebean.createSqlUpdate(query);
    down.execute();
    UrlInfo.find().ref(id).delete();
    UrlEntry.find().ref(id).delete();
    //getGalleryImageIds();
    isSearchResult = true;
    List<UrlInfo> urlList = new ArrayList<UrlInfo>();
    urlList = SearchEntries.searchAllUrl();
    return ok(MyLinks.render("MyLinks", Secured.isLoggedIn(ctx()),
        Secured.getUserInfo(ctx()), urlList, isSearchResult));
  }

  /**
   * Returns the page to enter url.
   * temporary until button is added.
   * @return the form data.
   */
  @Security.Authenticated(Secured.class)
  public static Result enterUrlTest() {
    ArrayList<Long> entryIdList = new ArrayList<Long>();
    String url = Form.form().bindFromRequest().get("url");
    String bk = Form.form().bindFromRequest().get("bk");
    //Long userId = Long.parseLong(Form.form().bindFromRequest().get("UserId"));
    System.out.println("INSIDE BOOKMARKLET URL TEST");
    if (url != null) {
      System.out.println("url---" + url);
      List<UrlEntry> idList      = UrlEntry.find()
                              .select("entryId")
                              .where()
                              .eq("email", Secured.getUser(ctx()))
                              .findList();
      for (UrlEntry entry : idList) {
        entryIdList.add(entry.getEntryId());
      }
      System.out.println("BK ENTRY ID LIST--" + entryIdList);
      int rowCount = UrlInfo.find()
                  .select("url")
                  .where()
                  .ieq("url", url)
                  .in("urlEntryId", entryIdList)
                  .findRowCount();
      System.out.println("rowcount== " + rowCount);
      if (rowCount == 0) {
        //call class that captures data and feeds it to db.
        ProcessUrlData.processUrl(url);
        return ok(Bookmarklet.render("Bookmarklet", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
      }
      else {
        return badRequest(Bookmarklet.render("Bookmarklet", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
      }
    }
    else {
      return badRequest(Bookmarklet.render("Bookmarklet", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
    }
  }
  /**
   * Allows JavaScript to access routes.
   * Not using as of now.
   * This is necessary to allow JavaScript issue GET/POST requests to the
   * correct route.
   *
   * @return Result object.
   */
  /*public static Result jsRoutes() {
    response().setContentType("text/javascript");
    return ok(
        // Every route accessible to JavaScript needs to be added here.
        Routes.javascriptRouter("jsRoutes" , controllers.routes.javascript.Application.enterUrlTest()));
  }*/

   /**
   * Define any extra CORS headers needed for option requests (see http://enable-cors.org/server.html for more info).
   * @param all all.
   * @return ok after setting header.
   */
  public static Result preflight(String all) {
    System.out.println("PREFLIGHT");
    response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
    response().setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept");
    return ok();
  }

  /**
   * Builds a png of the word cloud of the logged in user's keywords.
   * The png is displayed in the search screen.
   * @return File Returns the image file of the cloud.
   */
  public static File buildCloud() {
    System.out.println("INSIDE BUILD");
    List<String> keywords = new ArrayList<String>();
    File image = new File("public/images/DefaultCloud.png");
    ArrayList<Long> entryIdList = new ArrayList<Long>();
    //Check if any entry is present or not
    int entryCount = checkNoOfEntries();
    if (entryCount == 0) {
      return image;
    }
    else {
      List<UrlEntry> idList = UrlEntry.find()
          .select("entryId")
          .where()
          .eq("email", Secured.getUser(ctx()))
          .findList();
      for (UrlEntry entry : idList) {
        entryIdList.add(entry.getEntryId());
      }
      List<Keywords> list = Keywords.find()
          .select("keyword")
          .where()
          .in("keywordEntryId", entryIdList)
          .orderBy("keywordRelevance")
          .findList();
      for (Keywords keyword : list) {
        keywords.add(keyword.getKeyword());
      }
      System.out.println("word keywords list----"+keywords);
      try {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        //frequencyAnalizer.setWordFrequencesToReturn(20);
        //frequencyAnalizer.setMinWordLength(4);
        //frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(keywords);
        //System.out.println("list=====00"+Arrays.toString(list.toArray()));
        final WordCloud wordCloud = new WordCloud(400, 200, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
      /*try {
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("public/images/TagIt.png")));
      }
      catch (Exception e) {
        System.out.println("e.p");
        e.printStackTrace();
      }*/
        //wordCloud.setBackground(new CircleBackground(150));
        wordCloud.setBackground(new RectangleBackground(800, 200));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1),
            new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        //wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        //wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        //wordCloud.setColorPalette(buildRandomColorPallete(20));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        System.out.println("Before writing");
        wordCloud.writeToFile("public/images/Wordcloud.png");
        image = new File("public/images/Wordcloud.png");
        System.out.println("After writing");
      }
      catch (Exception e) {
        System.out.print("Exception" + e);
      }
      return image;
    }
  }

  /**
   * Gets the input stream to set background of word cloud.
   * @param path path of the image for the background.
   * @return the path thread.
   */
  private static InputStream getInputStream(String path) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
  }

  /**
   * Makes a word cloud with links.
   * Not using it as of now.
   * @return the tags added to the cloud.
   */
  public static List cloud() {
    final double maxWeight = 38.0;
    Cloud cloud = new Cloud();  // create cloud
    cloud.setMaxWeight(maxWeight);   // max font size
    cloud.setWordPattern("circle");
    cloud.setTagCase(Cloud.Case.CAPITALIZATION);
    System.out.println("INSIDE CLOUD");
    //List<String> keywords = new ArrayList<String>();
    List<Keywords> list = Keywords.find()
                        .select("keyword")
                        .select("score")
                        .where()
                        .findList();

    for (Keywords keyword: list) {
      //keywords.add(keyword.getKeyword());
      Long id = Keywords.find().select("keywordEntryId").where().eq("keyword",
          keyword.getKeyword()).findUnique().getId();
      String url = UrlInfo.find().select("url").where().eq("entryId", id).findUnique().getUrl();
      Tag tag = new Tag(keyword.getKeyword(), url);   // creates a tag
      cloud.addTag(tag);
    }
    return (cloud.allTags());

  }

  /**
   * Checks if there are any entries for the logged in user.
   * @return the entry count for the logged in user.
   */
  private static int checkNoOfEntries() {
    //Check if any entry is present or not
    int entryCount = UrlEntry.find()
        .select("entryId")
        .where()
        .eq("email", Secured.getUser(ctx()))
        .findRowCount();
    return entryCount;
  }
}
