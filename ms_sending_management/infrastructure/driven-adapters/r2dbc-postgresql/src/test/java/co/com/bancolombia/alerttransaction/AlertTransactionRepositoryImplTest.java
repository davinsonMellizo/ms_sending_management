package co.com.bancolombia.alerttransaction;

import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AlertTransactionRepositoryImplTest {

    @Autowired
    private AlertTransactionRepositoryImplement repositoryImplement;
    private final AlertTransaction alert = new AlertTransaction();

    @BeforeEach
    public void init() {
        alert.setIdAlert("HGD");
        alert.setIdTransaction("0358");
        alert.setIdConsumer("ALM");
    }

    @Test
    void test() {

    }

}