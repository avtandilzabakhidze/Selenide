import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class InternetHerokuTests {
    @BeforeMethod
    public void setUp() {
        open("https://the-internet.herokuapp.com/");
    }

    @Test
    public void testPageVisibility() {
        $(By.linkText("A/B Testing")).click();
        String url = WebDriverRunner.url();
        Assert.assertEquals(url, "https://the-internet.herokuapp.com/abtest", "\n url doesn't match \n");

        $(By.xpath("//h3[text() = 'A/B Test Variation 1']")).shouldBe(visible);
    }

    @Test
    public void testKeyPresses() {
        $(By.linkText("Key Presses")).click();
        $(By.id("target")).pressTab();
        $(By.id("result")).shouldHave(text("You entered: TAB"));
    }

    @Test
    public void testAddRemoveElements() {
        $(By.linkText("Add/Remove Elements")).click();

        $(By.xpath("//button[@onclick=\"addElement()\"]")).click();
        SelenideElement deleteButton = $(By.xpath("//button[@onclick=\"deleteElement()\"]"));

        deleteButton.shouldBe(visible);
        deleteButton.click();
        deleteButton.shouldNotBe(visible);
    }

    @Test
    public void testBaseAuthorization() {
        //user and password is: admin
        String user = "admin";
        String password = "admin";
        String url = String.format("https://%s:%s@the-internet.herokuapp.com/basic_auth", user, password);
        open(url);

        $(By.xpath("//p[contains(text(),'Congratulations! You must have the proper credentials.')]")).shouldBe(visible);
    }

    @Test
    public void testCheckboxes() {
        $(By.linkText("Checkboxes")).click();

        $(By.xpath("//div[@id ='content']//input[1]")).click();
        $(By.xpath("//div[@id ='content']//input[1]")).shouldBe(checked);
    }

    @Test
    public void testContextMenu() {
        $(By.linkText("Context Menu")).click();

        $(By.id("hot-spot")).contextClick();

        Alert alert = switchTo().alert();
        alert.accept();
    }

    @Test
    public void testDropdown() {
        $(By.linkText("Dropdown")).click();

        $(By.id("dropdown")).selectOption("Option 2");
        $(By.xpath("//option[@value=\"2\"]")).shouldBe(selected);
    }

    @Test
    public void testTypos() {
        $(By.linkText("Typos")).click();

        String current = $(By.xpath("//div[@class='example']//p[2]")).getText();
        Assert.assertEquals(current, "Sometimes you'll see a typo, other times you won't.", "\n contains typo\n");
    }

    @Test
    public void testHovers() {
        $(By.linkText("Hovers")).click();
        ElementsCollection images = $$(By.xpath("//div[@class=\"figure\"]//img"));
        images.first().hover();
        $(By.xpath("//h5[text() ='name: user1']")).shouldBe(visible);
    }

    @Test
    public void testFormAuthentication() {
        $(By.linkText("Form Authentication")).click();
        $(By.name("username")).val("tomsmith");
        $(By.name("password")).val("SuperSecretPassword!");
        $(By.xpath("//button[@class=\"radius\"]")).click();

        $(By.xpath("//h4[text() ='Welcome to the Secure Area. When you are done click logout below.']")).shouldBe(visible);
    }

    @Test
    public void testChallengingDOM() {
        $(By.linkText("Challenging DOM")).click();
        SelenideElement blueButton = $(By.xpath("//a[@class=\"button\"]"));
        String oldText = blueButton.text();
        blueButton.click();
        sleep(4000);
        String currentText = blueButton.text();
        Assert.assertNotEquals(currentText, oldText, "\n contains challenging DOM \n");
    }

    @Test
    public void testJavaScriptAlerts() {
        $(By.linkText("JavaScript Alerts")).click();
        $(By.xpath("//button[text() ='Click for JS Alert']")).click();
        Alert alert = switchTo().alert();
        alert.accept();
        $(By.id("result")).shouldHave(text("You successfully clicked an alert"));
    }

    @Test
    public void testDragAndDrop() {
        $(By.linkText("Drag and Drop")).click();

        SelenideElement A = $(By.id("column-a"));
        SelenideElement B = $(By.id("column-b"));
        actions().dragAndDrop(A, B).perform();

        Assert.assertEquals(A.getText(), "B", "\n Drag and Drop Don't Work \n");
    }
}
