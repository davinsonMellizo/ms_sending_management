package co.com.bancolombia.usecase.alert;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AlertUseCaseTest {

   /* @InjectMocks
    private AlertUseCase useCase;

    @Mock
    private AlertGateway alertGateway;
    @Mock
    private StateGateway stateGateway;
    @Mock
    private EnrollmentContactGateway enrollmentGateway;
    @Mock
    private ContactMediumGateway mediumGateway;

    private final State state = new State(0, "Activo");
    private final ContactMedium medium = new ContactMedium(1, "Mail");
    private final EnrollmentContact enrollment = new EnrollmentContact(0, "ALM");
    private final Client client = new Client(new Long(1061772353), 0);
    private final Alert alert = new Alert();

    @BeforeEach
    public void init() {
        alert.setIdContactMedium(1);
        alert.setIdEnrollmentContact(0);
        alert.setDocumentNumber(new Long(1061772353));
        alert.setDocumentType(0);
        alert.setValue("correo@gamail.com");
        alert.setState("Activo");
    }

    @Test
    public void findAllContactByClient() {
        when(alertGateway.findAlertById(anyInt()))
                .thenReturn(Flux.just(alert));
        StepVerifier
                .create(useCase.findAlertById(1))
                .expectNextCount(1)
                .verifyComplete();
        verify(alertGateway).findAlertById(client);
    }

    @Test
    public void saveContact() {
        when(alertGateway.saveAlert(any()))
                .thenReturn(Mono.just(alert));
        when(stateGateway.findStateByName(any()))
                .thenReturn(Mono.just(state));
        when(enrollmentGateway.findEnrollmentContactByCode(any()))
                .thenReturn(Mono.just(enrollment));
        when(mediumGateway.findContactMediumByCode(any()))
                .thenReturn(Mono.just(medium));
        StepVerifier
                .create(useCase.saveAlert(alert))
                .assertNext(response -> response
                        .getDocumentNumber()
                        .equals(alert.getDocumentNumber()))
                .verifyComplete();
        verify(alertGateway).saveAlert(any());
        verify(enrollmentGateway).findEnrollmentContactByCode(any());
        verify(mediumGateway).findContactMediumByCode(any());
    }

    @Test
    public void updateContact() {
        when(alertGateway.updateAlert(any()))
                .thenReturn(Mono.just(alert));
        when(stateGateway.findStateByName(any()))
                .thenReturn(Mono.just(state));
        StepVerifier
                .create(useCase.updateAlert(alert))
                .assertNext(response -> response
                        .getAlerts().get(0).getDocumentNumber()
                        .equals(alert.getDocumentNumber()))
                .verifyComplete();
        verify(alertGateway).updateAlert(any());
        verify(stateGateway).findStateByName(any());
    }

    @Test
    public void deleteContact() {
        when(alertGateway.findIdContact(any()))
                .thenReturn(Mono.just(1));
        when(alertGateway.deleteAlert(any()))
                .thenReturn(Mono.just(1));
        StepVerifier.create(useCase.deleteAlert(alert))
                .expectNextCount(1)
                .verifyComplete();
        verify(alertGateway).findIdContact(alert);
        verify(alertGateway).deleteAlert(any());
    }

    @Test
    public void updateContactWithException() {
        when(alertGateway.updateAlert(any()))
                .thenReturn(Mono.empty());
        when(stateGateway.findStateByName(any()))
                .thenReturn(Mono.just(state));
        useCase.updateAlert(alert)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void deleteContactWithException() {
        when(alertGateway.findIdContact(any()))
                .thenReturn(Mono.empty());
        useCase.deleteAlert(alert)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }*/
}
