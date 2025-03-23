package com.example.probecontrol.controller;

import com.example.probecontrol.model.Direction;
import com.example.probecontrol.model.Position;
import com.example.probecontrol.service.ProbeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/probe")
public class ProbeController {
    private final ProbeService probeService;

    public ProbeController(ProbeService probeService) {
        this.probeService = probeService;
    }

    @PostMapping("/commands")
    public Response sendCommands(@RequestBody List<String> commands) {
        try {
            var state = probeService.executeCommands(commands);
            return new Response("success", "Commands executed successfully",
                    state.getPosition(), state.getDirection(), state.getVisited());
        } catch (IllegalArgumentException e) {
            var currentState = probeService.getCurrentState();
            return new Response("error", e.getMessage(),
                    currentState.getPosition(), currentState.getDirection(), currentState.getVisited());
        }
    }

    @GetMapping("/summary")
    public Response getSummary() {
        var state = probeService.getCurrentState();
        return new Response("success", "Summary of visited coordinates",
                state.getPosition(), state.getDirection(), state.getVisited());
    }

    private record Response(
            String status,
            String message,
            Position position,
            Direction direction,
            List<Position> visited
    ) {}
}