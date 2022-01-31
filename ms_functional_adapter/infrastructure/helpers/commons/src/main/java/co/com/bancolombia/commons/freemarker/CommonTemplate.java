package co.com.bancolombia.commons.freemarker;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.commons.freemarker.config.FreemarkerConfig;
import co.com.bancolombia.commons.utils.JsonUtils;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_FREEMARKER_ERROR;


public class CommonTemplate {
    private static final String UTIL = "JsonUtil";
    private Template template;
    private Map<String, Object> map;

    private CommonTemplate(String stringTemplate){
        try {
            Configuration config = new FreemarkerConfig(Configuration.VERSION_2_3_30);
            template = new Template(UUID.randomUUID().toString(), new StringReader(stringTemplate), config);
            this.map = new HashMap<>();
            TemplateHashModel model = new BeansWrapperBuilder(Configuration.VERSION_2_3_30).build().getStaticModels();
            map.put(UTIL, model.get(JsonUtils.class.getName()));
        }catch (IOException | TemplateModelException exception) {
            throw new TechnicalException(exception, TECHNICAL_FREEMARKER_ERROR);
        }
    }

    public static Mono<CommonTemplate> create(String template) {
        return Mono.just(new CommonTemplate(template));
    }

    public Mono<String> process(Object data){
        return process(data, String.class);
    }

    public <T> Mono<T> process(Object data, Class<T> cls){
        try (StringWriter writer = new StringWriter()) {
            this.map.putAll(JsonUtils.convertValue(data, Map.class));
            this.template.process(map, writer);
            if (cls.equals(String.class)){
                return Mono.just((T)writer.toString());
            }
            return Mono.just(JsonUtils.readValue(writer.toString(), cls));
        } catch (IOException | TemplateException e) {
            return Mono.error(new TechnicalException(e, TECHNICAL_FREEMARKER_ERROR));
        }
    }
}