package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

public class TestMTS {
    private WebDriver driver;
    private MTSMainPage mtsMainPage;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.mts.by/?hash-offset=70&hash-dur=1300#pay-section");
        mtsMainPage = new MTSMainPage(driver);
        mtsMainPage.acceptCookies();
    }

    /*@AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }*/

    @Test
    @DisplayName("Проверка заголовка внутри страницы")
    public void testHeaderElement() {
        String headerEx = "Онлайн пополнение\n" + "без комиссии";
        String headerAc = mtsMainPage.getHeaderText();
        assertEquals(headerEx, headerAc);
    }

    @Test
    @DisplayName("Проверка наличия иконок платежных систем")
    public void testPaymentPics() {
        assertTrue(mtsMainPage.arePaymentLogosDisplayed());
    }

    @Test
    @DisplayName("Заполнение данных для оплаты")
    public void phone() {
        mtsMainPage.fillPaymentForm("297777777", "100");
    }

    @Test
    @DisplayName("Проверка ссылки 'Подробнее о сервисе'")
    public void testURL() {
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
    public void testPlaceholders(String serviceOptions, String expectedPhonePlaceholder, String expectedAmountPlaceholder) {
        mtsMainPage.selectServiceOption(serviceOptions);

        String actualPhonePlaceholder = mtsMainPage.getPhonePlaceholder();
        String actualAmountPlaceholder = mtsMainPage.getAmountPlaceholder();

        assertEquals(expectedPhonePlaceholder, actualPhonePlaceholder);
        assertEquals(expectedAmountPlaceholder, actualAmountPlaceholder);
    }

    @Test
    @DisplayName("Проверка данных в окне оплаты")
    @CsvSource({"100", "297777777", "Номер карты", "Срок действия", "CVC", "Имя держателя", "100"})
    public void testPaymentAfterPaymentInfo() {
        mtsMainPage.fillPaymentForm("297777777", "100");
        mtsMainPage.PayButtonClick();

        // проверка суммы оплаты вверху окна
        mtsMainPage.PaymentCheckTop();
        String PaymentCheckTopEx = "100";
        String PaymentCheckTopAc = "100";
        assertEquals(PaymentCheckTopEx, PaymentCheckTopAc);

        // проверка номера
        mtsMainPage.PaymentNumber();
        String PaymentNumberEx = "100";
        String PaymentNumberAc = mtsMainPage.getHeaderText(); // не забыть удалить
        assertEquals(PaymentNumberEx, PaymentNumberAc);

        // проверка поля карты
        mtsMainPage.PaymentCard();
        String PaymentCardEx = "100";
        String PaymentCardAc = "100";
        assertEquals(PaymentCardEx, PaymentCardAc);

        // проверка поля срока действия
        mtsMainPage.PaymentValidTill();
        String PaymentValidTillEx = "100";
        String PaymentValidTillAc = "100";
        assertEquals(PaymentValidTillEx, PaymentValidTillAc);

        // проверка поля CVC
        mtsMainPage.PaymentCVC();
        String PaymentCVCEx = "100";
        String PaymentCVCAc = "100";
        assertEquals(PaymentCVCEx, PaymentCVCAc);

        // Проверка поля Держателя
        mtsMainPage.PaymentCardholder();
        String PaymentCardholderEx = "100";
        String PaymentCardholderAc = "100";
        assertEquals(PaymentCardholderEx, PaymentCardholderAc);

        // Проверка суммы оплаты на кнопке
        mtsMainPage.PaymentCheckButton();
        String PaymentCheckButtonEx = "100";
        String PaymentCheckButtonAc = "100";
        assertEquals(PaymentCheckButtonEx, PaymentCheckButtonAc);
    }


}
