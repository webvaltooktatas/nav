package hu.webvalto;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FejlecEllenorzes {
    public static void main(String[] args) throws Exception {
        // Firefox driver
        // System.setProperty("webdriver.firefox.marionette", "src/test/resources/geckodriver");
        // WebDriver driver = new FirefoxDriver();
        // Chome driver
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver_mac");
        WebDriver driver = new ChromeDriver();

        String baseUrl = "http://localhost:8080/javaee";
        String expectedTitle = "Banki app";
        String actualTitle = "";

        // Bongeszo es az URL megnyitasa
        driver.get(baseUrl);
        Thread.sleep(10000l);
        actualTitle = driver.getTitle();

        if (actualTitle.contentEquals(expectedTitle)) {
            System.out.println("Teszt OK!");
        } else {
            System.out.println("Teszt HIBAS! Vart eredmeny: " + expectedTitle + ", kapott eredmeny: " + actualTitle);
        }

        //close Fire fox
        driver.quit();

    }
}
