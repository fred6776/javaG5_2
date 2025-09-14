import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        //  добавить логику теста, в рамках которого будет выполнена попытка входа в личный кабинет с учётными
        //  данными зарегистрированного активного пользователя, для заполнения полей формы используйте
        //  пользователя registeredUser
        Selenide.$("[data-test-id=login] input").setValue(registeredUser.getLogin());
        Selenide.$("[data-test-id=password] input").setValue(registeredUser.getPassword());
        Selenide.$("[data-test-id=action-login]").click();
        Selenide.$(byText("Личный кабинет")).click();
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        //  добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет
        //  незарегистрированного пользователя, для заполнения полей формы используйте пользователя notRegisteredUser
        Selenide.$("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        Selenide.$("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        Selenide.$("[data-test-id=action-login]").click();
        Selenide.$("[data-test-id=error-notification]").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        //  добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет,
        //  заблокированного пользователя, для заполнения полей формы используйте пользователя blockedUser
        Selenide.$("[data-test-id=login] input").setValue(blockedUser.getLogin());
        Selenide.$("[data-test-id=password] input").setValue(blockedUser.getPassword());
        Selenide.$("[data-test-id=action-login]").click();
        Selenide.$("[data-test-id=error-notification]").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        // добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  логином, для заполнения поля формы "Логин" используйте переменную wrongLogin,
        //  "Пароль" - пользователя registeredUser
        Selenide.$("[data-test-id=login] input").setValue(wrongLogin);
        Selenide.$("[data-test-id=password] input").setValue(registeredUser.getPassword());
        Selenide.$("[data-test-id=action-login]").click();
        Selenide.$("[data-test-id=error-notification]").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        //  добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  паролем, для заполнения поля формы "Логин" используйте пользователя registeredUser,
        //  "Пароль" - переменную wrongPassword
        Selenide.$("[data-test-id=login] input").setValue(registeredUser.getLogin());
        Selenide.$("[data-test-id=password] input").setValue(wrongPassword);
        Selenide.$("[data-test-id=action-login]").click();
        Selenide.$("[data-test-id=error-notification]").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}