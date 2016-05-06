package views.formdata;

import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;

import java.util.ArrayList;
import java.util.List;

/**
 * Backing class for the enter ImageUpload form.
 */
public class FileUploadFormData {
  /**
   * File to hold ImageUpload.
   */
  public FilePart uploadedFile ;
  /**
   * String to hold image name.
   */
  //public String uploadedImageName = "";

  /** 
   * Required for form instantiation. 
   * */
  public FileUploadFormData(FilePart uploadedImage) {
	  System.out.println("33333");
	  this.uploadedFile = uploadedImage;
  }
  /** 
   * Required for form instantiation. 
   * */
  public FileUploadFormData() {
	  System.out.println("44444");
  }

  /**
   * Validates Form<ImageUploadFormData>.
   * Called automatically in the controller by bindFromRequest().
   * Checks to see that email and password are valid credentials.
   * @return Null if valid, or a List[ValidationError] if problems found.
   */
  public List<ValidationError> validate() {
	  System.out.println("55555");

    List<ValidationError> errors = new ArrayList<>();
   /* play.mvc.Http.MultipartFormData data = request().body().asMultipartFormData();
    uploadedFile = data.getFile("uploadedFile");*/

    if (uploadedFile == null) {
    	System.out.println("INSIDE IMAGE ERROR");
      errors.add(new ValidationError("uploadedFile", "Please enter the file to upload."));
    }
    return (errors.size() > 0) ? errors : null;
  }

}
