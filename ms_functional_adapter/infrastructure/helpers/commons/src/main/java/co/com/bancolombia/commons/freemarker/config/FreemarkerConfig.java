package co.com.bancolombia.commons.freemarker.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class FreemarkerConfig extends Configuration{

    public FreemarkerConfig(Version version){
        super(version);
        setDefaultEncoding("UTF-8");
        setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        setLogTemplateExceptions(false);
        setWrapUncheckedExceptions(true);
        setFallbackOnNullLoopVariable(false);
    }
}
