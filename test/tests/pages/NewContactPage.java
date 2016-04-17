package tests.pages;

import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

import static org.fest.assertions.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withId;

/**
 * Provides testing support for the New DataEntry page.
 */
public class NewContactPage extends FluentPage {
  private String url;

  /**
   * Create the IndexPage.
   *
   * @param webDriver The driver.
   * @param port      The port.
   */
  public NewContactPage(WebDriver webDriver, int port) {
    super(webDriver);
    this.url = "http://localhost:" + port;
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public void isAt() {
    assertThat(title()).isEqualTo("Search (tagit)");
  }

  /**
   * Creates a new contact for testing.
   *
   * @param firstName     the first name.
   * @param lastName      the last name.
   * @param telephone     the tagId number.
   * @param telephoneType the tagId type.
   */
  public void createContact(String firstName, String lastName, String telephone, String telephoneType) {
    fill("#entryType").with(firstName);
    fill("#content").with(lastName);
    fill("#tagId").with(telephone);
    find("select", withId("tag")).find("option", withId(telephoneType)).click();
    submit("#submit");
  }
}
