package views.formdata;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Backing class for the add note form.
 */
public class NoteFormData {
  /**
   * String to hold note title.
   */
  public String noteTitle = "";
  /**
   * String to hold note.
   */
  public String note = "";

  /** Required for form instantiation. */
  public NoteFormData() {
  }

  /**
   * Validates Form<NoteFormData>.
   * Called automatically in the controller by bindFromRequest().
   * Checks to see that email and password are valid credentials.
   * @return Null if valid, or a List[ValidationError] if problems found.
   */
  public List<ValidationError> validate() {

    List<ValidationError> errors = new ArrayList<>();

    /*if (noteTitle == null || noteTitle.length() == 0) {
      errors.add(new ValidationError("note", "Please enter the title of the note."));
    }*/
    if (note == null || note.length() == 0) {
      errors.add(new ValidationError("note", "Please enter the note."));
    }
    return (errors.size() > 0) ? errors : null;
  }

}
