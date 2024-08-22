package kr.wikidocs.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class StringUtils { 

	public static String nvl(String str) {
		if (str == null) {
			return "";
		} else {
			return str;
		}
	}

	public static String nvlt(String str) {
		if (str == null) {
			return "";
		} else {
			return str.trim();
		}
	}

	public static String nvl(Object obj, String replace) {
		String str = (obj != null ? String.valueOf(obj) : "");
		if (str == null || "".equals(str) || "null".equals(str)
				|| str.trim().length() == 0) {
			return replace;
		} else {
			return str;
		}
		
	}
	public static String nvl(String str, String replace) {
		if (str == null || "".equals(str) || "null".equals(str)
				|| str.trim().length() == 0) {
			return replace;
		} else {
			return str;
		}
	}	
	public static boolean isNullOrBlank(String str) {
		return str == null || str.length() == 0;
	}

	public static String fixLength(String input, int limit, String postfix) {
		String buffer = "";
		char charArray[] = input.toCharArray();
		if (limit >= charArray.length) {
			return input;
		}
		for (int j = 0; j < limit; j++) {
			buffer = new StringBuffer(String.valueOf(buffer)).append(
					charArray[j]).toString();
		}
		buffer = new StringBuffer(String.valueOf(buffer)).append(postfix)
				.toString();
		return buffer;
	}

	public static String replaceStr(String source, String keyStr, String toStr) {
		if (source == null) {
			return null;
		}
		int startIndex = 0;
		int curIndex = 0;
		StringBuffer result = new StringBuffer();
		while ((curIndex = source.indexOf(keyStr, startIndex)) >= 0) {
			result.append(source.substring(startIndex, curIndex)).append(toStr);
			startIndex = curIndex + keyStr.length();
		}
		if (startIndex <= source.length()) {
			result.append(source.substring(startIndex, source.length()));
		}
		return result.toString();
	}

	public static String replaceStr(String source, int start, int end,
			String toStr) {
		if (source == null) {
			return null;
		}
		StringBuffer result = new StringBuffer(source);
		int len = source.length();
		if (start > len || end < start) {
			return result.toString();
		} else {
			result.replace(start, end, toStr);
			return result.toString();
		}
	}

	public static String replaceStr(String source, String keyStr,
			String toStr[]) {
		if (source == null) {
			return null;
		}
		int startIndex = 0;
		int curIndex = 0;
		int i = 0;
		StringBuffer result = new StringBuffer();
		String specialString = null;
		while ((curIndex = source.indexOf(keyStr, startIndex)) >= 0) {
			if (i < toStr.length) {
				specialString = toStr[i++];
			} else {
				specialString = " ";
			}
			result.append(source.substring(startIndex, curIndex)).append(
					specialString);
			startIndex = curIndex + keyStr.length();
		}
		if (startIndex <= source.length()) {
			result.append(source.substring(startIndex, source.length()));
		}
		return result.toString();
	}

	public static String printStr(String source, String format) {
		if (source == null) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		char f[] = format.toCharArray();
		char s[] = source.toCharArray();
		int len = f.length;
		int h = 0;
		for (int i = 0; i < len; i++) {
			if (h >= s.length) {
				break;
			}
			if (f[i] == '#') {
				buf.append(s[h++]);
			} else {
				buf.append(f[i]);
			}
		}

		return buf.toString();
	}

	public static String formatStr(String source, String format) {
		return printStr(source, format);
	}

	public static String moneyFormatStr(String moneyFormatStr) {
		double money = Double.parseDouble(moneyFormatStr);
		DecimalFormat df = new DecimalFormat("#,###,###,###,###,###");
		String formatStr = df.format(money);
		return formatStr;
	}

	public static String moneyFormatStr(double money) {
		DecimalFormat df = new DecimalFormat("#,###,###,###,###,###");
		String formatStr = df.format(money);
		return formatStr;
	}

	public static String moneyFormatStr(long money) {
		DecimalFormat df = new DecimalFormat("#,###,###,###,###,###");
		String formatStr = df.format(money);
		return formatStr;
	}

	public static String lpad(int src, int size) {
		StringBuffer builder;
		for (builder = new StringBuffer(String.valueOf(src)); builder.length() < size; builder
				.insert(0, "0"))
			;
		return builder.toString();
	}

	public static String lpad(long src, int size) {
		StringBuffer builder;
		for (builder = new StringBuffer(String.valueOf(src)); builder.length() < size; builder
				.insert(0, "0"))
			;
		return builder.toString();
	}

	public static String lpad(double src, int size) {
		StringBuffer pattern = new StringBuffer();
		String srcStr = String.valueOf(src);
		int len = srcStr.length();
		for (int i = size; i > len; i--) {
			pattern.append("0");
		}
		return pattern.append(srcStr).toString();
	}

	public static String lpad(String src, int size) {
		return lpad(src, size, "0");
	}

	public static String lpad(String src, int size, String filler) {
		String pattern = "";
		int len = nvl(src).length();
		if (len > size) {
			return src.substring(len - size);
		}
		for (int i = len; i < size; i++) {
			pattern = new StringBuffer(String.valueOf(pattern))
					.append(filler).toString();
		}
		return new StringBuffer(String.valueOf(pattern)).append(nvl(src))
				.toString();
	}

	public static String fixPrecision(double aSrc, int aPrecisionSize) {
		String result = "";
		if (aPrecisionSize > 0) {
			double tail = aSrc - (double) (long) aSrc;
			if (aPrecisionSize < DEFAULT_PRECISION_FORMAT_LENGTH) {
				result = new StringBuffer(String.valueOf(defaultDecimalFormat
						.format(aSrc - tail))).append(
						defaultPrecisionFormat.format(tail).substring(0,
								aPrecisionSize + 1)).toString();
			} else {
				String pattern = "##############################.00000000";
				for (int i = DEFAULT_PRECISION_FORMAT_LENGTH; i <= aPrecisionSize; i++) {
					pattern = new StringBuffer(String.valueOf(pattern)).append("0").toString();
				}
				DecimalFormat newFormat = new DecimalFormat(pattern);
				result = newFormat.format(aSrc);
			}
		} else {
			result = defaultDecimalFormat.format(aSrc);
		}
		return result;
	}
	
	public static String defaultString(String str) {
        return str == null ? EMPTY : str;
    }
	
	public static String defaultString(String str, String defaultStr) {
        return str == null ? defaultStr : str;
    }
	
    public static String defaultString( Object obj )
    {
        return defaultString( obj, "" );
    }

    public static String defaultString( Object obj, String defaultString )
    {
        return ( obj == null ) ? defaultString : obj.toString();
    }
    
    public static Integer defaultInteger(String str) {
    	if(str == null || "".equals(str)) {
    		return 0;
    	} else {
    		return Integer.parseInt(str);
    	}
    }
    
    public static Integer defaultInteger(Object obj) {
    	return obj == null ? 0 : defaultInteger(obj.toString());
    }
    
	public static String fixPrecision(String aSrc, int aPrecisionSize) {
		if (aSrc == null) {
			return null;
		}
		String result = aSrc;

		int index = aSrc.lastIndexOf(".");
		String tmp[] = result.split("[.]");//jdk 1.4 ver
		if (index >= 0) {
			int len = aSrc.length();
			if (aPrecisionSize + 1 > len - index) {
				for (int i = len - index; i <= aPrecisionSize; i++) {
					result = new StringBuffer(String.valueOf(result)).append('0').toString();
				}
			} else {
				String temp = tmp[1].substring(0, aPrecisionSize);
				result = tmp[0] + "." + temp;
			}
		}

		return result;
	}

	public static String fixPrecision(double aSrc, String aFormat) {
		if (aFormat == null || aFormat.equals("")) {
			return null;
		} else {
			DecimalFormat newFormat = new DecimalFormat(aFormat);
			String tmpStr = newFormat.format(aSrc);
			return tmpStr;
		}
	}

	public static String rpad(String src, int size) {
		return rpad(src, size, " ");
	}

	public static String rpad(String src, int size, String filler) {
		StringBuffer sb = new StringBuffer();
		if (src == null) {
			for (int i = 0; i < size; i++)
				sb.append(filler);

		} else {
			int len = src.length();
			if (len > size) {
				sb.append(src.substring(0, size));
			} else {
				sb.append(src);
			}
			for (int i = len; i < size; i++) {
				sb.append(filler);
			}
		}
		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List stringToList(String sourceString, String delim) {
		List destinationList = new ArrayList();
		if (sourceString != null) {
			int index = -1;
			int oldIndex = -1;
			do {
				oldIndex = index + 1;
				index = sourceString.indexOf(delim, oldIndex);
				if (index == -1) {
					break;
				}
				destinationList.add(sourceString.substring(oldIndex, index));
			} while (true);
			
			destinationList.add(sourceString.substring(oldIndex, sourceString.length()));
		}
		return destinationList;
	}

	public static String trim(String s) {
		if (s == null)
			return "";
		int st = 0;
		char val[] = s.toCharArray();
		int count = val.length;
		int len;
		for (len = count; st < len && val[st] <= ' '; st++)
			;
		for (; st < len && val[len - 1] <= ' '; len--)
			;
		return st <= 0 && len >= count ? s : s.substring(st, len);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return true;
		for (int i = 0; i < strLen; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return false;

		return true;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static long s2l(String str) {
		long lo;
		try {
			lo = new Long(str).longValue();
		} catch (Exception e) {
			lo = 0L;
		}
		return lo;
	}

	public static Long s2L(String str) {
		Long Lo;
		try {
			Lo = new Long(str);
		} catch (Exception e) {
			Lo = new Long(0L);
		}
		return Lo;
	}

	public static double s2d(String str) {
		double d;
		try {
			d = new Double(str).doubleValue();
		} catch (Exception e) {
			d = 0.0D;
		}
		return d;
	}

	public static Double s2D(String str) {
		Double D;
		try {
			D = new Double(str);
		} catch (Exception e) {
			D = new Double(0.0D);
		}
		return D;
	}

	public static String[] split(String source, String delim) {
		String rltarr[] = (String[]) null;
		if (source == null)
			return rltarr;
		if (delim == null || "".equals(delim)) {
			String rltarr2[] = { source };
			return rltarr2;
		} else {
			rltarr = new StringBuffer(String.valueOf(source))
					.append("\u25A9").toString().split(
							new StringBuffer("\\").append(delim).toString());
			int len = rltarr.length - 1;
			rltarr[len] = rltarr[len].substring(0, rltarr[len].length() - 1);
			return rltarr;
		}
	}
	
	public static boolean equalsOr(String var, String val) {
		if (var == null || "".equals(var))
			return false;
		if (val == null || "".equals(val))
			return false;
		boolean rtnVal = false;
		String valArr[] = (String[]) splitedArrs.get(val);
		if (valArr == null) {
			valArr = val.split("\\|");
			splitedArrs.put(val, valArr);
		}
		String as[] = valArr;
		int i = 0;
		for (int j = as.length; i < j; i++) {
			String _val = as[i];
			if (!_val.equals(var))
				continue;
			rtnVal = true;
			break;
		}
		return rtnVal;
	}

	public static boolean equalsOr1(String var, String val) {
		if (var == null || "".equals(var))
			return false;
		if (val == null || "".equals(val))
			return false;
		Map map = (Map) splitedMap.get(val);
		if (map == null) {
			String valArr[] = val.split("\\|");
			map = new HashMap();
			for (int i = 0; i < valArr.length; i++)
				map.put(valArr[i], valArr[i]);

			splitedMap.put(val, map);
		}
		return map.containsKey(var);
	}
	
	public static String typeFormat(String s, String t) {
		
		if(s.length() != 8 || isNullOrBlank(s)){
			return "";
		}
		String year = s.substring(0, 4);
		String month = s.substring(4, 6);
		String day = s.substring(6);
		
		StringBuffer sbf = new StringBuffer();	
		sbf.append(year + t + month + t + day);
		return sbf.toString();
	}
	
	public static String strFormat(String str, int num, String format) {
		
		if(isNullOrBlank(str) || isNullOrBlank(format) || num < 0){
			return "";
		}
		int leng = str.length();		
		int formatLeng = leng - num;
		StringBuffer strEx = new StringBuffer(str.substring(0, num));
		
		for (int i = 0; i < formatLeng; i++) {
			strEx.append(format);
		}		
		return strEx.toString();
	}
	
	public static String setZeroFrontNumFormat(String s) {		
		int nLeng = 2 - s.length();
		String sEx = "";
		if(nLeng != 1){		
			for (int i = 0; i < nLeng; i++) {
				sEx += "0";
			}
			sEx += s;
		}else{
			sEx  = s;
		}		
		return sEx;
	}
	
	private static final String ARR_DAY_KOR[] = {"�Ͽ���","�����","ȭ����","������","�����","�ݿ���","�����"};
	public static final String EMPTY = "";
	private static final DecimalFormat defaultDecimalFormat = new DecimalFormat("##############################");
	private static final int DEFAULT_PRECISION_FORMAT_LENGTH = ".00000000".length();
	private static final DecimalFormat defaultPrecisionFormat = new DecimalFormat(".00000000");
	private static Map splitedArrs = Collections.synchronizedMap(new HashMap());
	private static Map splitedMap = Collections.synchronizedMap(new WeakHashMap());
	
	public static final String[] getSpilt(String sKey) {	
		String[] sValue = sKey.split("%"); 			
	    return sValue;
	}
	
	public static String cutString(String strSource, int cnt) {
		if (strSource == null || strSource.length() < 1) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int len = 0;
		for (int i = 0; i < strSource.length(); i++) {
			if (strSource.charAt(i) > 255) {
				len += 2;
			} else {
				len++;
			}
			if (len > cnt) {
				return sb.toString();
			}
			sb.append(strSource.charAt(i));
		}
		return sb.toString();
	}

	public static String getElapsedTimeString(long elapsed) {
		int remdr = (int) (elapsed % (24L * 60 * 60 * 1000));

		final int hours = remdr / (60 * 60 * 1000);

		remdr %= 60 * 60 * 1000;

		final int minutes = remdr / (60 * 1000);

		remdr %= 60 * 1000;

		final int seconds = remdr / 1000;

		final int ms = remdr % 1000;
		String msec = Integer.toString(ms);
		while (msec != null && msec.length() < 3) {
			msec += "0";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(new DecimalFormat("00").format(hours));
		sb.append(":");
		sb.append(new DecimalFormat("00").format(minutes));
		sb.append(":");
		sb.append(new DecimalFormat("00").format(seconds));
		sb.append(".");
		sb.append(msec);
		return sb.toString();
	}
	
	public static String clearCrossSiteScripting(String value){
		String rtnVal = value;
		rtnVal = clearXSSMinimum(rtnVal);
		return rtnVal;
	}

	public static String clearXSSMinimum(String value) {
		if (value == null || value.trim().equals("")) {
			return "";
		}

		String returnValue = value;
		returnValue = returnValue.replaceAll("&", "&amp;");
		returnValue = returnValue.replaceAll("<", "&lt;");
		returnValue = returnValue.replaceAll(">", "&gt;");
		returnValue = returnValue.replaceAll("\"", "&#34;");
		returnValue = returnValue.replaceAll("\'", "&#39;");
		return returnValue;
    }
	
	public static String setXSSMinimum(String value) {
		if (value == null || value.trim().equals("")) {
			return "";
		}

		String returnValue = value;
		returnValue = returnValue.replaceAll("&lt;", "<");
		returnValue = returnValue.replaceAll("&gt;", ">");
		returnValue = returnValue.replaceAll("&quot;", "\"");
		returnValue = returnValue.replaceAll("&apos;", "\'");
		return returnValue;
	}

	/**
	 * 주어진 문자열을 주어진 maxBytesSize 를 넘지 않는 크기만큼 잘라서 문자열 배열로 반환한다.
	 * 
	 * @param src
	 * @param maxByteSize
	 * @param bytesPerCharacter
	 * @return
	 */
	public static String[] splitUnicodeToBytes(String src, final int maxByteSize, final int bytesPerCharacter) {

		List<String> res = new ArrayList();

		StringBuffer buf = new StringBuffer();
		int index = 0;
		for (int i = 0; i < src.length(); i++) {

			char ch = src.charAt(i);
			int appendix = 1;
			UnicodeBlock unicodeBlock = UnicodeBlock.of(ch);
			if (UnicodeBlock.HANGUL_SYLLABLES.equals(unicodeBlock)
					|| UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals(unicodeBlock)
					|| UnicodeBlock.HANGUL_JAMO.equals(unicodeBlock)
					|| UnicodeBlock.HANGUL_JAMO_EXTENDED_A.equals(unicodeBlock)
					|| UnicodeBlock.HANGUL_JAMO_EXTENDED_B.equals(unicodeBlock)) {
				appendix = bytesPerCharacter;
			}
			index += appendix;

			if (index > maxByteSize) {
				res.add(buf.toString());
				index = appendix;
				buf = new StringBuffer();
			}
			buf.append(ch);

		}
		if (buf.length() > 0) {
			res.add(buf.toString());
		}
		return res.toArray(new String[res.size()]);
	}

	/**
	 * 주어진 문자열을 원하는 charSet으로 변환. ex) 문자열(utf-8) -> euc-kr
	 * 
	 * @param src
	 * @param maxByteSize
	 * @param bytesPerCharacter
	 * @return
	 */
	public static String stringConvert(String str, String encoding) throws Exception {
		ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
		String returnStr = "";
		try {
			requestOutputStream.write(str.getBytes(encoding));
			returnStr = requestOutputStream.toString(encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnStr;
	}
	
	/**
     * 배열에서 포함값 여부
     * @param array
     * @param find
     * @return
     */
    public static boolean arrayContains(String[] array, String objectToFind) {
    	
    	//MEMO: 일치해야 찾아짐
    	//utils
    	//return ArrayUtils.contains(array, objectToFind);
    	//java8
    	//return Arrays.stream(array).anyMatch(objectToFind::equals);
    	//java7
    	//return Arrays.asList(array).contains(objectToFind);
    	
    	//MEMO: 포함되어도 찾아지도록.
		boolean isFind = false;
		if(isNotBlank(objectToFind)) {
			for(String str : array) {
				if (str.contains(objectToFind)) {
					isFind = true;
					break;
				}
				if (objectToFind.contains(str)) {
					isFind = true;
					break;
				}
			}
		}
		return isFind;
	}
    
    /**
     * 객체배열을 스트링배열로 변환
     * @param array
     * @param find
     * @return
     */
    public static String[] toStringArray(Object[] objectArray) {
    	
    	String[] stringArray = null;
    	if(objectArray != null && objectArray.length > 0) {
    		stringArray = new String[objectArray.length];
    		for(int i = 0; i < objectArray.length; i++) {
    			stringArray[i] = String.valueOf(objectArray[i]);
    		}
    	}
    	return stringArray;
	}
}