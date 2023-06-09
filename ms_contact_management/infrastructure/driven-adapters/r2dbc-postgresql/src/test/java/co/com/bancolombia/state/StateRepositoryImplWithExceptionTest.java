package co.com.bancolombia.state;


import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.state.data.StateMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class StateRepositoryImplWithExceptionTest {

    @InjectMocks
    private StateRepositoryImplement stateRepositoryImplement;
    @Mock
    private StateRepository repository;
    @Spy
    private StateMapper mapper = Mappers.getMapper(StateMapper.class);


    @Test
    void findStateByNameWithException() {
        when(repository.findState(anyString()))
                .thenReturn(Mono.error(RuntimeException::new));
        stateRepositoryImplement.findState("Active")
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

}