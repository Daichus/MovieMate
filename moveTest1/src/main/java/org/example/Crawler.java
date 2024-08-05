package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class Crawler {
    private WebDriver driver;

    public  Crawler(WebDriver wd) {
        this.driver = wd;
    }
    public List<String> getMovieInfo(String website) {
        driver.get(website);
        List<WebElement> webElements = driver.findElements(By.className("infoArea"));
        List<String> movieInfo = new ArrayList<>();

        for (WebElement element : webElements) {
            webElements = driver.findElements(By.className("infoArea"));
            WebElement linkElement = element.findElement(By.tagName("a"));
            linkElement.click();
            WebElement movieTitle = driver.findElement(By.cssSelector("titleArea h1"));
            String movieName = movieTitle.getText();
            movieInfo.add(movieName);
            driver.navigate().back();
        }
        return movieInfo;
    }



}
