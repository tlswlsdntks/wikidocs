package kr.wikidocs.config.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = -2148669317097583174L;

    public CustomObjectMapper() {
    	/*
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Number.class, new NumberToStringSerializer());
        registerModule(simpleModule);
        getSerializerProvider().setNullValueSerializer(new NullSerializer());
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 없는 필드로 인한 오류 무시
        */
    }
}