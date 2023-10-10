package exkc.api.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = "/version")
    public ResponseEntity<String> version() {
        return new ResponseEntity<>("1.0.0", HttpStatus.OK);
    }

}
