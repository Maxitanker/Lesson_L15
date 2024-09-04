package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.qameta.allure.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Feature("Тесты для проверки страницы MTS")
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
    @Description("Проверка правильности отображения заголовка на главной странице MTS")
    @Severity(SeverityLevel.MINOR)
    public void testHeaderElement() {
        String expectedHeader = "Онлайн пополнение\nбез комиссии";
        String actualHeader = mtsMainPage.getHeaderText();
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    @DisplayName("Проверка наличия иконок платежных систем")
    @Description("Проверка отображения всех логотипов платежных систем на главной странице")
    @Severity(SeverityLevel.NORMAL)
    public void testPaymentLogosDisplayed() {
        assertTrue(mtsMainPage.arePaymentLogosDisplayed(), "Логотипы платежных систем не отображаются.");
    }

    @Test
    @DisplayName("Заполнение данных для оплаты")
    @Description("Заполнение формы оплаты на странице MTS")
    @Severity(SeverityLevel.BLOCKER)
    public void testFillPaymentForm() {
        mtsMainPage.fillPaymentForm("297777777", "100");
    }

    @Test
    @DisplayName("Проверка ссылки 'Подробнее о сервисе'")
    @Description("Проверка работы ссылки 'Подробнее о сервисе' на главной странице")
    @Severity(SeverityLevel.NORMAL)
    public void testMoreInfoLink() {
        mtsMainPage.clickMoreInfoLink();
    }

    @ParameterizedTest
    @CsvSource({
            "услуги связи, Номер телефона, Сумма",
            "домашний интернет, Номер телефона, Сумма",
            "рассрочка, Номер телефона, Сумма",
            "задолженность, Номер телефона, Сумма"
    })
    @DisplayName("Проверка надписей в незаполненных полях")
    @Description("Проверка отображения плейсхолдеров в незаполненных полях для различных типов услуг")
    @Severity(SeverityLevel.NORMAL)
    public void testPlaceholders(String serviceOption, String expectedPhonePlaceholder, String expectedAmountPlaceholder) {
        mtsMainPage.selectServiceOption(serviceOption);

        String actualPhonePlaceholder = mtsMainPage.getPhonePlaceholder();
        String actualAmountPlaceholder = mtsMainPage.getAmountPlaceholder();

        assertEquals(expectedPhonePlaceholder, actualPhonePlaceholder);
        assertEquals(expectedAmountPlaceholder, actualAmountPlaceholder);
    }

    @Test
    @DisplayName("Проверка данных в окне оплаты")
    @Description("Проверка правильности данных в окне подтверждения оплаты")
    @Severity(SeverityLevel.BLOCKER)
    public void testPaymentAfterFormSubmission() {
        driver.findElement(By.id("connection-phone")).click();
        driver.findElement(By.id("connection-phone")).clear();
        driver.findElement(By.id("connection-phone")).sendKeys("(29)777-77-77");
        driver.findElement(By.id("connection-sum")).click();
        driver.findElement(By.id("connection-sum")).clear();
        driver.findElement(By.id("connection-sum")).sendKeys("100");
        driver.findElement(By.xpath("//form[@id='pay-connection']/button")).click();
        driver.switchTo().frame(1);
        assertEquals("100.00 BYN", driver.findElement(By.className("pay-description__cost")).getText());
        assertEquals("Номер карты", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='или используйте карту'])[1]/following::div[5]")).getText());
        assertEquals("Оплата: Услуги связи\nНомер:375297777777", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='или используйте карту'])[1]/preceding::span[2]")).getText());
        assertEquals("Номер карты", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='или используйте карту'])[1]/following::div[3]")).getText());
        assertEquals("Срок действия", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Номер карты'])[1]/following::div[9]")).getText());
        assertEquals("CVC", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Срок действия'])[1]/following::div[4]")).getText());
        assertEquals("Имя держателя (как на карте)", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='CVC'])[1]/following::div[4]")).getText());
        assertEquals("Имя держателя (как на карте)", driver.findElement(By.className("colored disabled")).getText());
    }
}
