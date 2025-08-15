package com.example.demo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final RatelimiterService rateLimiterService;

    public ApiController(RatelimiterService rateLimiterService){
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/data")
    public ResponseEntity<ApiResponse> getData(@RequestParam String userId){
        if(!rateLimiterService.isAllowed(userId)){
            ApiResponse response = new ApiResponse("error", "Rate limit exceeded. Wait a max of 60 secs before trying again!");
            return ResponseEntity.status(429).body(response);

        }
        ApiResponse response = new ApiResponse("success","Heres your data!");
        return ResponseEntity.ok(response);
    }
}
