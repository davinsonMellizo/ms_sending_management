package co.com.bancolombia.ibmmq.model;

import lombok.Data;

@Data
public class ConnectionDTO {
    private String name;
    private String qmGroup;
    private String temporaryQueueModel;
    private String secret;
    private String ccdtBucketName;
    private String jksBucketName;
    private String applicationName;
    private String ccdtPath;
    private String jksPath;
    private int outputConcurrency;
    private int replyTimeOut;
}
