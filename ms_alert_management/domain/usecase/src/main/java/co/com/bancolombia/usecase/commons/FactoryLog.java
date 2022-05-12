package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.newness.Newness;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.Response.SUCCESS_120;

@UtilityClass
public class FactoryLog {

    public Mono<Newness> createLog(AlertClient alertClient, String transaction) {
        return Mono.just(Newness.builder()
                .documentType(alertClient.getDocumentType())
                .documentNumber(alertClient.getDocumentNumber())
                .amountEnable(alertClient.getAmountEnable())
                .numberOperations(alertClient.getNumberOperations())
                .channelTransaction(alertClient.getAssociationOrigin())
                .idAlert(alertClient.getIdAlert())
                .descriptionAlert(alertClient.getAlertDescription())
                .transactionDescription(transaction)
                .responseCode(SUCCESS_120.getCode())
                .responseDescription(SUCCESS_120.getDescription())
                .userCreation(alertClient.getCreationUser())
                .build());
    }
}
