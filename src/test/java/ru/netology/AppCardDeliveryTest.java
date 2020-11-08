package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class AppCardDeliveryTest {
    @BeforeEach
    void shouldStartBeforeEachTest(){
        open("http://localhost:9999");
    }
    @Test
        void shouldTestHappyPath() {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $$("[type='tel']").get(0).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[type='tel']").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин Олег");
        $$("[type='tel']").get(1).setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='notification']").waitUntil(text("Успешно!"), 15000).shouldBe(visible);
    }
    @Test
    void shouldTestPathIfEmptyCity() {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("");
        $$("[type='tel']").get(0).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[type='tel']").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин Олег");
        $$("[type='tel']").get(1).setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }
    @Test
    void shouldTestPathIfCityIsNotCapital() {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Бийск");     //город не столица региона
        $$("[type='tel']").get(0).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[type='tel']").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин Олег");
        $$("[type='tel']").get(1).setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }
    @Test
    void shouldTestPathDateTwoDays() {
        String date = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $$("[type='tel']").get(0).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[type='tel']").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин Олег");
        $$("[type='tel']").get(1).setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }
    @Test
    void shouldTestPathDateNow() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $$("[type='tel']").get(0).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[type='tel']").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин Олег");
        $$("[type='tel']").get(1).setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }
    @Test
    void shouldTestPathIfEmptyName() {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $$("[type='tel']").get(0).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[type='tel']").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("");
        $$("[type='tel']").get(1).setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldBe(visible).shouldHave(exactText("Поле обязательно для заполнения"));
    }
    @Test
    void shouldTestPathIfInvalidName() {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $$("[type='tel']").get(0).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[type='tel']").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("Lelby_Jkeg$");
        $$("[type='tel']").get(1).setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        // $("[data-test-id='name'] .input_invalid .input__sub") почему-то не ищет, либо искать без data-test-id
        $("[data-test-id='name'] .input__sub").shouldBe(visible).shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }
    @Test
    void shouldTestPathIfValidPhoneNumberWithoutPlus() {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $$("[type='tel']").get(0).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[type='tel']").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин олег");
        $$("[type='tel']").get(1).setValue("79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        // $("[data-test-id='phone'] .input_invalid .input__sub") почему-то не ищет, либо искать без data-test-id
        $("[data-test-id='phone'] .input__sub").shouldBe(visible).shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }
    @Test
    void shouldTestPathIfInvalidPhoneUnderLimitDown() {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $$("[type='tel']").get(0).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[type='tel']").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин олег");
        $$("[type='tel']").get(1).setValue("799");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        // $("[data-test-id='phone'] .input_invalid .input__sub") почему-то не ищет, либо искать без data-test-id
        $("[data-test-id='phone'] .input__sub").shouldBe(visible).shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }
    @Test
    void shouldTestPathIfInvalidPhoneAfterLimit() {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $$("[type='tel']").get(0).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[type='tel']").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин олег");
        $$("[type='tel']").get(1).setValue("+799988811001");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        // $("[data-test-id='phone'] .input_invalid .input__sub") почему-то не ищет, либо искать без data-test-id
        $("[data-test-id='phone'] .input__sub").shouldBe(visible).shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }
    @Test
    void shouldTestPathIfEmptyPhoneNumber() {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $$("[type='tel']").get(0).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[type='tel']").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин олег");
        $$("[type='tel']").get(1).setValue("");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        // $("[data-test-id='phone'] .input_invalid .input__sub") почему-то не ищет, либо искать без data-test-id
        $("[data-test-id='phone'] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }
    @Test
    void shouldTestPathWithOutCheckbox() {
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $$("[type='tel']").get(0).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[type='tel']").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин олег");
        $$("[type='tel']").get(1).setValue("+79998881122");
        $(byText("Забронировать")).click();
        $("[data-test-id='agreement'].input_invalid [role]").shouldBe(visible).shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
