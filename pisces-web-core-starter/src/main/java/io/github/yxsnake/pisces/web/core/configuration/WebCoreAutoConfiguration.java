package io.github.yxsnake.pisces.web.core.configuration;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.github.yxsnake.pisces.web.core.configuration.properties.*;
import io.github.yxsnake.pisces.web.core.framework.filter.RequestLogFilter;
import io.github.yxsnake.pisces.web.core.framework.filter.XssFilter;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import java.io.IOException;
import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author snake
 * @description
 * @since 2024/1/16 22:24
 */
@SuppressWarnings("unchecked")
@Slf4j
@AllArgsConstructor
@Import({
        HealthProperties.class,
        WebCoreProperties.class,
        SwaggerProperties.class,
        RequestLogProperties.class,
        XssProperties.class
})
@Configuration
public class WebCoreAutoConfiguration {

  private final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

  private final static String FORMAT_DATE = "yyyy-MM-dd";

  private final static String FORMAT_TIME = "HH:mm:ss";


  private final HealthProperties health;

  private final WebCoreProperties webCore;

  private final SwaggerProperties swagger;

  private final RequestLogProperties requestLog;

  private final XssProperties xssProperties;

  private final Environment environment;

  @PostConstruct
  public void init() {
    log.info("------------ Web-core-starter StartUp Information -----------");
    log.info("web-core-starter");
    log.info("  |-Core");
    log.info("    |-applicationName: {}",getApplicationName(environment));
    log.info("    |-serverPort: {}",getServerPort(environment));
    log.info("  |-Health");
    log.info("    |-enabled: {}", health.getEnabled());
    log.info("    |-path: {}", health.getPath());
    log.info("  |-Swagger");
    log.info("    |-enabled: {}", swagger.getEnabled());
    log.info("  |-RequestLog");
    log.info("    |-needLogRequest: {}", requestLog.getNeedLogRequest());
    log.info("    |-needLogResponse: {}", requestLog.getNeedLogResponse());
    log.info("    |-needLogHeader: {}", requestLog.getNeedLogHeader());
    log.info("    |-needLogPayload: {}", requestLog.getNeedLogPayload());
    log.info("  |-Xss");
    log.info("    |-enabled: {}", xssProperties.getEnabled());
    log.info("-------------------------------------------------------------");
  }

  private String getApplicationName(Environment environment){
    return environment.getProperty("spring.application.name");
  }

  private String getServerPort(Environment environment){
    return environment.getProperty("server.port");
  }

  @Bean
  @ConditionalOnMissingBean
  public WebCoreMvcAutoConfiguration webMvcConfiguration() {
    return new WebCoreMvcAutoConfiguration(webCore);
  }

  @Bean
  public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean() {
    FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new XssFilter(xssProperties));
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<RequestLogFilter> requestLogFilterRegistrationBean() {
    FilterRegistrationBean<RequestLogFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new RequestLogFilter(requestLog));
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME);
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(FORMAT_TIME);
    return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
      .deserializerByType(String.class, new StdScalarDeserializer<String>(String.class) {
        @Serial
        private static final long serialVersionUID = 2434809356695323964L;

        @Override
        @SuppressWarnings("unchecked")
        public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
          return StringUtils.trimWhitespace(jsonParser.getValueAsString());
        }
      })
      .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter))
      .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter))
      .serializerByType(LocalDate.class, new LocalDateSerializer(dateFormatter))
      .deserializerByType(LocalDate.class, new LocalDateDeserializer(dateFormatter))
      .serializerByType(LocalTime.class, new LocalTimeSerializer(timeFormatter))
      .deserializerByType(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
  }

  /**
   * 解决主键Long类型返回给页面时，页面精度丢失的问题,时间格式化返回
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addSerializer(Long.class, ToStringSerializer.instance).addSerializer(Long.TYPE, ToStringSerializer.instance);
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.registerModule(simpleModule);
    return objectMapper;
  }


  @Bean
  public Validator validator(AutowireCapableBeanFactory autowireCapableBeanFactory) {
    ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
      .configure()
      // failFast=true 不校验所有参数，只要出现校验失败情况直接返回，不再进行后续参数校验
      .failFast(true)
      .constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
      .buildValidatorFactory();

    return validatorFactory.getValidator();
  }

  private String getShowText(String text) {
    return StrUtil.isBlank(text) ? "not set" : text;
  }
}
