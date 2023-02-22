package co.com.bancolombia.drivenadapters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TimeFactoryTest {
    @InjectMocks
    private TimeFactory timeFactory;

    @Test
    void nowTest(){
        Assertions.assertNotNull(timeFactory.now());
    }
}
