package com.example.probecontrol.controller;

import com.example.probecontrol.controller.ProbeController;
import com.example.probecontrol.model.Direction;
import com.example.probecontrol.model.Position;
import com.example.probecontrol.model.ProbeState;
import com.example.probecontrol.service.ProbeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProbeControllerTest {

    @Mock
    private ProbeService probeService;

    @InjectMocks
    private ProbeController probeController;

    @Test
    void testSendCommands() {
        List<String> commands = List.of("forward");
        ProbeState mockState = new ProbeState(new Position(1, 0), Direction.NORTH, List.of(new Position(0, 0), new Position(1, 0)));

        when(probeService.executeCommands(commands)).thenReturn(mockState);

        ProbeController.Response response = probeController.sendCommands(commands);
        assertEquals("success", response.status());
        assertEquals(new Position(1, 0), response.position());
        assertEquals(Direction.NORTH, response.direction());
    }

    @Test
    void testGetSummary() {
        ProbeState mockState = new ProbeState(new Position(0, 0), Direction.NORTH, List.of(new Position(0, 0)));
        when(probeService.getCurrentState()).thenReturn(mockState);

        ProbeController.Response response = probeController.getSummary();
        assertEquals("success", response.status());
        assertEquals(new Position(0, 0), response.position());
        assertEquals(Direction.NORTH, response.direction());
    }
}
