package views.formdata;

import play.data.validation.ValidationError;

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

    if (url == null || url.length() == 0) {
      errors.add(new ValidationError("url", "Please enter the url."));
    }
    return (errors.size() > 0) ? errors : null;
  }

}
