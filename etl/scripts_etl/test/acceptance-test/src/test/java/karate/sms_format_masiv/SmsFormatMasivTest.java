package karate.sms_format_masiv;

import com.intuit.karate.junit5.Karate;

public class SmsFormatMasivTest {

    @Karate.Test
    Karate checkSmsFormat() {
        return Karate.run("sms_format_masiv.feature").relativeTo(getClass());
    }
}
