package kr.wikidocs.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    private final static Log log = LogFactory.getLog(DateUtils.class.getName());

    //	하루(밀리초)
    static final long ONE_DAY = 1000 * 60 * 60 * 24;
    //	시간(밀리초)
    static final long ONE_HOUR = 1000 * 60 * 60;
    //	분(밀리초)
    static final long ONE_MIN = 1000 * 60;

    // 월별 마지막일
    static int[] nDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


    /**
     * 해당 월의 마지막일를 구한다.(28, 29, 30, 31)
     *
     * @param int year - 년
     * @param int month - 월
     * @return int - 마지막일(28, 29, 30, 31)
     */
    public static int getDays(int year, int month) {
        if (month == 2) {
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                return 29;
            }
        }

        return nDays[month - 1];
    }

    /**
     * 해당문자열에 날짜를 더한 문자열 날짜를 반환한다.
     *
     * @param String strDate - 날짜
     * @param int    add - 더 할 날짜수
     * @return String - 더해진 날짜
     */
    public static String getDayAdd(String strDate, int add) {
        return dateToStr(getDayAddDate(strDate, add), formatString(strDate));
    }

    /**
     * 해당문자열에 날짜수를 더한 Date 객체를 반환한다.
     *
     * @param String strDate - 날짜
     * @param int    add - 더 할 달수
     * @return Date - 더해진 날짜
     */
    public static Date getDayAddDate(String strDate, int add) {
        Date date = strToDate(strDate, formatString(strDate));
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        cal.add(Calendar.DAY_OF_YEAR, add);

        date = cal.getTime();

        return date;
    }

    /**
     * 해당문자열에 달수를 더한 문자열 날짜를 반환한다.
     *
     * @param String strDate - 날짜
     * @param int    add - 더 할 달수
     * @return String - 더해진 날짜
     */
    public static String getMonthAdd(String strDate, int add) {
        return dateToStr(getMonthAddDate(strDate, add), formatString(strDate));
    }

    /**
     * 해당문자열에 달수를 더한 Date 객체를 반환한다.
     *
     * @param String strDate - 날짜
     * @param int    add - 더 할 달수
     * @return Date - 더해진 날짜
     */
    public static Date getMonthAddDate(String strDate, int add) {
        Date date = strToDate(strDate, formatString(strDate));
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        cal.add(Calendar.MONTH, add);

        date = cal.getTime();

        return date;
    }

    /**
     * 해당 날짜의 길이가 6과 같으면 'yyyyMM'를 반환, 6보다 크면 'yyyyMMdd'를 반환한다.
     * (format String 값)
     *
     * @param strDate
     * @return
     */
    public static String formatString(String strDate) {
        String date = "yyyyMM";

        if (strDate.length() == 19) {
            date = "yyyy-MM-dd HH:mm:ss";
        } else if (strDate.length() > 6 && strDate.length() < 19) {
            date = "yyyyMMdd";
        }
        return date;
    }

    /**
     * Date 객체를 해당 날짜포맷의 문자열 날짜를 변환한다.
     *
     * @param Date   date - Date 객체
     * @param String pattern - 날짜 포맷
     * @return String - 문자열 날짜
     */
    public static String dateToStr(Date date, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat();

        sdf.applyPattern(pattern);

        return sdf.format(date);
    }

    /**
     * 해당 날짜포맷의 문자열 날짜를 Date 객체로 변환한다.
     *
     * @param String strDate - Date 객체
     * @param String pattern - 날짜 포맷
     * @return Date - Date 객체
     */
    public static Date strToDate(String strDate, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat();

        Date date = null;

        try {
            sdf.applyPattern(pattern);
            date = sdf.parse(strDate);
        } catch (Exception e) {
            log.error("Date format Error! - Value: [" + strDate + "], Format: ["
                    + pattern + "]");
        }
        return date;
    }

    /**
     * 문자열 날짜를 Date 객체로 변환한다.(날짜형태:yyyyMMdd)
     *
     * @param strDate - 날짜
     * @return
     */
    public static Date strToDate(String strDate) {

        SimpleDateFormat sdf = new SimpleDateFormat();

        Date date = null;

        try {
            sdf.applyPattern("yyyyMMdd");
            date = sdf.parse(strDate);
        } catch (Exception e) {
            log.error("Date format Error! - Value: [" + strDate + "]");
        }

        return date;
    }

    public static Date strToTime(String strTime) {

        SimpleDateFormat sdf = new SimpleDateFormat();

        Date date = null;

        try {
            sdf.applyPattern("HHmmss");
            date = sdf.parse(strTime);
        } catch (Exception e) {
            log.error("Date format Error! - Value: [" + strTime + "]");
        }

        return date;
    }

    /**
     * from에서 to까지 날짜의 일수를 계산한다.(날짜형태:yyyyMMdd)
     *
     * @param from - 시작일자
     * @param to   - 종료일자
     * @return
     */
    public static int getDateDiff(String from, String to) {
        Date fromDate = strToDate(from);
        Date toDate = strToDate(to);

        long fromL = fromDate.getTime();
        long toL = toDate.getTime();

        long diffL = toL - fromL;

        return (int) (diffL / ONE_DAY);
    }

    public static int getTimeDiff(String from, String to) {
        Date fromTime = strToTime(from);
        Date toTime = strToTime(to);

        long fromL = fromTime.getTime();
        long toL = toTime.getTime();

        long diffL = toL - fromL;

        return (int) (diffL / ONE_MIN);
    }

    /**
     * 현재 일자를 yyyyMMdd 형식으로 반환한다.
     *
     * @return
     */
    public static String getToday() {
        return getToday("yyyyMMdd");
    }

    /**
     * 현재 시각을 HHmmss 형식으로 반환한다.
     *
     * @return
     */
    public static String getTime() {
        return getToday("HHmmss");
    }

    /**
     * 오늘 일자를 지정된 Format의 날짜 표현형식으로 반환한다.
     * 예  ) getToday("yyyy/MM/dd HH:mm:ss.SSS")
     * 결과) 2007/03/22 16:57:20.123
     *
     * @param outFormat
     * @return
     */
    public static String getToday(String outFormat) {
        SimpleDateFormat outFormatter = new SimpleDateFormat(outFormat, Locale.KOREA);
        String dateString = null;
        Date date = new Date();
        try {
            dateString = outFormatter.format(date);
        } catch (Exception e) {
            log.error(e);
        }
        return dateString;
    }

    /**
     * 주어진 날짜를 원하는 type 형태로 변환한다. (형식 : yyyy[type]mm[type]dd)
     *
     * @param date   변환 시킬 문자열
     * @param center 삽입할 문자열 예) -
     * @return String
     */
    public static String displayDate(String date, String center) {
        if (date == null || date.trim().length() < 8) {
            return date;
        }
        String d = date.trim();
        String c = center.trim();

        StringBuilder sb = new StringBuilder();
        sb.append(d.substring(0, 4));
        sb.append(c).append(d.substring(4, 6));
        sb.append(c).append(d.substring(6, 8));
        return sb.toString();
    }


    /**
     * 현재시간HHmmss 값과 현재시간에서 입력받은 값(분minute 단위) 이전의 HHmmss 출력
     * 예)
     * 현재시간(HHmmss) = 153302 일 때,
     * getPreMinute(30) 결과는 = 150302
     * 하루 단위이므로 만약 002230 일 때 30분 이전시간을 구하면 000000을 리턴한다.
     *
     * @param min
     * @return pre_time, current_time
     */
    public static String[] getPreRangeMinute(int min) {
        String hhmmss = "000000";
        Calendar car = Calendar.getInstance();

        String time = new SimpleDateFormat("HHmmss").format(car.getTime());

        car.add(Calendar.MINUTE, min * (-1));

        String today = getToday();
        String day = new SimpleDateFormat("yyyyMMdd").format(car.getTime());
        if (today.equals(day)) {
            hhmmss = new SimpleDateFormat("HHmmss").format(car.getTime());
        }
        String[] result = new String[2];
        result[0] = hhmmss;
        result[1] = time;
        return result;
    }

    private static final GregorianCalendar calendar = new GregorianCalendar();

    public static final GregorianCalendar getGregorianCalendar() {
        return (GregorianCalendar) calendar.clone();
    }

    static final String CALENDAR_FORMATTER_FORMATS[] = {"%1$tY", "%1$ty",
            "%1$tm", "%1$td", "%1$tH", "%1$tM", "%1$tS", "%1$tL"};
    static final String CALENDAR_FORMATS[] = {"yyyy", "yy", "MM", "dd", "HH",
            "mm", "ss", "SSS"};
    static final int LENGTH_CALENDAR_FORMATS = CALENDAR_FORMATS.length;
    private static final char chars[] = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9'};
    static final int CALENDAR_FIELDS[] = {1, 1, 2, 5, 11, 12, 13, 14};
    private static final int lastDayTable[] = {31, 28, 31, 30, 31, 30, 31, 31,
            30, 31, 30, 31};
    private static Map yyyyMMStrMap;
    private static Map yyyy_MMStrMap;
    private static Map yyyyMMddStrMap;
    private static Map yyyy_MM_ddStrMap;
    private static Map yyyycMMcddStrMap;
    private static Map yyyyMMIntMap;
    private static Map yyyyMMddIntMap;
    private static final String calendarTable[] = {"1212122322121",
            "1212121221220", "1121121222120", "2112132122122", "2112112121220",
            "2121211212120", "2212321121212", "2122121121210", "2122121212120",
            "1232122121212", "1212121221220", "1121123221222", "1121121212220",
            "1212112121220", "2121231212121", "2221211212120", "1221212121210",
            "2123221212121", "2121212212120", "1211212232212", "1211212122210",
            "2121121212220", "1212132112212", "2212112112210", "2212211212120",
            "1221412121212", "1212122121210", "2112212122120", "1231212122212",
            "1211212122210", "2121123122122", "2121121122120", "2212112112120",
            "2212231212112", "2122121212120", "1212122121210", "2132122122121",
            "2112121222120", "1211212322122", "1211211221220", "2121121121220",
            "2122132112122", "1221212121120", "2121221212110", "2122321221212",
            "1121212212210", "2112121221220", "1231211221222", "1211211212220",
            "1221123121221", "2221121121210", "2221212112120", "1221241212112",
            "1212212212120", "1121212212210", "2114121212221", "2112112122210",
            "2211211412212", "2211211212120", "2212121121210", "2212214112121",
            "2122122121120", "1212122122120", "1121412122122", "1121121222120",
            "2112112122120", "2231211212122", "2121211212120", "2212121321212",
            "2122121121210", "2122121212120", "1212142121212", "1211221221220",
            "1121121221220", "2114112121222", "1212112121220", "2121211232122",
            "1221211212120", "1221212121210", "2121223212121", "2121212212120",
            "1211212212210", "2121321212221", "2121121212220", "1212112112210",
            "2223211211221", "2212211212120", "1221212321212", "1212122121210",
            "2112212122120", "1211232122212", "1211212122210", "2121121122210",
            "2212312112212", "2212112112120", "2212121232112", "2122121212110",
            "2212122121210", "2112124122121", "2112121221220", "1211211221220",
            "2121321122122", "2121121121220", "2122112112322", "1221212112120",
            "1221221212110", "2122123221212", "1121212212210", "2112121221220",
            "1211231212222", "1211211212220", "1221121121220", "1223212112121",
            "2221212112120", "1221221232112", "1212212122120", "1121212212210",
            "2112132212221", "2112112122210", "2211211212210", "2221321121212",
            "2212121121210", "2212212112120", "1232212122112", "1212122122120",
            "1121212322122", "1121121222120", "2112112122120", "2211231212122",
            "2121211212120", "2122121121210", "2124212112121", "2122121212120",
            "1212121223212", "1211212221220", "1121121221220", "2112132121222",
            "1212112121220", "2121211212120", "2122321121212", "1221212121210",
            "2121221212120", "1232121221212", "1211212212210", "2121123212221",
            "2121121212220", "1212112112220", "1221231211221", "2212211211220",
            "1212212121210", "2123212212121", "2112122122120", "1211212322212",
            "1211212122210", "2121121122120", "2212114112122", "2212112112120",
            "2212121211210", "2212232121211", "2122122121210", "2112122122120",
            "1231212122212", "1211211221220"};
    private static final int dateTable[] = {384, 355, 354, 384, 354, 354, 384,
            354, 355, 384, 355, 384, 354, 354, 383, 355, 354, 384, 355, 384,
            354, 355, 383, 354, 355, 384, 354, 355, 384, 354, 384, 354, 354,
            384, 355, 354, 384, 355, 384, 354, 354, 384, 354, 354, 385, 354,
            355, 384, 354, 383, 354, 355, 384, 355, 354, 384, 354, 384, 354,
            354, 384, 355, 355, 384, 354, 354, 384, 354, 384, 354, 355, 384,
            355, 354, 384, 354, 384, 354, 354, 384, 355, 354, 384, 355, 353,
            384, 355, 384, 354, 355, 384, 354, 354, 384, 354, 384, 354, 355,
            384, 355, 354, 384, 354, 384, 354, 354, 385, 354, 355, 384, 354,
            354, 383, 355, 384, 355, 354, 384, 354, 354, 384, 354, 355, 384,
            355, 384, 354, 354, 384, 354, 354, 384, 355, 384, 355, 354, 384,
            354, 354, 384, 354, 355, 384, 354, 384, 355, 354, 383, 355, 354,
            384, 355, 384, 354, 354, 384, 354, 354, 384, 355, 355, 384, 354};

    /**
     * 현재 년,월을 구한다.
     *
     * @param void
     * @return int
     */
    public static String getYearMonth() {
        String year = String.valueOf(getYear());
        String month = String.valueOf(getMonth());
        String yearMonth = year + month;
        return yearMonth;
    }

    /**
     * 현재 년도를 구한다.
     *
     * @param void
     * @return int
     */
    public static int getYear() {
        int year;
        GregorianCalendar todaysDate = new GregorianCalendar();
        year = todaysDate.get(Calendar.YEAR);
        return year;
    }

    /**
     * 현재 달(월)을 구한다.
     *
     * @param void
     * @return int
     */
    public static int getMonth() {
        int month;
        GregorianCalendar todaysDate = new GregorianCalendar();
        month = todaysDate.get(Calendar.MONTH);
        return month;
    }

    /**
     * 현재 일을 구한다.
     *
     * @param void
     * @return int
     */
    public static int getDay() {
        int day;
        GregorianCalendar todaysDate = new GregorianCalendar();
        day = todaysDate.get(Calendar.DATE);
        return day;
    }

    /**
     * 파라미터로받은 년부터  현재년도까지 구한다.
     *
     * @param
     * @return ArrayList years
     * @throws RowSetException
     * @cdate 2010.10.02
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List getStrComboYearsList(int startYear, int yearSelected) throws Exception {
        int current_year = DateUtils.getYear();
        List yearResultList = new ArrayList();
        log.debug("startYear  getStrComboYearsList " + startYear + "  yearSelected  " + yearSelected);

        for (int year = startYear; year <= current_year; year++) {

//				if(  startYear == yearSelected ) {
//					log.debug(startYear +"여기에 셀렉트디 " + yearSelected);
//					value += "<option value='"+year+"' selected>"+year+"년</option>";
//				}else{
//					value += "<option value='"+year+"' >"+year+"년</option>";
//				}
            yearResultList.add(year);

        }

        return yearResultList;

    }

    public static String getStrComboYearsToList(int startYear, int yearSelected) throws Exception {
        int current_year = DateUtils.getYear();
        String options = "";

        log.debug("startYear  getStrComboYearsToList  " + startYear + "  yearSelected  " + yearSelected);

        for (int year = startYear; year <= current_year; year++) {

            if (startYear == yearSelected) {
                log.debug(startYear + "여기에 셀렉트디 " + yearSelected);
                options += "<option value='" + year + "' selected>" + year + "년</option>";
            } else {
                options += "<option value='" + year + "' >" + year + "년</option>";
            }

        }

        return options;

    }

    /**
     * 월반환
     *
     * @param
     * @return RowSet
     * @throws Exception
     * @cdate 2010.10.02
     */
    public static String getStrComboMonthsList(int monthIss) throws Exception {
        String options = "";

        for (int month = 1; month <= 12; month++) {

            if (month == monthIss) {
                options += "<option value='" + month + "' selected>" + month + "월</option>";
            } else {
                options += "<option value='" + month + "'>" + month + "월</option>";
            }
        }

        return options;
    }


    /**
     * 해당인자값만큼 년(YYYY)를 더하거나 뺀다
     *
     * @param iYear
     * @return YYYY
     */
    public static String diffYearCal(int iYear) {
        return diffDateCal(iYear, "YYYY");
    }

    /**
     * 해당인자값만큼 월(MM)를 더하거나 뺀다
     *
     * @param iMonth
     * @return MM
     */
    public static String diffMonthCal(int iMonth) {
        return diffDateCal(iMonth, "MM");
    }

    /**
     * 해당인자값만큼 일(DD)를 더하거나 뺀다
     *
     * @param iDay
     * @return DD
     */
    public static String diffDayCal(int iDay) {
        return diffDateCal(iDay, "DD");
    }

    //내부계산용
    private static String diffDateCal(int iDate, String format) {
        Calendar temp = Calendar.getInstance();
        StringBuffer sbDate = new StringBuffer();

        if (format.toUpperCase().equals("YYYY")) {
            temp.add(Calendar.YEAR, iDate);
        } else if (format.toUpperCase().equals("MM")) {
            temp.add(Calendar.MONTH, iDate);
        } else if (format.toUpperCase().equals("DD")) {
            temp.add(Calendar.DATE, iDate);
        }

        int nYear = temp.get(Calendar.YEAR);
        int nMonth = temp.get(Calendar.MONTH) + 1;
        int nDay = temp.get(Calendar.DAY_OF_MONTH);

        if (format.toUpperCase().equals("YYYY")) {
            sbDate.append(nYear);
        }
        if (format.toUpperCase().equals("MM")) {
            if (nMonth < 10) {
                sbDate.append("0");
            }
            sbDate.append(nMonth);
        }
        if (format.toUpperCase().equals("DD")) {
            if (nDay < 10) {
                sbDate.append("0");
            }
            sbDate.append(nDay);
        }

        return sbDate.toString();
    }

    /**
     * 두날짜 크기비교(작은날짜)
     *
     * @param
     * @return String
     */
    public static final String minDate(String d1, String d2) throws Exception {

        String dateFormat = "yyyyMMdd";
        DateFormat df = new SimpleDateFormat(dateFormat);

        if (df.parse(d1) == null || df.parse(d2) == null)
            return null;
        if (df.parse(d1).getTime() - df.parse(d2).getTime() > 0L)
            return d2;
        else
            return d1;
    }

    /**
     * 두날짜 크기비교(큰날짜)
     *
     * @param
     * @return String
     */
    public static final String maxDate(String d1, String d2) throws Exception {

        String dateFormat = "yyyyMMdd";
        DateFormat df = new SimpleDateFormat(dateFormat);

        if (df.parse(d1) == null || df.parse(d2) == null)
            return null;
        if (df.parse(d1).getTime() - df.parse(d2).getTime() < 0L)
            return d2;
        else
            return d1;
    }

    /**
     * 윤년인지를 판단한다
     *
     * @param
     * @return
     */
    public static final boolean isLeapYear(String year) throws NumberFormatException {
        return isLeapYear(Integer.parseInt(year));
    }

    public static final boolean isLeapYear(int year) {
        GregorianCalendar gregorianCal = new GregorianCalendar();
        return gregorianCal.isLeapYear(year);
    }

    /**
     * 주어진 날짜 표현 문자열에 대해 yyyyMMdd 형태에 부합하며 유효한지 검사한다.
     *
     * @param
     * @return
     */
    public static final boolean isValid(String dateString) {
        return isValid(dateString, "yyyyMMdd");
    }

    /**
     * 주어진 날짜 표현 문자열에 대해 날짜 표현 포맷(format) 형태에 부합하며 유효한지 검사한다
     *
     * @param
     * @return
     */
    public static final boolean isValid(String dateTimeString, String format) {
        if (dateTimeString == null)
            return false;
        if (format == null)
            return false;
        Date date = null;
        try {
            date = parse(dateTimeString, format);
        } catch (ParseException e) {
            return false;
        }
        String str = null;
        try {
            str = format(date, format);
        } catch (ParseException pe) {
            return false;
        }
        return str.equals(dateTimeString);
    }

    /**
     * 생년월일로 만나이를 구하기(현재일자 기준)
     *
     * @param
     * @return
     */
    public static final String getFullAge(String strBirth) {
        return getFullAge(strBirth, getFormatString("yyyyMMdd"));
    }

    /**
     * 생년월일로 만나이를 구하기(기준일자 기준)
     *
     * @param
     * @return
     */
    public static final String getFullAge(String strBirth, String strApplyDate) {
        if (!isValid(strBirth) || !isValid(strApplyDate)
                || Integer.parseInt(strApplyDate) < Integer.parseInt(strBirth))
            return "";
        String year = strApplyDate.substring(0, 4);
        String month = strApplyDate.substring(4, 6);
        String day = strApplyDate.substring(6, 8);
        int ageYear = Integer.parseInt(year)
                - Integer.parseInt(strBirth.substring(0, 4));
        int ageMonth = Integer.parseInt(strBirth.substring(4, 6))
                - Integer.parseInt(month);
        int ageDay = Integer.parseInt(strBirth.substring(6, 8))
                - Integer.parseInt(day);
        int fullAge = 0;
        if (ageMonth < 0)
            fullAge = ageYear;
        else if (ageMonth == 0) {
            if (ageDay <= 0)
                fullAge = ageYear;
            else
                fullAge = ageYear - 1;
        } else {
            fullAge = ageYear - 1;
        }
        return (new StringBuffer(String.valueOf(fullAge))).toString();
    }

    public static final String getFormatString(String format) {
        String str = null;
        try {
            str = format(new Date(), format);
        } catch (ParseException parseexception) {
            log.error(parseexception.getMessage());
        }
        return str;
    }

    //내부계산용
    private static final String m01(String str1, String str2) {
        if (StringUtils.equalsOr(str2, "1|2|5|6"))
            str1 = (new StringBuffer("19")).append(str1).toString();
        else if (StringUtils.equalsOr(str2, "3|4|7|8"))
            str1 = (new StringBuffer("20")).append(str1).toString();
        else if (StringUtils.equalsOr(str2, "9|0"))
            str1 = (new StringBuffer("18")).append(str1).toString();
        return str1;
    }

    /**
     * 음력일자를 입력받아 양력일자를 반환
     *
     * @param
     * @return
     */
    public static final String lunarToSolar(String lunarDate) {
        int lunYear = Integer.parseInt(lunarDate.substring(0, 4));
        int lunMonth = Integer.parseInt(lunarDate.substring(4, 6));
        int lunDay = Integer.parseInt(lunarDate.substring(6, 8));
        if (lunYear < 1881 || lunYear > 2043)
            return null;
        boolean isLeap;
        if (lunYear % 4 == 0 && lunYear % 100 > 0 && lunYear % 400 == 0)
            isLeap = true;
        else
            isLeap = false;
        int yy = -1;
        int accDay = 0;
        if (lunYear != 1881) {
            yy = lunYear - 1882;
            for (int i = 0; i <= yy; i++) {
                for (int j = 0; j <= 12; j++)
                    accDay += Integer.parseInt(calendarTable[i].substring(j,
                            j + 1));

                if (calendarTable[i].substring(12, 13).equals("0"))
                    accDay += 336;
                else
                    accDay += 362;
            }

        }
        yy++;
        int n2 = lunMonth - 1;
        int mm = -1;
        int r = 2;
        while (r != 1) {
            mm++;
            if (Integer.parseInt(calendarTable[yy].substring(mm, mm + 1)) > 2) {
                accDay = accDay
                        + 26
                        + Integer.parseInt(calendarTable[yy].substring(mm,
                        mm + 1));
                n2++;
                continue;
            }
            if (mm == n2)
                break;
            accDay = accDay + 28
                    + Integer.parseInt(calendarTable[yy].substring(mm, mm + 1));
        }
        if (isLeap)
            accDay = accDay + 28
                    + Integer.parseInt(calendarTable[yy].substring(mm, mm + 1));
        accDay = accDay + lunDay + 29;
        yy = 1880;
        for (r = 2; r != 1; ) {
            yy++;
            mm = 365;
            if (yy % 4 == 0 && (yy % 100 != 0 || yy % 400 == 0))
                mm = 366;
            if (accDay <= mm)
                break;
            accDay -= mm;
        }

        int solYear = yy;
        lastDayTable[1] = mm - 337;
        yy = 0;
        for (r = 2; r != 1; ) {
            yy++;
            if (accDay <= lastDayTable[yy - 1])
                break;
            accDay -= lastDayTable[yy - 1];
        }

        int solMonth = yy;
        int solDay = accDay;
        String str = Integer.toString(solYear);
        if (solMonth < 10)
            str = (new StringBuffer(String.valueOf(str))).append("0").append(
                    Integer.toString(solMonth)).toString();
        else
            str = (new StringBuffer(String.valueOf(str))).append(
                    Integer.toString(solMonth)).toString();
        if (solDay < 10)
            str = (new StringBuffer(String.valueOf(str))).append("0").append(
                    Integer.toString(solDay)).toString();
        else
            str = (new StringBuffer(String.valueOf(str))).append(
                    Integer.toString(solDay)).toString();
        return str;
    }

    /**
     * 양력일자를 입력받아 음력일자를 반환
     *
     * @param
     * @return
     */
    public static final String solarToLunar(String solarDate) {
        int solYear = Integer.parseInt(solarDate.substring(0, 4));
        int solMonth = Integer.parseInt(solarDate.substring(4, 6));
        int solDay = Integer.parseInt(solarDate.substring(6, 8));
        if (solYear < 1881 || solYear > 2043)
            return null;
        int total_day = ((--solYear * 365 + solYear / 4) - solYear / 100)
                + solYear / 400;
        if (++solYear % 4 == 0 && solYear % 100 != 0 || solYear % 400 == 0)
            lastDayTable[1] = 29;
        else
            lastDayTable[1] = 28;
        int i;
        for (i = 0; i < solMonth - 1; i++)
            total_day += lastDayTable[i];

        total_day += solDay;
        int accDay = (total_day - 0xa7a5e) + 1;
        int buff_day = dateTable[0];
        for (i = 0; i <= 162; i++) {
            if (accDay <= buff_day)
                break;
            buff_day += dateTable[i + 1];
        }

        int lunYear = i + 1881;
        buff_day -= dateTable[i];
        accDay -= buff_day;
        int temp;
        if (!calendarTable[i].substring(12, 13).equals("0"))
            temp = 13;
        else
            temp = 12;
        int m2 = 0;
        for (int j = 0; j < temp - 1; j++) {
            int m1;
            if (Integer.parseInt(calendarTable[i].substring(j, j + 1)) <= 2) {
                m2++;
                m1 = Integer.parseInt(calendarTable[i].substring(j, j + 1)) + 28;
            } else {
                m1 = Integer.parseInt(calendarTable[i].substring(j, j + 1)) + 26;
            }
            if (accDay <= m1)
                break;
            accDay -= m1;
        }

        int lunMonth = m2;
        int lunDay = accDay;
        String str = Integer.toString(lunYear);
        if (lunMonth < 10)
            str = (new StringBuffer(String.valueOf(str))).append("0").append(
                    lunMonth).toString();
        else
            str = (new StringBuffer(String.valueOf(str))).append(lunMonth)
                    .toString();
        if (lunDay < 10)
            str = (new StringBuffer(String.valueOf(str))).append("0").append(
                    lunDay).toString();
        else
            str = (new StringBuffer(String.valueOf(str))).append(lunDay)
                    .toString();
        return str;
    }

    public static final int whichDay(String dateString) throws ParseException {
        return whichDay(dateString, "yyyyMMdd");
    }

    /*
     * "yyyyMMdd" 형태의 날짜 표현 문자열(dateString)에 대해 해당 요일 값을 반환.
     *  1: 일요일 (java.util.Calendar.SUNDAY 와 비교)
     *  2: 월요일 (java.util.Calendar.MONDAY 와 비교)
     *  3: 화요일 (java.util.Calendar.TUESDAY 와 비교)
     *  4: 수요일 (java.util.Calendar.WENDESDAY 와 비교)
     *  5: 목요일 (java.util.Calendar.THURSDAY 와 비교)
     *  6: 금요일 (java.util.Calendar.FRIDAY 와 비교)
     *   7: 토요일 (java.util.Calendar.SATURDAY 와 비교)
     */
    public static final String whichDay(String dateString, int printPattern)
            throws ParseException {
        String retValue = "";
        switch (whichDay(dateString)) {
            default:
                break;

            case 1: // '\001'
                if (printPattern == 1) {
                    retValue = String.valueOf(1);
                    break;
                }
                if (printPattern == 2) {
                    retValue = "\uC77C";
                    break;
                }
                if (printPattern == 3) {
                    retValue = "\uC77C\uC694\uC77C";
                    break;
                }
                if (printPattern == 4) {
                    retValue = "SUN";
                    break;
                }
                if (printPattern == 5)
                    retValue = "SUNDAY";
                break;

            case 2: // '\002'
                if (printPattern == 1) {
                    retValue = String.valueOf(2);
                    break;
                }
                if (printPattern == 2) {
                    retValue = "\uC6D4";
                    break;
                }
                if (printPattern == 3) {
                    retValue = "\uC6D4\uC694\uC77C";
                    break;
                }
                if (printPattern == 4) {
                    retValue = "MON";
                    break;
                }
                if (printPattern == 5)
                    retValue = "MONDAY";
                break;

            case 3: // '\003'
                if (printPattern == 1) {
                    retValue = String.valueOf(3);
                    break;
                }
                if (printPattern == 2) {
                    retValue = "\uD654";
                    break;
                }
                if (printPattern == 3) {
                    retValue = "\uD654\uC694\uC77C";
                    break;
                }
                if (printPattern == 4) {
                    retValue = "TUE";
                    break;
                }
                if (printPattern == 5)
                    retValue = "TUESDAY";
                break;

            case 4: // '\004'
                if (printPattern == 1) {
                    retValue = String.valueOf(4);
                    break;
                }
                if (printPattern == 2) {
                    retValue = "\uC218";
                    break;
                }
                if (printPattern == 3) {
                    retValue = "\uC218\uC694\uC77C";
                    break;
                }
                if (printPattern == 4) {
                    retValue = "WED";
                    break;
                }
                if (printPattern == 5)
                    retValue = "WEDNESDAY";
                break;

            case 5: // '\005'
                if (printPattern == 1) {
                    retValue = String.valueOf(5);
                    break;
                }
                if (printPattern == 2) {
                    retValue = "\uBAA9";
                    break;
                }
                if (printPattern == 3) {
                    retValue = "\uBAA9\uC694\uC77C";
                    break;
                }
                if (printPattern == 4) {
                    retValue = "THU";
                    break;
                }
                if (printPattern == 5)
                    retValue = "THURSDAY";
                break;

            case 6: // '\006'
                if (printPattern == 1) {
                    retValue = String.valueOf(6);
                    break;
                }
                if (printPattern == 2) {
                    retValue = "\uAE08";
                    break;
                }
                if (printPattern == 3) {
                    retValue = "\uAE08\uC694\uC77C";
                    break;
                }
                if (printPattern == 4) {
                    retValue = "FRI";
                    break;
                }
                if (printPattern == 5)
                    retValue = "FRIDAY";
                break;

            case 7: // '\007'
                if (printPattern == 1) {
                    retValue = String.valueOf(7);
                    break;
                }
                if (printPattern == 2) {
                    retValue = "\uD1A0";
                    break;
                }
                if (printPattern == 3) {
                    retValue = "\uD1A0\uC694\uC77C";
                    break;
                }
                if (printPattern == 4) {
                    retValue = "SAT";
                    break;
                }
                if (printPattern == 5)
                    retValue = "SATURDAY";
                break;
        }
        return retValue;
    }

    public static final int whichDay(String dateTimeString, String format)
            throws ParseException {
        Date date = getDate(dateTimeString, format);
        return getCalendar(date).get(7);
    }

    public static final Calendar getCalendar(Date date) {
        GregorianCalendar calendar = getGregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    public static final String getDate() {
        StringBuilder dateTime = new StringBuilder();
        String date = getToday();
        String time = getTime();
        return dateTime.append(date).append(time).toString();
    }

    public static final Date getDate(String dateTimeString) throws ParseException {
        return getDate(dateTimeString, "yyyyMMddHHmmss");
    }

    public static final Date getDate(String dateTimeString, String format) throws ParseException {
        return parse(dateTimeString, format);
    }

    @SuppressWarnings("unchecked")
    public static final Date parse(String dateString, String format) throws ParseException {
        if (format == null || format.length() < 1)
            throw new ParseException((new StringBuffer(
                    "Illegal pattern character. pattern=")).append(format)
                    .toString(), 0);
        if ("yyyyMM".equals(format) && dateString.length() == 6) {
            Date d = (Date) yyyyMMStrMap.get(dateString);
            if (d == null) {
                GregorianCalendar c = getGregorianCalendar();
                c.set(1, Integer.parseInt(dateString.substring(0, 4)));
                c.set(2, Integer.parseInt(dateString.substring(4, 6)) - 1);
                c.set(5, 1);
                d = c.getTime();
                yyyyMMStrMap.put(dateString, d);
            }
            return (Date) d.clone();
        }
        if ("yyyy-MM".equals(format) && dateString.length() == 7) {
            Date d = (Date) yyyy_MMStrMap.get(dateString);
            if (d == null) {
                GregorianCalendar c = getGregorianCalendar();
                c.set(1, Integer.parseInt(dateString.substring(0, 4)));
                c.set(2, Integer.parseInt(dateString.substring(5, 7)) - 1);
                c.set(5, 1);
                d = c.getTime();
                yyyy_MMStrMap.put(dateString, d);
            }
            return (Date) d.clone();
        }
        if ("yyyyMMdd".equals(format) && dateString.length() == 8) {
			/* 기존내역 주석처리
			Date d = (Date) yyyyMMddStrMap.get(dateString);
			if (d == null) {
				GregorianCalendar c = getGregorianCalendar();
				c.set(1, Integer.parseInt(dateString.substring(0, 4)));
				c.set(2, Integer.parseInt(dateString.substring(4, 6)) - 1);
				c.set(5, Integer.parseInt(dateString.substring(6, 8)));
				d = c.getTime();
				yyyyMMddStrMap.put(dateString, d);
			}
			return (Date) d.clone();
			*/

            GregorianCalendar c = getGregorianCalendar();
            c.set(1, Integer.parseInt(dateString.substring(0, 4)));
            c.set(2, Integer.parseInt(dateString.substring(4, 6)) - 1);
            c.set(5, Integer.parseInt(dateString.substring(6, 8)));
            return c.getTime();
        }
        if ("yyyy-MM-dd".equals(format) && dateString.length() == 10) {
            Date d = (Date) yyyy_MM_ddStrMap.get(dateString);
            if (d == null) {
                GregorianCalendar c = getGregorianCalendar();
                c.set(1, Integer.parseInt(dateString.substring(0, 4)));
                c.set(2, Integer.parseInt(dateString.substring(5, 7)) - 1);
                c.set(5, Integer.parseInt(dateString.substring(8, 10)));
                d = c.getTime();
                yyyy_MM_ddStrMap.put(dateString, d);
            }
            return (Date) d.clone();
        }
        if ("yyyy.MM.dd".equals(format) && dateString.length() == 10) {
            Date d = (Date) yyyycMMcddStrMap.get(dateString);
            if (d == null) {
                GregorianCalendar c = getGregorianCalendar();
                c.set(1, Integer.parseInt(dateString.substring(0, 4)));
                c.set(2, Integer.parseInt(dateString.substring(5, 7)) - 1);
                c.set(5, Integer.parseInt(dateString.substring(8, 10)));
                d = c.getTime();
                yyyycMMcddStrMap.put(dateString, d);
            }
            return (Date) d.clone();
        }
        GregorianCalendar c = getGregorianCalendar();
        String f = null;
        int index = 0;
        String v = null;
        int iv = 0;
        boolean setting = false;
        for (int i = 0; i < LENGTH_CALENDAR_FORMATS; i++) {
            f = CALENDAR_FORMATS[i];
            if (i == 3 && format.indexOf(f) == -1)
                c.set(CALENDAR_FIELDS[i], 1);
            do {
                if ((index = format.indexOf(f)) <= -1)
                    break;
                try {
                    v = dateString.substring(index, index + f.length());
                    iv = Integer.parseInt(v);
                } catch (Exception e) {
                    throw new ParseException((new StringBuffer(
                            "Illegal pattern/value character. pattern="))
                            .append(f).append(" value=").append(v).toString(),
                            0);
                }
                format = (new StringBuffer(String.valueOf(format.substring(0,
                        index)))).append(format.substring(index + f.length()))
                        .toString();
                dateString = (new StringBuffer(String.valueOf(dateString
                        .substring(0, index)))).append(
                        dateString.substring(index + f.length())).toString();
                if ("MM".equals(f))
                    c.set(CALENDAR_FIELDS[i], iv - 1);
                else if ("yy".equals(f) && iv == 0)
                    c.set(CALENDAR_FIELDS[i], 2000);
                else
                    c.set(CALENDAR_FIELDS[i], iv);
                if (!setting)
                    setting = true;
            } while (true);
        }

        if (!setting)
            throw new ParseException("Format is invalid", 0);
        else
            return c.getTime();
    }

    /**
     * 여러종류의 날짜포멧을 설정
     *
     * @param
     * @return
     */
    public static final String format(Date date, String format) throws ParseException {
        if (format == null || format.length() < 1) {
            throw new ParseException("Format is null", 0);
        }
        if ("yyyy".equals(format)) {
            GregorianCalendar c = getGregorianCalendar();
            c.setTime(date);
            int y = c.get(1);
            return String.valueOf(y);
        }
        if ("yyyyMM".equals(format)) {
            GregorianCalendar c = getGregorianCalendar();
            c.setTime(date);
            int y = c.get(1);
            int m = c.get(2) + 1;
            return String.valueOf(y * 100 + m);
        }
        if ("yyyy-MM".equals(format)) {
            GregorianCalendar c = getGregorianCalendar();
            c.setTime(date);
            int y = c.get(1);
            int m = c.get(2) + 1;
            char ret[] = new char[7];
            ret[0] = chars[y / 1000];
            ret[1] = chars[(y % 1000) / 100];
            ret[2] = chars[(y % 100) / 10];
            ret[3] = chars[y % 10];
            ret[4] = '-';
            ret[5] = chars[m / 10];
            ret[6] = chars[m % 10];
            return String.valueOf(ret);
        }
        if ("yyyyMMdd".equals(format)) {
            GregorianCalendar c = getGregorianCalendar();
            c.setTime(date);
            int y = c.get(1);
            int m = c.get(2) + 1;
            int d = c.get(5);
            return String.valueOf(y * 10000 + m * 100 + d);
        }
        if ("yyyy-MM-dd".equals(format)) {
            GregorianCalendar c = getGregorianCalendar();
            c.setTime(date);
            int y = c.get(1);
            int m = c.get(2) + 1;
            int d = c.get(5);
            char ret[] = new char[10];
            ret[0] = chars[y / 1000];
            ret[1] = chars[(y % 1000) / 100];
            ret[2] = chars[(y % 100) / 10];
            ret[3] = chars[y % 10];
            ret[4] = '-';
            ret[5] = chars[m / 10];
            ret[6] = chars[m % 10];
            ret[7] = '-';
            ret[8] = chars[d / 10];
            ret[9] = chars[d % 10];
            return String.valueOf(ret);
        }
        if ("yyyy/MM/dd".equals(format)) {
            GregorianCalendar c = getGregorianCalendar();
            c.setTime(date);
            int y = c.get(1);
            int m = c.get(2) + 1;
            int d = c.get(5);
            char ret[] = new char[10];
            ret[0] = chars[y / 1000];
            ret[1] = chars[(y % 1000) / 100];
            ret[2] = chars[(y % 100) / 10];
            ret[3] = chars[y % 10];
            ret[4] = '/';
            ret[5] = chars[m / 10];
            ret[6] = chars[m % 10];
            ret[7] = '/';
            ret[8] = chars[d / 10];
            ret[9] = chars[d % 10];
            return String.valueOf(ret);
        }
        if ("yyyy.MM.dd".equals(format)) {
            GregorianCalendar c = getGregorianCalendar();
            c.setTime(date);
            int y = c.get(1);
            int m = c.get(2) + 1;
            int d = c.get(5);
            char ret[] = new char[10];
            ret[0] = chars[y / 1000];
            ret[1] = chars[(y % 1000) / 100];
            ret[2] = chars[(y % 100) / 10];
            ret[3] = chars[y % 10];
            ret[4] = '.';
            ret[5] = chars[m / 10];
            ret[6] = chars[m % 10];
            ret[7] = '.';
            ret[8] = chars[d / 10];
            ret[9] = chars[d % 10];
            return String.valueOf(ret);
        }
        if ("yyyy-MM-dd HH:mm:ss.SSS".equals(format)) {
            GregorianCalendar c = getGregorianCalendar();
            c.setTime(date);
            int y = c.get(1);
            int m = c.get(2) + 1;
            int d = c.get(5);
            int h = c.get(11);
            int mi = c.get(12);
            int s = c.get(13);
            int ss = c.get(14);
            char ret[] = new char[23];
            ret[0] = chars[y / 1000];
            ret[1] = chars[(y % 1000) / 100];
            ret[2] = chars[(y % 100) / 10];
            ret[3] = chars[y % 10];
            ret[4] = '-';
            ret[5] = chars[m / 10];
            ret[6] = chars[m % 10];
            ret[7] = '-';
            ret[8] = chars[d / 10];
            ret[9] = chars[d % 10];
            ret[10] = ' ';
            ret[11] = chars[h / 10];
            ret[12] = chars[h % 10];
            ret[13] = ':';
            ret[14] = chars[mi / 10];
            ret[15] = chars[mi % 10];
            ret[16] = ':';
            ret[17] = chars[s / 10];
            ret[18] = chars[s % 10];
            ret[19] = '.';
            ret[20] = chars[ss / 100];
            ret[21] = chars[(ss % 100) / 10];
            ret[22] = chars[ss % 10];
            return String.valueOf(ret);
        }
        if ("yyyy-MM-dd HH:mm:ss".equals(format)) {
            GregorianCalendar c = getGregorianCalendar();
            c.setTime(date);
            int y = c.get(1);
            int m = c.get(2) + 1;
            int d = c.get(5);
            int h = c.get(11);
            int mi = c.get(12);
            int s = c.get(13);
            char ret[] = new char[23];
            ret[0] = chars[y / 1000];
            ret[1] = chars[(y % 1000) / 100];
            ret[2] = chars[(y % 100) / 10];
            ret[3] = chars[y % 10];
            ret[4] = '-';
            ret[5] = chars[m / 10];
            ret[6] = chars[m % 10];
            ret[7] = '-';
            ret[8] = chars[d / 10];
            ret[9] = chars[d % 10];
            ret[10] = ' ';
            ret[11] = chars[h / 10];
            ret[12] = chars[h % 10];
            ret[13] = ':';
            ret[14] = chars[mi / 10];
            ret[15] = chars[mi % 10];
            ret[16] = ':';
            ret[17] = chars[s / 10];
            ret[18] = chars[s % 10];
            return String.valueOf(ret);
        }
        if ("HHmmss".equals(format)) {
            GregorianCalendar c = getGregorianCalendar();
            c.setTime(date);
            int h = c.get(11);
            int mi = c.get(12);
            int s = c.get(13);
            int tmp = h * 10000 + mi * 100 + s;
            if (tmp < 0x186a0) {
                return (new StringBuffer("0")).append(String.valueOf(tmp))
                        .toString();
            } else {
                return String.valueOf(tmp);
            }
        }
        String f = null;
        int index = 0;
        boolean setting = false;
        for (int i = 0; i < LENGTH_CALENDAR_FORMATS; i++) {
            f = CALENDAR_FORMATS[i];
            do {
                if ((index = format.indexOf(f)) <= -1)
                    break;
                format = (new StringBuffer(String.valueOf(format.substring(0,
                        index)))).append(CALENDAR_FORMATTER_FORMATS[i]).append(
                        format.substring(index + f.length())).toString();
                if (!setting)
                    setting = true;
            } while (true);
        }

        if (!setting) {
            throw new ParseException("Format is invalid", 0);
        } else {
            return String.format(format, new Object[]{date});
        }
        //return null;
    }

}
