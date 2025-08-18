package com.example.demo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final RatelimiterService rateLimiterService;
    private final UserRepository userRepository;
    
    public ApiController(RatelimiterService rateLimiterService, UserRepository userRepository){
        this.rateLimiterService = rateLimiterService;
        this.userRepository = userRepository;
    }
    @PostMapping("/data")
    public ResponseEntity<ApiResponse> createData(@RequestParam String userId, @RequestBody String data){
        try {
            // Check if user already exists, if so update, otherwise create new
            Optional<User> existingUser = userRepository.findById(userId);
            
            User user;
            if (existingUser.isPresent()) {
                user = existingUser.get();
                user.setData(data); // This will also update the updatedAt timestamp
            } else {
                user = new User(userId, data);
            }
            
            userRepository.save(user);
            
            ApiResponse response = new ApiResponse("success", "Data created/updated for user: " + userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("error", "Failed to save data: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/data")
    public ResponseEntity<ApiResponse> getData(@RequestParam String userId){
        if(!rateLimiterService.isAllowed(userId)){
            ApiResponse response = new ApiResponse("error", "Rate limit exceeded. Wait a max of 60 secs before trying again!");
            return ResponseEntity.status(429).body(response);
        }
        
        try {
            Optional<User> user = userRepository.findById(userId);
            
            if (user.isPresent()) {
                String data = user.get().getData();
                ApiResponse response = new ApiResponse("success", "Here's your data: " + data);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse response = new ApiResponse("error", "No data found for user: " + userId);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("error", "Failed to retrieve data: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
