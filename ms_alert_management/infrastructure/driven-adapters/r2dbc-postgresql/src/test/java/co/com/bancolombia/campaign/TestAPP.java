package co.com.bancolombia.campaign;

import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.schedule.Schedule;
import co.com.bancolombia.model.schedule.gateways.ScheduleGateway;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class TestAPP {
    public static void main(String[] args) {
        run(co.com.bancolombia.category.TestAPP.class, args);
    }

    @Bean
    public TimeFactory timeFactory() {
        return new TimeFactory();
    }

    @Bean
    public ScheduleGateway scheduleGateway() {
        return new ScheduleGateway() {
            @Override
            public Mono<Schedule> saveSchedule(Schedule schedule) {
                return Mono.just(schedule);
            }

            @Override
            public Mono<StatusResponse<Schedule>> updateSchedule(Schedule schedule, Long id) {
                return Mono.just(StatusResponse.<Schedule>builder().actual(schedule).build());
            }

            @Override
            public Mono<Schedule> findScheduleById(Long id) {
                return Mono.just(Schedule.builder().build());
            }

            @Override
            public Mono<Campaign> saveSchedulesByCampaign(Campaign campaign) {
                return Mono.just(campaign);
            }

            @Override
            public Mono<Campaign> updateSchedulesByCampaign(Campaign campaign) {
                return Mono.just(campaign);
            }

            @Override
            public Mono<Campaign> findSchedulesByCampaign(Campaign campaign) {
                return Mono.just(campaign);
            }
        };
    }
}
