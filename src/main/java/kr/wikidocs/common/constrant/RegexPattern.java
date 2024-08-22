package kr.wikidocs.common.constrant;

public interface RegexPattern {

	public static final String HTML = "<[^>]*>";
    public static final String EMAIL = "^(\\w+)([-+.][\\w]+)*@(\\w[-\\w]*\\.){1,5}([A-Za-z]){2,4}$";
    public static final String IP = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";
    public static final String UNVALID_FILE_EXT = "(JS|JSP|JAVA|CLASS)";
    public static final String DATE = "[1-2]{1}[0-9]{3}(\\.|-)?[0-1]{1}[0-9]{1}(\\.|-)?[0-3]{1}[0-9]{1}";
}
