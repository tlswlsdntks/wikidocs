package kr.wikidocs.config.handler;

import jakarta.servlet.http.HttpServletRequest;
import kr.wikidocs.common.map.CommandMap;
import kr.wikidocs.common.session.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * ArgumentResolver
 */
@Slf4j
@Component
public class CustomArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        //log.info("@@@ CustomArgumentResolver.supportsParameter: " + Arrays.toString(parameter.getMethodAnnotations()));

        // HashMap params
        if (HashMap.class.isAssignableFrom(parameter.getParameterType()) && parameter.hasParameterAnnotation(RequestParam.class)) {
            return true;
        } else if (CommandMap.class.isAssignableFrom(parameter.getParameterType())) {
            return true;
        } else if (UserSession.class.isAssignableFrom(parameter.getParameterType())) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        if (HashMap.class.isAssignableFrom(parameter.getParameterType()) && parameter.hasParameterAnnotation(RequestParam.class)) {
        } else if (CommandMap.class.isAssignableFrom(parameter.getParameterType())) {
            CommandMap commandMap = new CommandMap();
            HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
            Enumeration<?> enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String[] values = request.getParameterValues(key);
                if (values != null) {
                    commandMap.put(key, (values.length > 1) ? values : values[0]);
                }
            }
            return commandMap;
        } else if (UserSession.class.isAssignableFrom(parameter.getParameterType())) {
            return new UserSession();
        }
        return null;
    }
}
