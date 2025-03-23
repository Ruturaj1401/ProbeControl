package com.example.probecontrol;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/probe")
public class ProbeController {

    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;
    private static final Set<String> OBSTACLES = new HashSet<>(Set.of("2,2", "3,5", "7,8"));

    private static class ProbeState {
        private int x = 0;
        private int y = 0;
        private String direction = "north";
        private List<int[]> visited = new ArrayList<>();

        public ProbeState() {
            visited.add(new int[]{x, y});
        }

        public int getX() { return x; }
        public int getY() { return y; }
        public String getDirection() { return direction; }
        public List<int[]> getVisited() { return visited; }

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
            this.visited.add(new int[]{x, y});
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }
    }

    private final ProbeState probeState = new ProbeState();

    private boolean isWithinGrid(int x, int y) {
        return x >= 0 && x < GRID_WIDTH && y >= 0 && y < GRID_HEIGHT;
    }

    private boolean isObstacle(int x, int y) {
        return OBSTACLES.contains(x + "," + y);
    }

    private void moveProbe(String command) {
        int x = probeState.getX();
        int y = probeState.getY();
        String direction = probeState.getDirection();

        switch (command) {
            case "forward":
                if (direction.equals("north")) y++;
                else if (direction.equals("east")) x++;
                else if (direction.equals("south")) y--;
                else if (direction.equals("west")) x--;
                break;
            case "backward":
                if (direction.equals("north")) y--;
                else if (direction.equals("east")) x--;
                else if (direction.equals("south")) y++;
                else if (direction.equals("west")) x++;
                break;
            case "left":
                probeState.setDirection(switch (direction) {
                    case "north" -> "west";
                    case "west" -> "south";
                    case "south" -> "east";
                    case "east" -> "north";
                    default -> throw new IllegalArgumentException("Invalid direction");
                });
                return;
            case "right":
                probeState.setDirection(switch (direction) {
                    case "north" -> "east";
                    case "east" -> "south";
                    case "south" -> "west";
                    case "west" -> "north";
                    default -> throw new IllegalArgumentException("Invalid direction");
                });
                return;
            default:
                throw new IllegalArgumentException("Invalid command");
        }

        if (!isWithinGrid(x, y) || isObstacle(x, y)) {
            throw new IllegalArgumentException("Invalid move: Out of bounds or obstacle detected");
        }

        probeState.setPosition(x, y);
    }

    @PostMapping("/commands")
    public Response sendCommands(@RequestBody List<String> commands) {
        for (String command : commands) {
            try {
                moveProbe(command);
            } catch (IllegalArgumentException e) {
                return new Response("error", e.getMessage(), probeState.getX(), probeState.getY(), probeState.getDirection(), probeState.getVisited());
            }
        }
        return new Response("success", "Commands executed successfully", probeState.getX(), probeState.getY(), probeState.getDirection(), probeState.getVisited());
    }

    @GetMapping("/summary")
    public Response getSummary() {
        return new Response("success", "Summary of visited coordinates", probeState.getX(), probeState.getY(), probeState.getDirection(), probeState.getVisited());
    }

    private static class Response {
        private final String status;
        private final String message;
        private final int[] position;
        private final String direction;
        private final List<int[]> visited;

        public Response(String status, String message, int x, int y, String direction, List<int[]> visited) {
            this.status = status;
            this.message = message;
            this.position = new int[]{x, y};
            this.direction = direction;
            this.visited = visited;
        }

        public String getStatus() { return status; }
        public String getMessage() { return message; }
        public int[] getPosition() { return position; }
        public String getDirection() { return direction; }
        public List<int[]> getVisited() { return visited; }
    }
}