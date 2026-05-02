package tests.testdata;

import com.github.javafaker.Faker;
import java.util.Locale;

public class TestData {
    static Faker faker = new Faker(new Locale("en"));
    public static String regularUserName= "Agzamurai",
            regularUserPassword = "Qwer1234",
            wrongPassword = "Qwer4321",
            getRandomUserName = faker.name().firstName(),
            getRandomUserPassword = faker.internet().password(4, 6);

}
