package kr.wikidocs.common.util;

import jakarta.servlet.http.HttpServletRequest;
import net.sf.javainetlocator.InetAddressLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.util.Locale;

/**
 * 시스템 유틸
 */
public class SystemUtils {

    private final static Log log = LogFactory.getLog(SystemUtils.class.getName());

    /**
     * claspath 경로
     */
    public static String getClassPath() {
        String classPath = System.getProperty("java.class.path");
        return classPath;
    }

    /**
     * 접속자 IP
     */
    public static String getUserIP(HttpServletRequest request) {

        String ip = null;

        try {
            ip = request.getHeader("X-FORWARDED-FOR");
            if (ip == null || ip.length() == 0) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0) {
                ip = request.getRemoteAddr();
            }
            if (ip == null || ip.length() == 0) {
                InetAddress local = InetAddress.getLocalHost();
                ip = local.getHostAddress();
            } else {
                if (ip.startsWith("0")) {
                    InetAddress local = InetAddress.getLocalHost();
                    ip = local.getHostAddress();
                }
            }
            if (ip != null && ip.length() > 0) {
                if (ip.split(",").length > 1) {
                    ip = ip.split(",")[0];
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ip;
    }

    /**
     * 접속자 OS
     */
    public static String getUserOS(HttpServletRequest request) {

        String os = null;

        try {
            String agent = request.getHeader("User-Agent");

            if (agent.indexOf("NT 6.0") != -1) {
                os = "Windows Vista/Server 2008";
            } else if (agent.indexOf("NT 5.2") != -1) {
                os = "Windows Server 2003";
            } else if (agent.indexOf("NT 5.1") != -1) {
                os = "Windows XP";
            } else if (agent.indexOf("NT 5.0") != -1) {
                os = "Windows 2000";
            } else if (agent.indexOf("NT") != -1) {
                os = "Windows NT";
            } else if (agent.indexOf("9x 4.90") != -1) {
                os = "Windows Me";
            } else if (agent.indexOf("98") != -1) {
                os = "Windows 98";
            } else if (agent.indexOf("95") != -1) {
                os = "Windows 95";
            } else if (agent.indexOf("Win16") != -1) {
                os = "Windows 3.x";
            } else if (agent.indexOf("Windows") != -1) {
                os = "Windows";
            } else if (agent.indexOf("Linux") != -1) {
                os = "Linux";
            } else if (agent.indexOf("Macintosh") != -1) {
                os = "Macintosh";
            } else {
                os = "";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return os;
    }

    /**
     * 접속자 Browser
     */
    public static String getUserBrowser(HttpServletRequest request) {

        String browser = null;

        try {
            String agent = request.getHeader("User-Agent");

            if (agent != null) {
                if (agent.indexOf("Mobile") > -1) {
                    if (agent.indexOf("iPhone") > -1) {
                        if (agent.indexOf("Morpheus") > -1) {
                            browser = "iPhone";
                        } else {
                            browser = "MWeb-iPhone";
                        }
                    } else if (agent.indexOf("Android") > -1) {
                        if (agent.indexOf("Morpheus") > -1) {
                            browser = "Android";
                        } else {
                            browser = "MWeb-Android";
                        }
                    } else if (agent.indexOf("Trident") > -1) {
                        browser = "MWeb-MSIE";
                    } else if (agent.indexOf("Chrome") > -1) {
                        browser = "MWeb-Chrome";
                    } else if (agent.indexOf("Opera") > -1) {
                        browser = "MWeb-Opera";
                    } else {
                        browser = "MWeb";
                    }
                } else {
                    if (agent.indexOf("Trident") > -1) {
                        browser = "MSIE";
                    } else if (agent.indexOf("Chrome") > -1) {
                        browser = "Chrome";
                    } else if (agent.indexOf("Opera") > -1) {
                        browser = "Opera";
                    } else {
                        browser = "";
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return browser;
    }

    /**
     * 접속자 국가
     */
    public static String getUserCountry(HttpServletRequest request) {

        String country = null;

        try {

//			String lang = request.getHeader("accept-language");
//			country = lang.substring(0, 2).toUpperCase();

            String ip = getUserIP(request);
            Locale locale = InetAddressLocator.getLocale(ip);

//			log.debug(locale.getDisplayCountry());
//			log.debug(locale.getCountry());
//			log.debug(locale.getDisplayLanguage());
//			log.debug(locale.getDisplayName());

            country = locale.getCountry();
            if (country != null && country.length() > 4) {
                country = country.substring(0, 4);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return country;
    }
}
