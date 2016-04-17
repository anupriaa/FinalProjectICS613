package views.formdata;

import models.EntryDB;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Backing class for the login form.
 */
public class SignupFormData {

  /**
   * The submitted email.
   */
  public String email = "";

  /**
   * The submitted first Name.
   */
  public String firstName = "";

  /**
   * The submitted last Name.
   */
  public String lastName = "";

  /**
   * The submitted password.
   */
  public String password = "";

  /**
   * The submitted password.
   */
  public String confirmPassword = "";


  /** Required for form instantiation. */
  public SignupFormData() {
  }

  /**
   * Validates Form<LoginFormData>.
   * Called automatically in the controller by bindFromRequest().
   * Checks to see that email and password are valid credentials.
   * @return Null if valid, or a List[ValidationError] if problems found.
   */
  public List<ValidationError> validate() {

    List<ValidationError> errors = new ArrayList<>();

    if (email == null || email.length() == 0) {
      errors.add(new ValidationError("email", "Please enter an email address."));
    }

    if (EntryDB.isUser(email)) {
      errors.add(new ValidationError("email", "Email already exists."));
    }

    if (password == null || password.length() == 0) {
      errors.add(new ValidationError("password", "Please enter a password."));
    }

    if (password.length() > 0 && password.length() < 6) {
      errors.add(new ValidationError("password", "Please enter a password of at least 6 characters."));
    }

    if (confirmPassword == null || confirmPassword.length() == 0) {
      errors.add(new ValidationError("confirmPassword", "Please confirm password."));
    }

    if (!(confirmPassword.equals(password))) {
      errors.add(new ValidationError("confirmPassword", "Password and confirm password do not match"));
    }
    if (firstName == null || firstName.length() == 0) {
      errors.add(new ValidationError("firstName" , "Please enter your first name."));
    }
    if (lastName == null || lastName.length() == 0) {
      errors.add(new ValidationError("lastName" , "Please enter your last name."));
    }

    return (errors.size() > 0) ? errors : null;
  }

}