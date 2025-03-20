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
    public void testAddRemoveElements() {
        $(By.linkText("Add/Remove Elements")).click();

        $(By.xpath("//button[@onclick=\"addElement()\"]")).click();
        SelenideElement deleteButton = $(By.xpath("//button[@onclick=\"deleteElement()\"]"));

        deleteButton.shouldBe(visible);
        deleteButton.click();
        deleteButton.shouldNotBe(visible);
    }

    @Test
    public void testBaseAuthorization(){
        //user and password is: admin
        String user = "admin";
        String password = "admin";
        String url = String.format("https://%s:%s@the-internet.herokuapp.com/basic_auth", user, password);
        open(url);

        $(By.xpath("//p[contains(text(),'Congratulations! You must have the proper credentials.')]")).shouldBe(visible);
    }

    @Test
    public void testCheckboxes(){
        $(By.linkText("Checkboxes")).click();

        $(By.xpath("//div[@id ='content']//input[1]")).click();
        $(By.xpath("//div[@id ='content']//input[1]")).shouldBe(checked);
    }

    @Test
    public void testContextMenu(){
        $(By.linkText("Context Menu")).click();

        $(By.id("hot-spot")).contextClick();

        Alert alert = switchTo().alert();
        alert.accept();
    }

    @Test
    public void testDropdown(){
        $(By.linkText("Dropdown")).click();

        $(By.id("dropdown")).selectOption("Option 2");
        $(By.xpath("//option[@value=\"2\"]")).shouldBe(selected);
    }

    @Test
    public void testTypos(){
        $(By.linkText("Typos")).click();

        String current = $(By.xpath("//div[@class='example']//p[2]")).getText();
        Assert.assertEquals(current, "Sometimes you'll see a typo, other times you won't.", "\n contains typo\n");
    }

    @Test
    public void testDragAndDrop(){
        $(By.linkText("Drag and Drop")).click();

        SelenideElement A = $(By.id("column-a"));
        SelenideElement B = $(By.id("column-b"));
        actions().dragAndDrop(A, B).perform();

        Assert.assertEquals(A.getText(), "B", "\n Drag and Drop Don't Work \n");
    }
}
