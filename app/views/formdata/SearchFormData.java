package views.formdata;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Backing class for the search form.
 */
public class SearchFormData {
  /**
   * String to hold queried keywords.
   */
  public String queryData = "";

  /** Required for form instantiation. */
  public SearchFormData() {
  }

  /**
   * Validates Form<SearchFormData>.
   * Called automatically in the controller by bindFromRequest().
   * Checks to see that email and password are valid credentials.
   * @return Null if valid, or a List[ValidationError] if problems found.
   */
  public List<ValidationError> validate() {

    List<ValidationError> errors = new ArrayList<>();

    if (queryData == null || queryData.length() == 0) {
      errors.add(new ValidationError("queryData", "Please enter a keyword to search."));
    }
    return (errors.size() > 0) ? errors : null;
  }
}
