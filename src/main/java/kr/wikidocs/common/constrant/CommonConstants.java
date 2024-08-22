package kr.wikidocs.common.constrant;

/**
 * COMMON - 공통 상수 선언 인터페이스.
 *
 * <p>
 * 한글 및 변경 가능성 있는 상수는 {@link SysProperties}로 부터
 * 시스템 별 환경 설정파일에서 값을 가져올 수 있도록 매핑함.
 * </p>
 *
 * @see SysProperties
 */
public interface CommonConstants {

    /** 기본 페이지사이즈 */
    int DEFAULT_PAGE_SIZE = 10;

    /**
     * 응용 SQL 기록을 위한 로거명.
     */
    String LOGGER_NAME_APPSQL = "__appsql";

    /**
     * BaseRuntimeException 디폴트 메시지.
     */
    String NO_MESSAGE_BASERUNTIME_ERROR = "BaseRuntimeException without message";

    /**
     * No Message Key.
     */
    String NO_MESSAGE_COMMON = "N/A";

    /**
     * HttpRequest Header 'X-Requested-With' Value.
     */
    String HTTP_HEADER_VALUE_XHR = "XMLHttpRequest";

    /**
     * Global date format.
     */
    String DATE_FORMAT_GLOBAL = "yyyyMMdd";

    /**
     * Screen date format.
     */
    String DATE_FORMAT_SCREEN = "yyyy.MM.dd";

    /**
     * Global time format.
     */
    String TIME_FORMAT_GLOBAL = "HH:mm";

    /**
     * download file dateformat.
     */
    String DOWNLOAD_FILE_DATETIME_FORMAT = "yyyyMMdd_HHmmss";

    /**
     * system menu sequence key used in various sectors.
     */
    String KEY_SYS_MENU_SEQ = "sysMenuSeq";

    /**
     * menu key used in binding MenuTree to Session.
     */
    String SESSION_MENU = "userMenu";

    /**
     * menu sequence key used in binding menu sequence to Session.
     */
    String SESSION_MENU_SEQ = "menuSeq";

    /**
     * login user key used in binding UserVO to Session.
     */
    String SESSION_USER = "userInfo";
}
