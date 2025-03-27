package com.example.probecontrol.service;

import com.example.probecontrol.model.Direction;
import com.example.probecontrol.model.Position;
import com.example.probecontrol.model.ProbeState;
import com.example.probecontrol.service.ProbeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class ProbeServiceTest {
    private ProbeService probeService;

    @BeforeEach
    void setUp() {
        probeService = new ProbeService();
    }

    @Test
    void testExecuteCommandsForward() {
        List<String> commands = List.of("forward");
        ProbeState state = probeService.executeCommands(commands);
        assertEquals(new Position(0, 1), state.getPosition());
        assertEquals(Direction.NORTH, state.getDirection());
    }

    @Test
    void testExecuteCommandsTurnLeft() {
        List<String> commands = List.of("left");
        ProbeState state = probeService.executeCommands(commands);
        assertEquals(Direction.WEST, state.getDirection());
    }

    @Test
    void testExecuteCommandsTurnRight() {
        List<String> commands = List.of("right");
        ProbeState state = probeService.executeCommands(commands);
        assertEquals(Direction.EAST, state.getDirection());
    }
}
