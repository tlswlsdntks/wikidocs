package kr.wikidocs.api.controller;

import kr.wikidocs.config.vo.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    @RequestMapping("/test")
    public ResponseEntity<Result> test(@RequestParam HashMap<String, Object> param) throws Exception {
        log.debug("param: {}", param);
        Result result = new Result();
        try {
            HashMap<String, Object> rstData = new HashMap<>();
            rstData.put("TEST", "SUCCESS");
            result.setData(rstData);
        } catch (Exception e) {
            result.setError(-1, e.getMessage());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
