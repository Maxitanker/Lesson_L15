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

    @BeforeEach
    public void setup() {
        // Настройка WebDriver с использованием WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.mts.by/?hash-offset=70&hash-dur=1300#pay-section");
        WebElement Button = wait.until(ExpectedConditions.elementToBeClickable(By.id("cookie-agree")));
        if (Button.isDisplayed()) {
            System.out.println("Окно с куками закрыто");
            Button.click();
        }
    }
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
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
    @DisplayName("Заполнение данных для оплаты")
    public void phone() {
        //297777777
        WebElement phone = driver.findElement(By.id("connection-phone"));
        WebElement amount = driver.findElement(By.id("connection-sum"));

        phone.sendKeys("297777777");
        amount.sendKeys("100");

        // Отправление формы
        WebElement submit = driver.findElement(By.cssSelector("#pay-connection > button"));
        submit.click();
    }

    @Test
    @DisplayName("Проверка ссылки 'Подробнее о сервисе'")
    public void testURL() {
        driver.findElement(By.linkText("Подробнее о сервисе")).click(); // Я не знаю почему, но у меня страница с окном "о сервисе" загружается секунд 40. Возможно дело в регионах
    }
}




