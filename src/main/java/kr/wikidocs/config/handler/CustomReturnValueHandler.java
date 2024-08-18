package kr.wikidocs.config.handler;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.extern.slf4j.Slf4j;

/**
 * ReturnValueHandler
 */
@Slf4j
@Component
public class CustomReturnValueHandler implements HandlerMethodReturnValueHandler {

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		if(AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class)) {
			return true;
		}
		return false;
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
//		ResultParams params = (ResultParams) returnValue;
//		mavContainer.setRequestHandled(true);
//		if (params.containsKey(CommonConstants.STATUS_CODE)) {
//			webRequest.setAttribute(CommonConstants.STATUS_CODE, params.getVariable(CommonConstants.STATUS_CODE), 0);
//			webRequest.setAttribute(CommonConstants.STATUS_TEXT, params.getVariable(CommonConstants.STATUS_TEXT), 0);
//		}
	}
}
