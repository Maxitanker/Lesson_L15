package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


// www.mts.by
// https://www.mts.by/?hash-offset=70&hash-dur=1300#pay-section
public class TestMTS {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setup() {
        // Настройка WebDriver с использованием WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /*@AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }*/

    @Test
    @DisplayName("Открытие страницы")
    public void launch() {
        driver.get("https://www.mts.by/?hash-offset=70&hash-dur=1300#pay-section");
    }

    @Test
    @DisplayName("Закрыть окно куки")
    public void closeCookiePopup() {
        WebElement Button = wait.until(ExpectedConditions.elementToBeClickable(By.id("cookie-agree")));
        if (Button.isDisplayed()) {
            System.out.println("Окно с куками закрыто");
            Button.click();
        }
    }

    @Test
    @DisplayName("Проверка заголовка внутри страницы")
    public void testHeaderElement() {
        String headerEx = "Онлайн пополнение\n" + "без комиссии";
        String headerAc = driver.findElement(By.xpath("//*[@id=\"pay-section\"]/div/div/div[1]/section/div/h2")).getText();
        assertEquals(headerEx, headerAc);
    }

    @Test
    @DisplayName("Проверка наличия иконок платежных систем")
    public void testPaymentPics() {
        List<WebElement> Logos = driver.findElements(By.className("pay__partners"));
        Assertions.assertFalse(Logos.isEmpty());
    }

    @Test
    @DisplayName("Проверка ссылки 'Подробнее о сервисе'") // не работает
    public void testURL() {
        driver.findElement(By.linkText("Подробнее о сервисе")).click();
    }

    @Test
    @DisplayName("Заполнение данных для оплаты") // недоделано
    public void phone() {
        //297777777
    }

}




