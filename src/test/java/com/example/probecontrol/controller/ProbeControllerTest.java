package com.example.probecontrol.controller;

import com.example.probecontrol.model.Direction;
import com.example.probecontrol.model.Position;
import com.example.probecontrol.model.ProbeState;
import com.example.probecontrol.service.ProbeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProbeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProbeService probeService;

    @InjectMocks
    private ProbeController probeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(probeController).build();
    }

    @Test
    public void testSendCommandsSuccess() throws Exception {
        var position = new Position(1, 2);
        var direction = Direction.NORTH;
        var visited = List.of(new Position(0, 0), new Position(1, 2));
        var state = new ProbeState(position, direction, visited);

        when(probeService.executeCommands(anyList())).thenReturn(state);

        mockMvc.perform(post("/probe/commands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"forward\", \"left\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Commands executed successfully"))
                .andExpect(jsonPath("$.position.x").value(1))
                .andExpect(jsonPath("$.position.y").value(2))
                .andExpect(jsonPath("$.direction").value("NORTH"))
                .andExpect(jsonPath("$.visited").isArray());
    }

    @Test
    public void testSendCommandsError() throws Exception {
        var position = new Position(0, 0);
        var direction = Direction.NORTH;
        var visited = List.of(new Position(0, 0));
        var state = new ProbeState(position, direction, visited);

        when(probeService.executeCommands(anyList())).thenThrow(new IllegalArgumentException("Invalid command"));
        when(probeService.getCurrentState()).thenReturn(state);

        mockMvc.perform(post("/probe/commands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"INVALID\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Invalid command"))
                .andExpect(jsonPath("$.position.x").value(0))
                .andExpect(jsonPath("$.position.y").value(0))
                .andExpect(jsonPath("$.direction").value("NORTH"))
                .andExpect(jsonPath("$.visited").isArray());
    }

    @Test
    public void testGetSummary() throws Exception {
        var position = new Position(1, 2);
        var direction = Direction.NORTH;
        var visited = List.of(new Position(0, 0), new Position(1, 2));
        var state = new ProbeState(position, direction, visited);

        when(probeService.getCurrentState()).thenReturn(state);

        mockMvc.perform(get("/probe/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Summary of visited coordinates"))
                .andExpect(jsonPath("$.position.x").value(1))
                .andExpect(jsonPath("$.position.y").value(2))
                .andExpect(jsonPath("$.direction").value("NORTH"))
                .andExpect(jsonPath("$.visited").isArray());
    }
}