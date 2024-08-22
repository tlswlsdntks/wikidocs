package kr.wikidocs.common.util;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * 데이터 유틸
 */
@Slf4j
public class DataUtils extends org.apache.commons.io.FileUtils {
	/**
	 * 공백값 null 설정
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T setEmptyToNull(Object obj) throws Exception {

		if(obj instanceof Map) {
			Map<String, Object> map = (Map) obj;
			map.entrySet().forEach((entry) -> {
				if(entry.getValue() instanceof String && StringUtils.isEmpty((String)entry.getValue())) {
					entry.setValue(null);
				}
			});
			return (T) map;
		}
		return (T) obj;
	}
}
