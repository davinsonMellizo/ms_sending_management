package co.com.bancolombia.document;


import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.document.data.DocumentMapper;
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
public class DocumentRepositoryImplWithExceptionTest {

    @InjectMocks
    private DocumentRepositoryImplement documentRepositoryImplement;
    @Mock
    private DocumentRepository repository;
    @Spy
    private DocumentMapper mapper = Mappers.getMapper(DocumentMapper.class);


    @Test
    void findStateByNameWithException() {
        when(repository.getDocument(anyString()))
                .thenReturn(Mono.error(RuntimeException::new));
        documentRepositoryImplement.getDocument("CC")
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

}