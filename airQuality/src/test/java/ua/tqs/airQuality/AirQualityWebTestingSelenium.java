package ua.tqs.airQuality;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.util.*;

public class AirQualityWebTestingSelenium {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;

  @BeforeEach
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @Test
  public void testBasicFunctionsAndCaching() {
    WebDriverWait wait = new WebDriverWait(driver, 40);

    driver.get("http://localhost:8080/home");
    driver.manage().window().setSize(new Dimension(1477, 805));
    driver.findElement(By.id("city")).click();
    driver.findElement(By.id("city")).sendKeys("Coimbra");
    driver.findElement(By.id("country")).click();
    driver.findElement(By.id("country")).sendKeys("Portugal");
    driver.findElement(By.cssSelector("p:nth-child(3) > input")).click();

    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".w3-display-right > .headerResult:nth-child(1)")));
    assertThat(driver.findElement(By.cssSelector(".w3-display-right > .headerResult:nth-child(1)")).getText(), is("Number of requests : 1"));
    assertThat(driver.findElement(By.cssSelector(".w3-display-right > .headerResult:nth-child(2)")).getText(), is("Hits : 0"));
    assertThat(driver.findElement(By.cssSelector(".w3-display-right > .headerResult:nth-child(3)")).getText(), is("Misses : 1"));
    
    driver.findElement(By.linkText("Go back")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city")));
    driver.findElement(By.id("city")).click();
    driver.findElement(By.id("city")).sendKeys("Coimbra");
    driver.findElement(By.id("country")).click();
    driver.findElement(By.id("country")).sendKeys("Portugal");
    driver.findElement(By.cssSelector("p:nth-child(3) > input")).click();

    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".w3-display-right > .headerResult:nth-child(1)")));
    assertThat(driver.findElement(By.cssSelector(".w3-display-right > .headerResult:nth-child(1)")).getText(), is("Number of requests : 2"));
    assertThat(driver.findElement(By.cssSelector(".w3-display-right > .headerResult:nth-child(2)")).getText(), is("Hits : 1"));
    assertThat(driver.findElement(By.cssSelector(".w3-display-right > .headerResult:nth-child(3)")).getText(), is("Misses : 1"));
    
    driver.findElement(By.linkText("Go back")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city")));
    driver.findElement(By.id("city")).click();
    driver.findElement(By.id("city")).sendKeys("Kanpur");
    driver.findElement(By.id("country")).click();
    driver.findElement(By.id("country")).sendKeys("India");
    driver.findElement(By.cssSelector("p:nth-child(3) > input")).click();

    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".w3-display-right > .headerResult:nth-child(1)")));
    assertThat(driver.findElement(By.cssSelector(".w3-display-right > .headerResult:nth-child(1)")).getText(), is("Number of requests : 3"));
    assertThat(driver.findElement(By.cssSelector(".w3-display-right > .headerResult:nth-child(2)")).getText(), is("Hits : 1"));
    assertThat(driver.findElement(By.cssSelector(".w3-display-right > .headerResult:nth-child(3)")).getText(), is("Misses : 2"));
  }
}
