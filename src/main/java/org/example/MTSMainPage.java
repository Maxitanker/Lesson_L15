package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MTSMainPage extends BasePage {

    // Локаторы
    private By cookieAgreeButton = By.id("cookie-agree");
    private By headerElement = By.xpath("//*[@id='pay-section']/div/div/div[1]/section/div/h2");
    private By paymentLogos = By.className("pay__partners");
    private By phoneField = By.id("connection-phone");
    private By amountField = By.id("connection-sum");
    private By submitButton = By.cssSelector("#pay-connection > button");
    private By moreInfoLink = By.linkText("Подробнее о сервисе");
    private By serviceDropdown = By.className("select__header");
    /*private By serviceOptions = By.className("select__header");
    private By payButton = By.xpath("//*[@id=\"pay-internet\"]/button");
    private By paymentCheckTop = By.className("pay-description__cost");
    private By paymentNumber = By.xpath("/html/body/app-root/div/div/div/app-payment-container/section/div/div/div[2]/span");
    private By paymentCard = By.id("cc-number");
    private By paymentValidTill = By.name("expirationDate");
    private By paymentCVC = By.name("verification_value");
    private By paymentCardholder = By.name("cc-name");
    private By paymentCheckButton = By.cssSelector(".colored.disabled");*/

    public MTSMainPage(WebDriver driver) {
        super(driver);
    }

    // Методы
    private WebElement waitForElement(By locator) {
        return new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(driver -> driver.findElement(locator));
    }
    public void acceptCookies() {
        try {
            WebElement button = waitForElement(cookieAgreeButton);
            if (button.isDisplayed() && button.isEnabled()) {
                button.click();
                System.out.println("Окно с куками закрыто");
            }
        } catch (Exception e) {
            System.out.println("Кнопка для принятия куки не найдена или не отображается.");
        }
    }


    public String getHeaderText() {
        return waitForElement(headerElement).getText();
    }

    public boolean arePaymentLogosDisplayed() {
        return waitForElement(paymentLogos).isDisplayed();
    }

    public void fillPaymentForm(String phoneNumber, String amount) {
        WebElement phone = waitForElement(phoneField);
        phone.sendKeys(phoneNumber);

        WebElement amountFld = waitForElement(amountField);
        amountFld.sendKeys(amount);

        waitForElement(submitButton).click();
    }

    public void clickMoreInfoLink() {
        waitForElement(moreInfoLink).click();
    }

    // выбор списка опций
    public void selectServiceOption(String optionName) {
        waitForElement(serviceDropdown).click();

        List<WebElement> options = driver.findElements(By.className("select__option"));

        for (WebElement option : options) {
            if (option.getText().equalsIgnoreCase(optionName)) {
                option.click();
                break;
            }
        }
    }

    public String getPhonePlaceholder() {
        return waitForElement(phoneField).getAttribute("placeholder");
    }

    public String getAmountPlaceholder() {
        return waitForElement(amountField).getAttribute("placeholder");
    }

    /*public void payButtonClick() {
        waitForElement(payButton).click()
    }

    public String getPaymentCheckTopText() {
        return waitForElement(paymentCheckTop)
    }

    public String getPaymentNumberText() {
        return waitForElement(paymentNumber)
    }

    public String getPaymentCardPlaceholder() {
        return waitForElement(paymentCard)
    }

    public String getPaymentValidTillPlaceholder() {
        return waitForElement(paymentValidTill)
    }

    public String getPaymentCVCPlaceholder() {
        return waitForElement(paymentCVC)
    }

    public String getPaymentCardholderPlaceholder() {
        return waitForElement(paymentCardholder)
    }

    public String getPaymentCheckButtonText() {
        return waitForElement(paymentCheckButton)
    }*/


}
