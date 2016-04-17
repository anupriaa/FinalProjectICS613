package tests.pages;

import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Provides testing support for the Index page.
 */
public class IndexPage extends FluentPage {
  private String url;

  /**
   * Create the IndexPage.
   *
   * @param webDriver The driver.
   * @param port      The port.
   */
  public IndexPage(WebDriver webDriver, int port) {
    super(webDriver);
    this.url = "http://localhost:" + port;
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public void isAt() {
    assertThat(title()).isEqualTo("Home (digits)");
  }

  /**
   * Checks if contact added reflects on the home page.
   *
   * @param firstName     the first name.
   * @param lastName      the last name.
   * @param telephone     the tagId number.
   * @param telephoneType the tagId type.
   */
  public void hasContact(String firstName, String lastName, String telephone, String telephoneType) {
    assertThat(pageSource()).contains(firstName);
    assertThat(pageSource()).contains(lastName);
    assertThat(pageSource()).contains(telephone);
    assertThat(pageSource()).contains(telephoneType);
  }
}
