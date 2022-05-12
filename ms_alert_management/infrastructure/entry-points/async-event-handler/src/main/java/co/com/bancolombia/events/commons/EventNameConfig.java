package co.com.bancolombia.events.commons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventNameConfig {
    public static final String SEND_CREATE_PROVIDER = "send.create.provider";

    public static final String SEND_UPDATE_PROVIDER = "send.update.provider";
    public static final String SEND_CREATE_REMITTER = "send.create.remitter";
    public static final String SEND_UPDATE_REMITTER = "send.update.remitter";
    public static final String SEND_CREATE_ALERT = "send.create.alert";
    public static final String SEND_UPDATE_ALERT = "send.update.alert";
    public static final String SEND_CREATE_ALERT_TRX = "send.create.alert.trx";
    public static final String SEND_DELETE_ALERT_TRX = "send.delete.alert.trx";
    public static final String SEND_CREATE_CONSUMER = "send.create.consumer";
    public static final String SEND_UPDATE_CONSUMER = "send.update.consumer";
}
