package web.scraping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Main {
    public static void main(String[] args) {
        System.out.println("Movie Data Scraper");

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--headless");

        WebDriver firefoxWebDriver = new FirefoxDriver(firefoxOptions);
        firefoxWebDriver.get("https://www2.smcinema.com/movies/now-showing");

        List<WebElement> allImageElements = firefoxWebDriver.findElements(By.id("movie-image"));
        List<byte[]> allImagesBytes = new ArrayList<>();

        for (WebElement imageElement : allImageElements) {
            byte[] data = ((TakesScreenshot) imageElement).getScreenshotAs(OutputType.BYTES);
            allImagesBytes.add(data);
        }

        try {
            if (allImageElements.size() == 0) {
                System.err.println("ERROR OCCURED");
                return;
            }

            for (int i = 0; i < allImageElements.size(); ++i) {
                Path image = Files
                        .createFile(Paths.get("/home/russel/Programming/movieticketscraper/movies-data/",
                                String.format("%s.jpg", allImageElements.get(i).getAttribute("alt"))));
                Files.write(image, allImagesBytes.get(i));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        firefoxWebDriver.quit();
    }
}
