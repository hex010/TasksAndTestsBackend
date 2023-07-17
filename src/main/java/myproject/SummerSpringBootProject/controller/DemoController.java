package myproject.SummerSpringBootProject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RequestMapping("/api/v1")
@RestController

public class DemoController {

    @GetMapping ("/hiuser")
    public ResponseEntity<String> greetingAuth()
    {
        return ResponseEntity.ok("Hi user");
    }

    @GetMapping ("/hiadmin")
    public ResponseEntity<String> greetingAuth2()
    {
        return ResponseEntity.ok("Hi admin");
    }

    @GetMapping ("/hirandom")
    public ResponseEntity<String> greetingAuth3()
    {
        return ResponseEntity.ok("Hi random");
    }
}
