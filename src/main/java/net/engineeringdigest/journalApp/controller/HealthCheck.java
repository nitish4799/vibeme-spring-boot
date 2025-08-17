package net.engineeringdigest.journalApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/check")
    public ResponseEntity<String> fun() {
        return ResponseEntity
                .ok()
                .body("RunningSmoothly");
    }
}
