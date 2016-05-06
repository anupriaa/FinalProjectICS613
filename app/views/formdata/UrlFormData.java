package views.formdata;

import jdk.nashorn.internal.runtime.regexp.RegExp;
import play.data.validation.ValidationError;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Backing class for the enter url form.
 */
public class UrlFormData {
  /**
   * String to hold url.
   */
  public String url = "";

  /** Required for form instantiation. */
  public UrlFormData() {
  }

  /**
   * Validates Form<UrlFormData>.
   * Called automatically in the controller by bindFromRequest().
   * Checks to see that email and password are valid credentials.
   * @return Null if valid, or a List[ValidationError] if problems found.
   */
  public List<ValidationError> validate() {
    List<ValidationError> errors = new ArrayList<>();
    try{
      final URI u = new URI(url);

      if(!u.isAbsolute()){
        errors.add(new ValidationError("url", "Please enter an absolute url."));
      }
      if (url == null || url.length() == 0) {
        errors.add(new ValidationError("url", "Please enter the url."));
      }

    }catch(Exception e){
      e.printStackTrace();
    }
    return (errors.size() > 0) ? errors : null;
  }

}
