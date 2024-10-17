package kr.wikidocs.mvc.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.wikidocs.common.annotation.NoSession;
import kr.wikidocs.common.exception.ComException;
import kr.wikidocs.common.session.UserSession;
import kr.wikidocs.common.util.PathUtils;
import kr.wikidocs.config.vo.Path;
import kr.wikidocs.config.vo.Result;
import kr.wikidocs.mvc.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping({
            "/login",
            "/register",
            "/forgot-password"
    })
    public String login(HttpServletRequest req, HttpServletResponse res, Model model, @RequestParam HashMap<String, Object> param) throws ComException {
        log.debug("call => " + req.getRequestURI());
        /**
         * 공통 경로 추가
         */
        PathUtils.addCommonPath(req, model);
        return req.getRequestURI();
    }

    @GetMapping({
            "/index",
            "/404",
            "/blank",
            "/buttons",
            "/cards",
            "/charts",
            "/tables",
            "/utilities-animation",
            "/utilities-border",
            "/utilities-color",
            "/utilities-other"
    })
    public ModelAndView page(ModelAndView mv, @RequestParam HashMap<String, Object> param) throws ComException {
        log.debug("param: {}", param);
        mv.addObject("result", param);
        return mv;
    }

    /**
     * 사용자 로그인
     */
    @NoSession
    @PostMapping("/login/process")
    public ResponseEntity<Result> login(ModelAndView mv, @RequestParam HashMap<String, Object> param) throws ComException {
        log.debug("Call ==> /login/process");
        Result result = new Result();
        try {
            authService.login(param);
            // Session
            UserSession session = new UserSession();
            if (session.isLogin()) {
                // Session 정보리턴
                HashMap<String, Object> outData = new HashMap<String, Object>();
                outData.put("userId", session.getUserId());
                outData.put("userNm", session.getUserNm());
                result.setData(outData);
            } else {
                result.setError(-1, "처리중 오류가 발생하였습니다.");
            }
        } catch (Exception e) {
            result.setError(-1, e.getMessage());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 사용자 로그아웃
     */
    @GetMapping("/logout")
    public void logout(HttpServletRequest req, HttpServletResponse res, UserSession session) throws Exception {
        try {
            if (session != null) {
                session.removeSession();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            res.sendRedirect(req.getContextPath() + "/auth/login");
        }
    }

}
