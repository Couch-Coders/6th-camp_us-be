package couch.camping.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    //TEST
    @GetMapping("/test12")
    public String test() {
        return "okk";
    }
}
