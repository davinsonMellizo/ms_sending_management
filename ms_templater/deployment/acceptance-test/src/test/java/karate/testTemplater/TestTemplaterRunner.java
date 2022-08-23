package karate.testTemplater;

import com.intuit.karate.junit5.Karate;

public class TestTemplaterRunner {

        @Karate.Test
        Karate GestionarUsuariosRunner(){
            return Karate.run("test_templater.feature").relativeTo(getClass());
        }
}
