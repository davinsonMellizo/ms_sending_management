package co.com.bancolombia.config;

import co.com.bancolombia.model.template.dto.TemplateResponse;
import co.com.bancolombia.usecase.gettemplate.GetTemplateUseCase;
import co.com.bancolombia.usecase.createtemplate.CreateTemplateUseCase;
import co.com.bancolombia.usecase.deletetemplate.DeleteTemplateUseCase;
import co.com.bancolombia.usecase.updatetemplate.UpdateTemplateUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import reactor.core.publisher.Mono;

@Configuration
//@ComponentScan(basePackages = "co.com.bancolombia.usecase",
//        includeFilters = {
//                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
//        },
//        useDefaultFilters = false)
public class UseCasesConfig {

        @Bean
        public TemplateRepository templateRepository() {
                return new TemplateRepository() {
                        @Override
                        public Mono<TemplateResponse> getTemplate(String idTemplate) {
                                return null;
                        }
                };
        }

        @Bean
        public GetTemplateUseCase getTemplateUseCase(TemplateRepository templateRepository){
                return new GetTemplateUseCase(templateRepository);
        }

        @Bean
        public CreateTemplateUseCase createTemplateUseCase(TemplateRepository templateRepository){
                return new CreateTemplateUseCase(templateRepository);
        }

        @Bean
        public UpdateTemplateUseCase updateTemplateUseCase(TemplateRepository templateRepository){
                return new UpdateTemplateUseCase(templateRepository);
        }

        @Bean
        public DeleteTemplateUseCase deleteTemplateUseCase(TemplateRepository templateRepository){
                return new DeleteTemplateUseCase(templateRepository);
        }
}
