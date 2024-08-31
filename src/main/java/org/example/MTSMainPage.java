package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class MTSMainPage extends BasePage {

    @FindBy(id = "cookie-agree")
    private WebElement cookieAgreeButton;

    @FindBy(xpath = "//*[@id='pay-section']/div/div/div[1]/section/div/h2")
    private WebElement headerElement;

    @FindBy(className = "pay__partners")
    private WebElement paymentLogos;

    @FindBy(id = "connection-phone")
    private WebElement phoneField;

    @FindBy(id = "connection-sum")
    private WebElement amountField;

    @FindBy(css = "#pay-connection > button")
    private WebElement submitButton;

    @FindBy(linkText = "Подробнее о сервисе")
    private WebElement moreInfoLink;

    @FindBy(className = "select__header")
    private WebElement serviceDropdown;

    @FindBy(css = "select__header")
    private List<WebElement> serviceOptions;

    @FindBy(xpath = "//*[@id=\"pay-internet\"]/button")
    private WebElement PayButton;

    @FindBy(className = "pay-description__cost")
    private WebElement PaymentCheckTop;

    @FindBy(xpath = "/html/body/app-root/div/div/div/app-payment-container/section/div/div/div[2]/span")
    private WebElement PaymentNumber;

    @FindBy(id = "cc-number")
    private WebElement PaymentCard;

    @FindBy(name = "expirationDate")
    private WebElement PaymentValidTill;

    @FindBy(name = "verification_value")
    private WebElement PaymentCVC;

    @FindBy(name = "cc-name")
    private WebElement PaymentCardholder;

    @FindBy(className = "colored disabled")
    private WebElement PaymentCheckButton;


    public MTSMainPage(WebDriver driver) {
        super(driver);
    }

    public void acceptCookies() {
        try {
            if (cookieAgreeButton.isDisplayed() && cookieAgreeButton.isEnabled()) {
                waitAndClick(cookieAgreeButton);
                System.out.println("Окно с куками закрыто");
            }
        } catch (Exception e) {
            System.out.println("Кнопка для принятия куки не найдена или не отображается.");
        }
    }

    public String getHeaderText() {
        waitForElement(headerElement);
        return headerElement.getText();
    }

    public boolean arePaymentLogosDisplayed() {
        waitForElement(paymentLogos);
        return paymentLogos.isDisplayed();
    }

    public void fillPaymentForm(String phoneNumber, String amount) {
        phoneField.sendKeys(phoneNumber);
        amountField.sendKeys(amount);
        submitButton.click();
    }

    public void clickMoreInfoLink() {
        waitAndClick(moreInfoLink);
    }

    public void selectServiceOption(String optionName) {
        // Открываем выпадающий список
        waitAndClick(serviceDropdown);

        // Проходим по списку опций и выбираем нужную
        for (WebElement option : serviceOptions) {
            if (option.getText().equalsIgnoreCase("домашний интернет")) {
                option.click();
                break;
            }
        }
    }

    public String getPhonePlaceholder() {
        return phoneField.getAttribute("placeholder");
    }

    public String getAmountPlaceholder() {
        return amountField.getAttribute("placeholder");
    }

    public void PayButtonClick() {
        PayButton.click();
    }

    public String PaymentCheckTop() {
        return PaymentCheckTop.getAttribute("span"); //починить
    }

    // проверка номера
    public String PaymentNumber(); {
        return PaymentNumber.getAttribute("span"); //починить
    }

    // проверка поля карты
    public void PaymentCard(); {
        return PaymentCard.getAttribute("placeholder");
    }

    // проверка поля срока действия
    public void PaymentValidTill(); {
        return PaymentValidTill.getAttribute("placeholder");
    }

    // проверка поля CVC
    public void PaymentCVC(); {
        return PaymentCVC.getAttribute("placeholder");
    }

    // Проверка поля Держателя
    public void PaymentCardholder(); {
        return PaymentCardholder.getAttribute("placeholder");
    }

    // Проверка суммы оплаты на кнопке
    public void PaymentCheckButton(); {
        return PaymentCheckButton.getAttribute("placeholder");
    }
}