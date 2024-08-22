package kr.wikidocs.mvc.demo.controller;

import kr.wikidocs.common.exception.ComException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

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

}
