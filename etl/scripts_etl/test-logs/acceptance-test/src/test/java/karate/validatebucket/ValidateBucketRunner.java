package karate.validatebucket;

import com.intuit.karate.junit5.Karate;

public class ValidateBucketRunner {

    @Karate.Test
    Karate ValidateBucketRunner() {
        return Karate.run("validate_bucket.feature").relativeTo(getClass());
    }
}
