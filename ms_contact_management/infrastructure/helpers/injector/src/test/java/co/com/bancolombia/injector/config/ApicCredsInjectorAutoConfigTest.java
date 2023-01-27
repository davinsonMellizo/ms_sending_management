package co.com.bancolombia.injector.config;

import co.com.bancolombia.d2b.model.secret.AsyncSecretVault;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApicCredsInjectorAutoConfigTest {
    private ApicCredsInjectorAutoConfig apicCredsInjectorAutoConfig;
    @Mock
    private AsyncSecretVault asyncSecretVault;

   @Test
   void apicCredsFilterFunctionTest(){
       apicCredsInjectorAutoConfig = new ApicCredsInjectorAutoConfig();
       assertNotNull(apicCredsInjectorAutoConfig.apicCredsFilterFunction(asyncSecretVault, "secretName"));
   }
}
