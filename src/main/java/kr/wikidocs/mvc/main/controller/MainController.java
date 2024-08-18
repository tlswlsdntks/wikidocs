package kr.wikidocs.mvc.main.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.wikidocs.common.exception.ComException;
import kr.wikidocs.config.vo.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping({
            "/main"
    })
    public String page(ModelAndView mv, @RequestParam HashMap<String, Object> param) throws ComException {
        log.debug("call => /main");
        mv.addObject("result", param);
        return "main/home";
    }

}
