package kr.wikidocs.common.util;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

/**
 * JSON 유틸
 */
@Slf4j
public class JSONUtils extends org.apache.commons.io.FileUtils {

	// =========================================================
	// Gson
	// =========================================================
	/**
	 * Gson
	 */
	private static Gson gson = new GsonBuilder().create();

	/**
	 * JsonObject 문자열체크
	 */
	public static boolean isValidJsonObject(String value) {

		try {
			gson.fromJson(value, JsonObject.class);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * JsonArray 문자열체크
	 */
	public static boolean isValidJsonArray(String value) {

		try {
			gson.fromJson(value, JsonArray.class);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Map > JsonObject
	 */
	public static JsonObject mapToJsonObject(Map<String, Object> map) throws Exception {

		JsonObject json = new JsonObject();

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if(value instanceof String) {
				if(JSONUtils.isValidJsonObject((String)value)) {
					json.add(key, gson.fromJson((String)value, JsonObject.class));
				}
				else if(JSONUtils.isValidJsonArray((String)value)) {
					json.add(key, gson.fromJson((String)value, JsonArray.class));
				}
				else {
					json.add(key, gson.toJsonTree(value));
				}
			} else {
				json.add(key, gson.toJsonTree(value));
			}
		}
		return json;
	}





	// =========================================================
	// org.json
	// =========================================================
//	/**
//	 * JSONObject 문자열체크
//	 */
//	public static boolean isValidJSONObject(String json) {
//
//		try {
//			new JSONObject(json);
//		}
//		catch(Exception e) {
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * JSONArray 문자열체크
//	 */
//	public static boolean isValidJSONArray(String json) {
//
//		try {
//			new JSONArray(json);
//		}
//		catch(Exception e) {
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * Map > JSONObject
//	 */
//	public static JSONObject mapToJSONObject(Map<String, Object> map) throws Exception {
//
//		JSONObject json = new JSONObject();
//
//		for (Map.Entry<String, Object> entry : map.entrySet()) {
//			String key = entry.getKey();
//			Object value = entry.getValue();
//			//json.addProperty(key, value);
//
//			if(value instanceof String) {
//				if(JSONUtils.isValidJSONObject((String)value)) {
//					json.put(key, new JSONObject((String)value));
//				}
//				else if(JSONUtils.isValidJSONArray((String)value)) {
//					json.put(key, new JSONArray((String)value));
//				}
//				else {
//					json.put(key, value);
//				}
//			} else {
//				json.put(key, value);
//			}
//		}
//		return json;
//	}
}
