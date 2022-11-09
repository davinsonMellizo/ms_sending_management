package co.com.bancolombia.cronexpression;

import co.com.bancolombia.commons.enums.ScheduleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@ExtendWith(MockitoExtension.class)
class CronExpressionTest {
    @InjectMocks
    CronExpression cronExpression;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startAndEndTime;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.parse("2022-10-07", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        startAndEndTime = LocalTime.parse("08:00:00.00", DateTimeFormatter.ofPattern("HH:mm:ss.SS"));
        endDate = LocalDate.parse("2022-12-12", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Test
    void hourlySuccess() {
        String cron = cronExpression.dateToCron(ScheduleType.HOURLY, startDate, startAndEndTime, endDate);
        Assertions.assertEquals("cron(0 8/1 7-12 10-12 ? 2022)", cron);
    }

    @Test
    void hourlySuccessNotEnd() {
        String cron = cronExpression.dateToCron(ScheduleType.HOURLY, startDate, startAndEndTime, null);
        Assertions.assertEquals("cron(0 8/1 * * ? *)", cron);
    }

    @Test
    void onDemandSuccess() {
        String cron = cronExpression.dateToCron(ScheduleType.ON_DEMAND, startDate, startAndEndTime, endDate);
        Assertions.assertNull(cron);
    }

    @Test
    void onTimeSuccess() {
        String cron = cronExpression.dateToCron(ScheduleType.ONE_TIME, startDate, startAndEndTime, null);
        Assertions.assertEquals("cron(0 8 7 10 ? 2022)", cron);
    }

    @Test
    void dailySuccess() {
        String cron = cronExpression.dateToCron(ScheduleType.DAILY, startDate, startAndEndTime, endDate);
        Assertions.assertEquals("cron(0 8 7-12 10-12 ? 2022)", cron);
    }

    @Test
    void dailySuccess2() {
        LocalDate endDate = LocalDate.parse("2022-12-07", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String cron = cronExpression.dateToCron(ScheduleType.DAILY, startDate, startAndEndTime, endDate);
        Assertions.assertEquals("cron(0 8 7-7 10-12 ? 2022)", cron);
    }

    @Test
    void dailySuccessNoEnd() {
        String cron = cronExpression.dateToCron(ScheduleType.DAILY, startDate, startAndEndTime, null);
        Assertions.assertEquals("cron(0 8 * * ? *)", cron);
    }

    @Test
    void weeklySuccess() {
        String cron = cronExpression.dateToCron(ScheduleType.WEEKLY, startDate, startAndEndTime, endDate);
        Assertions.assertEquals("cron(0 8 * 10-12 FRI 2022)", cron);
    }

    @Test
    void weeklySuccessNoEnd() {
        String cron = cronExpression.dateToCron(ScheduleType.WEEKLY, startDate, startAndEndTime, null);
        Assertions.assertEquals("cron(0 8 * * FRI *)", cron);
    }

    @Test
    void monthlySuccess() {
        String cron = cronExpression.dateToCron(ScheduleType.MONTHLY, startDate, startAndEndTime, endDate);
        Assertions.assertEquals("cron(0 8 7 10-12 ? 2022)", cron);
    }

    @Test
    void monthlySuccessTwoYear() {
        endDate = LocalDate.parse("2023-01-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String cron = cronExpression.dateToCron(ScheduleType.MONTHLY, startDate, startAndEndTime, endDate);
        Assertions.assertEquals("cron(0 8 7 10-1 ? 2022-2023)", cron);
    }

    @Test
    void monthlySuccessNoEnd() {
        String cron = cronExpression.dateToCron(ScheduleType.MONTHLY, startDate, startAndEndTime, null);
        Assertions.assertEquals("cron(0 8 7 * ? *)", cron);
    }
}