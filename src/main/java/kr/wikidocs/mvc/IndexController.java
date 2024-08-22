package kr.wikidocs.mvc;

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
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String page(ModelAndView mv, @RequestParam HashMap<String, Object> param) throws ComException {
        log.debug("call => /");
        return "forward:/main";
    }

}
