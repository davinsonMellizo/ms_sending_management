package karate.sms_format_ina;

import com.intuit.karate.junit5.Karate;

public class SmsFormatInaTest {
    @Karate.Test
    Karate checkSmsFormat() {
        return Karate.run("sms_format_ina.feature").relativeTo(getClass());
    }
}
