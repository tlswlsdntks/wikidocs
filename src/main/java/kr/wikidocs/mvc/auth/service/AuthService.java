package kr.wikidocs.mvc.auth.service;

import jakarta.servlet.http.HttpSession;
import kr.wikidocs.common.exception.ComException;
import kr.wikidocs.common.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;

/**
 * 사용자 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * 로그인
     */
    public void login(HashMap<String, Object> param) throws ComException {
        HttpSession session = null;
        try {
            // TODO: 하드코딩 제거, 비밀번호 암호화
            HashMap<String, Object> userMap = new HashMap<String, Object>();
            userMap.put("userId", "auth");
            userMap.put("userNm", "관리자");
            userMap.put("userEmail", "auth@copr.kr");
            userMap.put("userTelno", "01012341234");
            userMap.put("userStatus", "TEST");
            userMap.put("userType", "TEST");
            userMap.put("acctLckdYn", "N");
            log.debug("로그인 사용자정보 = {}", userMap);

            if (userMap == null) {
                throw new ComException("로그인ID 또는 비밀번호를 확인하세요.");
            } else if (userMap.get("acctLckdYn").equals("Y")) {
                throw new ComException("해당 계정은 잠김상태입니다. \n관리자에게 계정사용 문의 바랍니다.");
            } else {
                session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true);
                session.setMaxInactiveInterval(-1);
                //session.setMaxInactiveInterval(86400);
                // 로그인여부
                session.setAttribute("isLogin", true);
                // 로그인시간
                session.setAttribute("loginTime", DateUtils.getToday("yyyy.MM.dd HH:mm:ss.SSS"));
                // 사용자ID
                session.setAttribute("userId", userMap.get("userId"));
                // 사용자명
                session.setAttribute("userNm", userMap.get("userNm"));
                // 이메일
                session.setAttribute("userEmail", userMap.get("userEmail"));
                // 전화번호
                session.setAttribute("userTelno", userMap.get("userTelno"));
                // 사용자상태
                session.setAttribute("userStatus", userMap.get("userStatus"));
                // 사용자유형
                session.setAttribute("userType", userMap.get("userType"));
                // Token
                // session.setAttribute("token", param.get("token"));
            }
        } catch (Exception e) {
            throw new ComException(e);
        }
    }
}
