package com.example.probecontrol.service;

import com.example.probecontrol.model.Direction;
import com.example.probecontrol.model.Position;
import com.example.probecontrol.model.ProbeState;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProbeService {
    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;
    private static final Set<String> OBSTACLES = new HashSet<>(Set.of("2,2", "3,5", "7,8"));

    private final ProbeState probeState = new ProbeState();

    public ProbeState executeCommands(List<String> commands) {
        for (String command : commands) {
            moveProbe(command);
        }
        return probeState;
    }

    public ProbeState getCurrentState() {
        return probeState;
    }

    private void moveProbe(String command) {
        Position current = probeState.getPosition();
        Direction direction = probeState.getDirection();
        int x = current.x();
        int y = current.y();

        switch (command) {
            case "forward":
                switch (direction) {
                    case NORTH -> y++;
                    case EAST -> x++;
                    case SOUTH -> y--;
                    case WEST -> x--;
                }
                break;
            case "backward":
                switch (direction) {
                    case NORTH -> y--;
                    case EAST -> x--;
                    case SOUTH -> y++;
                    case WEST -> x++;
                }
                break;
            case "left":
                probeState.setDirection(direction.left());
                return;
            case "right":
                probeState.setDirection(direction.right());
                return;
            default:
                throw new IllegalArgumentException("Invalid command");
        }

        if (!isWithinGrid(x, y) || isObstacle(x, y)) {
            throw new IllegalArgumentException("Invalid move: Out of bounds or obstacle detected");
        }

        probeState.setPosition(new Position(x, y));
    }

    private boolean isWithinGrid(int x, int y) {
        return x >= 0 && x < GRID_WIDTH && y >= 0 && y < GRID_HEIGHT;
    }

    private boolean isObstacle(int x, int y) {
        return OBSTACLES.contains(new Position(x, y).toString());
    }
}