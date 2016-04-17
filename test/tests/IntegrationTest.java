package tests;

import org.junit.Test;
import play.libs.F.Callback;
import play.test.TestBrowser;
import tests.pages.IndexPage;
import tests.pages.NewContactPage;

import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

/**
 * Runs a server with a fake in-memory database to test the system.
 */
public class IntegrationTest {
  /**
   * The port to be used for testing.
   */
  private final int port = 3333;

  /**
   * Check to see that both the index and search pages can be retrieved.
   */
  @Test
  public void testRetrieveIndexPage() {
    running(testServer(port, fakeApplication(inMemoryDatabase())), HTMLUNIT,
        new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
            browser.maximizeWindow();
            IndexPage indexPage = new IndexPage(browser.getDriver(), port);
            browser.goTo(indexPage);
            indexPage.isAt();
          }
        });
  }

  /**
   * Checks to create a new contact, then go to Index page and verify that the new contact appears there.
   */
  public void testCreateNewContact() {
    running(testServer(port, fakeApplication(inMemoryDatabase())), HTMLUNIT,
        new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
            browser.maximizeWindow();
            IndexPage indexPage = new IndexPage(browser.getDriver(), port);
            NewContactPage contactPage = new NewContactPage(browser.getDriver(), port);
            browser.goTo(contactPage);
            contactPage.isAt();
            String firstName = "Anu";
            String lastName = "Sinha";
            String telephone = "812-789-78965";
            String telephoneType = "Work";
            contactPage.createContact(firstName, lastName, telephone, telephoneType);
            browser.goTo(indexPage);
            indexPage.hasContact(firstName, lastName, telephone, telephoneType);
          }
        });
  }
}
