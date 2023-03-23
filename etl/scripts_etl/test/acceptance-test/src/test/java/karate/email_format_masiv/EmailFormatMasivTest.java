package karate.email_format_masiv;

import com.intuit.karate.junit5.Karate;

public class EmailFormatMasivTest {
    @Karate.Test
    Karate checkEmailFormat() {
        return Karate.run("email_format_masiv.feature").relativeTo(getClass());
    }
}
