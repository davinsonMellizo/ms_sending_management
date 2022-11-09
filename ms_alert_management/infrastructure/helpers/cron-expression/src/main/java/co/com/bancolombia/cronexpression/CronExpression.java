package co.com.bancolombia.cronexpression;

import co.com.bancolombia.commons.enums.ScheduleType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class CronExpression {
    public String dateToCron(ScheduleType scheduleType, LocalDate startDate, LocalTime startTime, LocalDate endDate) {
        return !ScheduleType.ON_DEMAND.equals(scheduleType) ?
                getCronExpression(scheduleType, startDate, startTime, endDate)
                : null;
    }

    private String getCronExpression(ScheduleType scheduleType, LocalDate startDate, LocalTime startTime, LocalDate endDate) {
        return "cron(" + startTime.getMinute() + " " +
                hourCron(scheduleType, startTime) + " " +
                dayCron(scheduleType, startDate, endDate) + " " +
                monthCron(scheduleType, startDate, endDate) + " " +
                dayWeek(scheduleType, startDate) + " " +
                yearCron(scheduleType, startDate, endDate) + ")";
    }

    private String yearCron(ScheduleType scheduleType, LocalDate startDate, LocalDate endDate) {
        String endYear = endDate != null && endDate.getYear() != startDate.getYear() ? "-" + endDate.getYear() : "";
        String cron = endDate != null ? startDate.getYear() + endYear : "*";
        return ScheduleType.ONE_TIME.equals(scheduleType) ? String.valueOf(startDate.getYear()) : cron;
    }

    private String dayWeek(ScheduleType scheduleType, LocalDate startDate) {
        return ScheduleType.WEEKLY.equals(scheduleType) ? startDate.getDayOfWeek().toString().substring(0, 3) : "?";
    }

    private String hourCron(ScheduleType scheduleType, LocalTime startTime) {
        return ScheduleType.HOURLY.equals(scheduleType) ? startTime.getHour() + "/1" : String.valueOf(startTime.getHour());
    }

    private String dayCron(ScheduleType scheduleType, LocalDate startDate, LocalDate endDate) {
        String cron = (ScheduleType.HOURLY.equals(scheduleType) && endDate != null) ||
                (ScheduleType.DAILY.equals(scheduleType) && endDate != null) ?
                startDate.getDayOfMonth() + "-" + endDate.getDayOfMonth() : "*";

        return ScheduleType.ONE_TIME.equals(scheduleType) || ScheduleType.MONTHLY.equals(scheduleType) ?
                String.valueOf(startDate.getDayOfMonth()) : cron;
    }

    private String monthCron(ScheduleType scheduleType, LocalDate startDate, LocalDate endDate) {
        if (ScheduleType.ONE_TIME.equals(scheduleType)) {
            return String.valueOf(startDate.getMonthValue());
        } else {
            if (endDate == null) {
                return "*";
            } else {
                return startDate.getMonthValue() + (
                        endDate.getMonthValue() != startDate.getMonthValue() ? "-" + endDate.getMonthValue() : ""
                );
            }
        }
    }

}
