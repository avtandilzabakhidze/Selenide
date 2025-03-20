import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
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
}
