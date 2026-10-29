package com.collaborativecanvas.server.controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController public class HealthRestController { @GetMapping("/api/v1/health") public Map<String,String> health(){return Map.of("status","UP");} }
