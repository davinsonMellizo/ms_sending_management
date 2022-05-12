package co.com.bancolombia.ibmmq.model;

import lombok.Data;

@Data
public class Transaction {
    String listener;
    String template;
    String target;
    String queue;
}
