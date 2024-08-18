package kr.wikidocs.mvc.demo.controller;

import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.wikidocs.common.exception.ComException;
import kr.wikidocs.config.vo.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/demo")
public class DemoController {

    @GetMapping({
            "/index",
            "/404",
            "/blank",
            "/buttons",
            "/cards",
            "/charts",
            "/forgot-password",
            "/index",
            "/login",
            "/register",
            "/tables",
            "/utilities-animation",
            "/utilities-border",
            "/utilities-color",
            "/utilities-other"
    })
    public ModelAndView page(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @RequestParam HashMap<String, Object> param) throws ComException {
        log.debug("param: {}", param);
        mv.addObject("result", param);
        return mv;
    }

}
