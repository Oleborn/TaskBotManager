package oleborn.taskbot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    @GetMapping("/debug/env")
    public Map<String, String> debugEnvironment() {
        Map<String, String> env = new HashMap<>();
        env.put("DB_PASSWORD", System.getenv("DB_PASSWORD"));
        env.put("BOT_TOKEN", System.getenv("BOT_TOKEN"));
        return env;
    }
}
