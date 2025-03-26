package com.example.probecontrol.service;

import com.example.probecontrol.model.*;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProbeServiceTest {
    private ProbeService probeService;

    @BeforeEach
    void setUp() {
        probeService = new ProbeService(); // Fresh instance for each test
    }

    // Test 1: Move forward once
    @Test
    @DisplayName("Move forward from (0,0) should go to (0,1)")
    void moveForward_UpdatesYCoordinate() {
        probeService.executeCommands(List.of("forward"));
        assertEquals(new Position(0, 1), probeService.getCurrentState().getPosition());
    }

    // Test 2: Turn right from NORTH
    @Test
    @DisplayName("Turn right from NORTH should face EAST")
    void turnRight_ChangesDirectionToEast() {
        probeService.executeCommands(List.of("right"));
        assertEquals(Direction.EAST, probeService.getCurrentState().getDirection());
    }

    // Test 3: Hit an obstacle
    @Test
    @DisplayName("Moving into (2,2) should throw an error")
    void moveIntoObstacle_ThrowsError() {
        // Adjust commands to ensure they lead to the obstacle at (2,2)
        List<String> commands = List.of("right", "forward", "forward", "left", "forward", "forward");
        assertThrows(IllegalArgumentException.class,
                () -> probeService.executeCommands(commands));
    }
}