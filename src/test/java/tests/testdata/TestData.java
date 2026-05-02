package tests.testdata;

import com.github.javafaker.Faker;
import java.util.Locale;

public class TestData {
    public static Faker faker = new Faker(new Locale("en"));
    public static final String regularUserName= "Agzamurai",
            regularUserPassword = "Qwer1234",
            wrongPassword = "Qwer4321",
            prefix_jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

    public static String returnRandomUsername() {
        return faker.name().username();
    }

    public static String returnRandomPassword() {
        return faker.internet().password();
    }

    public String randomPassword = faker.internet().password();
    public String randomUsername = faker.name().username();
    public String randomEmail = faker.internet().emailAddress();
    public String randomFirstName = faker.name().firstName();
    public String randomLastName = faker.name().lastName();
}
