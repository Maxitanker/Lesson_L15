package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestMTS {

    private WebDriver driver;
    private MTSMainPage mtsMainPage;

    @BeforeAll
    public void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setupTest() {
        driver = new ChromeDriver();
        driver.get("https://www.mts.by/?hash-offset=70&hash-dur=1300#pay-section");
        mtsMainPage = new MTSMainPage(driver);
        mtsMainPage.acceptCookies();
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Проверка заголовка внутри страницы")
    public void testHeaderElement() {
        String expectedHeader = "Онлайн пополнение\nбез комиссии";
        String actualHeader = mtsMainPage.getHeaderText();
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    @DisplayName("Проверка наличия иконок платежных систем")
    public void testPaymentLogosDisplayed() {
        assertTrue(mtsMainPage.arePaymentLogosDisplayed(), "Логотипы платежных систем не отображаются.");
    }

    @Test
    @DisplayName("Заполнение данных для оплаты")
    public void testFillPaymentForm() {
        mtsMainPage.fillPaymentForm("297777777", "100");
        // Можно добавить проверки после заполнения формы, если требуется.
    }

    @Test
    @DisplayName("Проверка ссылки 'Подробнее о сервисе'")
    public void testMoreInfoLink() {
        mtsMainPage.clickMoreInfoLink();
        // Можно добавить проверку перехода на новую страницу.
    }

    @ParameterizedTest
    @CsvSource({
            "услуги связи, Номер телефона, Сумма",
            "домашний интернет, Номер телефона, Сумма",
            "рассрочка, Номер телефона, Сумма",
            "задолженность, Номер телефона, Сумма"
    })
    @DisplayName("Проверка надписей в незаполненных полях")
    public void testPlaceholders(String serviceOption, String expectedPhonePlaceholder, String expectedAmountPlaceholder) {
        mtsMainPage.selectServiceOption(serviceOption);

        String actualPhonePlaceholder = mtsMainPage.getPhonePlaceholder();
        String actualAmountPlaceholder = mtsMainPage.getAmountPlaceholder();

        assertEquals(expectedPhonePlaceholder, actualPhonePlaceholder);
        assertEquals(expectedAmountPlaceholder, actualAmountPlaceholder);
    }


    @Test
    @DisplayName("Проверка данных в окне оплаты")
    public void testPaymentAfterFormSubmission() {
        driver.findElement(By.id("connection-phone")).click();
        driver.findElement(By.id("connection-phone")).clear();
        driver.findElement(By.id("connection-phone")).sendKeys("(29)777-77-77");
        driver.findElement(By.id("connection-sum")).click();
        driver.findElement(By.id("connection-sum")).clear();
        driver.findElement(By.id("connection-sum")).sendKeys("100");
        new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.findElement(By.xpath("//form[@id='pay-connection']/button")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.switchTo().frame(wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//iframe[@class='bepaid-iframe']")))));
        assertEquals("100.00 BYN", driver.findElement(By.xpath("//div[@class='pay-description__cost']/span")).getAttribute("textContent"));
        assertEquals("Номер карты", driver.findElement(By.xpath("(//div[@class='content ng-tns-c46-1'])[1]")).getAttribute("textContent"));
        assertEquals("Оплата: Услуги связи\nНомер:375297777777", driver.findElement(By.xpath("//span[contains(text(),':')]")).getAttribute("textContent"));
        assertEquals("Срок действия", driver.findElement(By.xpath("(//div[@class='content ng-tns-c46-4'])[1]")).getAttribute("textContent"));
        assertEquals("CVC", driver.findElement(By.xpath("(//div[@class='content ng-tns-c46-5'])[1]")).getAttribute("textContent"));
        assertEquals("Имя держателя (как на карте)", driver.findElement(By.xpath("(//div[@class='content ng-tns-c46-3'])[1]")).getAttribute("textContent"));
    }
}

