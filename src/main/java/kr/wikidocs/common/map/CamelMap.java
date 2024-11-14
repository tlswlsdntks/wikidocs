package kr.wikidocs.common.map;

import java.util.HashMap;

import org.springframework.jdbc.support.JdbcUtils;

/**
 * Camel Case 표기법 변환 처리를 포함하는 Map 확장 클래스
 */
public class CamelMap extends HashMap<Object, Object> {

    private static final long serialVersionUID = 6723434363565852261L;

	/**
     * key 에 대하여 Camel Case 변환하여 super.put(ListOrderedMap) 을 호출한다.
     * 
     * @param key - '_' 가 포함된 변수명
     * @param value - 명시된 key 에 대한 값 (변경 없음)
     * @return previous value associated with specified key, or null if there was no mapping for key
     */
	@Override
	public Object put(Object key, Object value) {
		if(key != null && key.toString().contains("_")) {
			return super.put(JdbcUtils.convertUnderscoreNameToPropertyName((String) key), value);
		}
		return super.put(key, value);
	}
}
