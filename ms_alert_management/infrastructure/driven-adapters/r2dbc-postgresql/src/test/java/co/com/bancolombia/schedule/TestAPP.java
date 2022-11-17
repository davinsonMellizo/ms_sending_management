package co.com.bancolombia.schedule;

import co.com.bancolombia.campaign.data.CampaignData;
import co.com.bancolombia.campaign.data.CampaignMapper;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.campaign.Campaign;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class TestAPP {
    public static void main(String[] args) {
        run(co.com.bancolombia.provider.TestAPP.class, args);
    }

    @Bean
    public TimeFactory timeFactory() {
        return new TimeFactory();
    }

    @Bean
    public CampaignMapper campaignMapper(){
        return new CampaignMapper() {
            @Override
            public Campaign toEntity(CampaignData campaignData) {
                return Campaign.builder().build();
            }

            @Override
            public CampaignData toData(Campaign campaign) {
                return CampaignData.builder().build();
            }
        };
    }
}
