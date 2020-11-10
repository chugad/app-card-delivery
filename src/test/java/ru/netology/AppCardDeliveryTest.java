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
    String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    @BeforeEach
    void shouldStartBeforeEachTest() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTestHappyPath() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $$("[data-test-id=date] input").get(0).setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин Олег");
        $("[data-test-id=phone] input").setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='notification']").waitUntil(text("Успешно!"), 15000).shouldBe(visible);
        $("[data-test-id='notification']").waitUntil(text("Встреча успешно забронирована на " + date), 15000).shouldBe(visible);
    }

    @Test
    void shouldTestPathIfEmptyCity() {
        $("[data-test-id=city] .input__control").setValue("");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин Олег");
        $("[data-test-id=phone] input").setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestPathIfCityIsNotCapital() {
        $("[data-test-id=city] .input__control").setValue("Бийск");     //город не столица региона
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин Олег");
        $("[data-test-id=phone] input").setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestPathDateEmpty() {

        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").setValue("");
        $("[data-test-id='name'] input").setValue("Дудин Олег");
        $("[data-test-id=phone] input").setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void shouldTestPathDateTwoDays() {
        String date2 = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").setValue(date2);
        $("[data-test-id='name'] input").setValue("Дудин Олег");
        $("[data-test-id=phone] input").setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldTestPathDateNow() {
        String date3 = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").setValue(date3);
        $("[data-test-id='name'] input").setValue("Дудин Олег");
        $("[data-test-id=phone] input").setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldTestPathIfEmptyName() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldBe(visible).shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestPathIfInvalidName() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Lelby_Jkeg$");
        $("[data-test-id=phone] input").setValue("+79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldBe(visible).shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestPathIfValidPhoneNumberWithoutPlus() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин олег");
        $("[data-test-id=phone] input").setValue("79995552211");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldBe(visible).shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestPathIfInvalidPhoneUnderLimitDown() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин олег");
        $("[data-test-id=phone] input").setValue("799");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldBe(visible).shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestPathIfInvalidPhoneAfterLimit() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин олег");
        $("[data-test-id=phone] input").setValue("+799988811001");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldBe(visible).shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestPathIfEmptyPhoneNumber() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин олег");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestPathWithOutCheckbox() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Дудин олег");
        $("[data-test-id=phone] input").setValue("+79998881122");
        $(byText("Забронировать")).click();
        $("[data-test-id='agreement'].input_invalid [role]").shouldBe(visible).shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
