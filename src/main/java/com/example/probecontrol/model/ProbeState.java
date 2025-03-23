package com.example.probecontrol.model;

import java.util.ArrayList;
import java.util.List;

public class ProbeState {
    private Position position = new Position(0, 0);
    private Direction direction = Direction.NORTH;
    private final List<Position> visited = new ArrayList<>();

    public ProbeState() {
        visited.add(position);
    }

    public Position getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Position> getVisited() {
        return new ArrayList<>(visited);
    }

    public void setPosition(Position position) {
        this.position = position;
        this.visited.add(position);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}