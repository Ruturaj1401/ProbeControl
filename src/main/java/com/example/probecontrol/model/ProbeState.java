package com.example.probecontrol.model;

import java.util.List;

public class ProbeState {
    private Position position;
    private Direction direction;
    private List<Position> visited;

    public ProbeState(Position position, Direction direction, List<Position> visited) {
        this.position = position;
        this.direction = direction;
        this.visited = visited;
    }

    // Getters and setters
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public List<Position> getVisited() {
        return visited;
    }

    public void setVisited(List<Position> visited) {
        this.visited = visited;
    }
}