package com.example.demo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final RatelimiterService rateLimiterService;
    private final Map<String, String> userData = new ConcurrentHashMap<>();
    public ApiController(RatelimiterService rateLimiterService){
        this.rateLimiterService = rateLimiterService;
    }
    @PostMapping("/data")
    public ResponseEntity<ApiResponse> createData(@RequestParam String userId, @RequestBody String data){
        userData.put(userId,data);
        ApiResponse response = new ApiResponse("success", "Data created for user : " + userId);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/data")
    public ResponseEntity<ApiResponse> getData(@RequestParam String userId){
        if(!rateLimiterService.isAllowed(userId)){
            ApiResponse response = new ApiResponse("error", "Rate limit exceeded. Wait a max of 60 secs before trying again!");
            return ResponseEntity.status(429).body(response);

        }
        String data = userData.getOrDefault(userId, userId);
        ApiResponse response = new ApiResponse("success","Heres your data: " +data);
        return ResponseEntity.ok(response);
    }
}
