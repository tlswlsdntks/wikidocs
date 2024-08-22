package kr.wikidocs.common.util;

import jakarta.servlet.http.HttpServletRequest;
import kr.wikidocs.common.constrant.CommonConstants;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * COMMON - Servlet 관련 쉬운 조작을 위해 구현한 유틸리티 클래스.
 *
 * <p>
 * ajax call 체크 여부 등의 메소드를 제공한다.
 * </p>
 */
public abstract class ServletUtils {

    /**
     * ajax 요청여부를 판단한다.
     *
     * @param request HttpServletRequest
     * @return isAjaxCall boolean
     */
    public static boolean isAjaxCall(HttpServletRequest request) {

        Enumeration headerEnum = request.getHeaderNames();
        while (headerEnum.hasMoreElements()) {
            String key = (String)headerEnum.nextElement();
            String value = request.getHeader(key);
            if (value.equalsIgnoreCase(CommonConstants.HTTP_HEADER_VALUE_XHR)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 전달되는 모든 Parameters 를 queryString(key=value) 로 생성한다.
     *
     * @param request HttpServletRequest
     * @return queryString String
     */
    public static String getQueryStringFromAllParams(HttpServletRequest request) {

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Iterator it = request.getParameterMap().entrySet().iterator(); it.hasNext();) {
            Entry entry = (Entry)it.next();
            if (i > 0) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=");

            String value = null;
            if (entry.getValue() instanceof String[]) {
                String[] values = (String[])entry.getValue();
                value = (values.length == 1 ? values[0] : String.valueOf(values));
            }
            else {
                value = (String)entry.getValue();
            }
            sb.append(value);
            ++i;
        }

        return sb.toString();
    }
    
    /**
     * 전달되는 모든 Parameters 를 HashMap으로 변환 
     * @param request HttpServletRequest
     * @return hmap HashMap
     */
	public static HashMap<String, Object> convertMap(HttpServletRequest request) {
		
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		String key;
		Enumeration<?> parameterNames = request.getParameterNames();

		while (parameterNames.hasMoreElements()) {
			key = (String) parameterNames.nextElement();
			if (request.getParameterValues(key).length > 1) {
				hmap.put(key, request.getParameterValues(key));
			} else {
				hmap.put(key, request.getParameter(key));
			}
		}
		return hmap;
	}
}
