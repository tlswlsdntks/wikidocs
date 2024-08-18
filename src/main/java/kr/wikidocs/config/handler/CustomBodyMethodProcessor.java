package kr.wikidocs.config.handler;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomBodyMethodProcessor extends RequestResponseBodyMethodProcessor {

	public CustomBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
		super(converters);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return super.supportsParameter(parameter);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.info("@@@ not called ...");
		return super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
	}
}
