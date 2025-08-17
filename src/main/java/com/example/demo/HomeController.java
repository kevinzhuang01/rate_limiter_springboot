package com.example.demo;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "RateLimiter API Homepage";
    }
}
