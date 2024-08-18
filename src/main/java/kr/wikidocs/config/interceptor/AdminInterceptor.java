package kr.wikidocs.config.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.wikidocs.config.vo.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.wikidocs.common.session.UserSession;
import kr.wikidocs.common.util.XssUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Interceptor
 */
@Slf4j
@Component
public class AdminInterceptor implements HandlerInterceptor {

	@Value("${spring.profiles.active}")
	private String profiles;

	@SuppressWarnings("rawtypes")
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws IOException{
		if(handler instanceof HandlerMethod) {
			UserSession session = new UserSession();
			String url = req.getRequestURI();
			log.info("preHandle.url ::: [{}]", url);

			boolean isAjax = false;
			Enumeration enumeration = req.getHeaderNames();
			while(enumeration.hasMoreElements()) {
				String headerName = (String)enumeration.nextElement();
				if("AJAX".equals(headerName.toUpperCase())) {
					isAjax = true;
					break;
				}
			}

//			// 로그인 어노테이션 판단
//			NoSession noSession = ((HandlerMethod) handler).getMethodAnnotation(NoSession.class);
//			if(noSession != null) {
//				return true;
//			} else {
//				if(!session.isLogin()) {
//					if(isAjax) {
//						res.sendError(999, "세션 종료");
//					} else {
//						if("local".equals(profiles)) {
//							res.sendRedirect("/user/login");
//						} else {
//							res.sendRedirect("/user/login");
//						}
//					}
//					return false;
//				}
//			}
		}
		return true;
	}

	@Override
	@SuppressWarnings("rawtypes")
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView modelAndView) throws Exception {

//    	UserSession session = new UserSession();
//		String url = req.getRequestURI();
//		log.info("postHandle.url ::: [{}]", url);

    	// Ajax 여부체크
		boolean isAjax = false;
		Enumeration enumeration = req.getHeaderNames();
		while(enumeration.hasMoreElements()) {
			String headerName = (String)enumeration.nextElement();
			if("AJAX".equals(headerName.toUpperCase())) {
				isAjax = true;
				break;
			}
		}

		if(isAjax) {
			/*
			if(modelAndView != null) {
				modelAndView.addObject("result", new HashMap());
				modelAndView.setViewName("jsonView");
			}*/
		}
		else {

			if(modelAndView != null) {

				Path path = new Path(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort());

				String viewName = modelAndView.getViewName();
				log.debug("@@@ viewName: {}", viewName);

				if(viewName != null){
					if("jsonView".equals(viewName)) {
						// TODO:
					}
					else {
						String jsonObject = "";
						ObjectMapper objectMapper = new ObjectMapper();
						if(modelAndView.getModelMap().get("result") instanceof HashMap) {
                            HashMap result = (HashMap)modelAndView.getModelMap().get("result");
							jsonObject = objectMapper.writeValueAsString(XssUtils.stripXSS(result));
						}
						else if(modelAndView.getModelMap().get("result") instanceof ArrayList) {
							ArrayList result = (ArrayList)modelAndView.getModelMap().get("result");
							jsonObject = objectMapper.writeValueAsString(XssUtils.stripXSS(result));
						}
						else {
							jsonObject = objectMapper.writeValueAsString(XssUtils.stripXSS("{}"));
						}
						modelAndView.addObject("result", jsonObject);
					}
				}

				log.debug("@@@ modelAndView: {}", modelAndView);
			}
		}
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {
    }
}