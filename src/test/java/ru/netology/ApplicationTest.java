package ru.netology;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.Data.*;

public class ApplicationTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        Configuration.browserCapabilities = options;
    }


    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");

        SelenideElement form = $("form");

        form.$("[data-test-id=login] input").setValue(registeredUser.getLogin());
        form.$("[data-test-id=password] input").setValue(registeredUser.getPassword());
        form.$("[data-test-id=action-login]").click();

        $(".heading").shouldHave(text("Личный кабинет"));

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");

        SelenideElement form = $("form");
        form.$("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        form.$("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        form.$("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");

        SelenideElement form = $("form");

        form.$("[data-test-id=login] input").setValue(blockedUser.getLogin());
        form.$("[data-test-id=password] input").setValue(blockedUser.getPassword());
        form.$("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        SelenideElement form = $("form");
        form.$("[data-test-id=login] input").setValue(wrongLogin);
        form.$("[data-test-id=password] input").setValue(registeredUser.getPassword());
        form.$("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();


        SelenideElement form = $("form");
        form.$("[data-test-id=login] input").setValue(registeredUser.getLogin());
        form.$("[data-test-id=password] input").setValue(wrongPassword);
        form.$("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}