package com.example.probecontrol.model;

public record Position(int x, int y) {
    @Override
    public String toString() {
        return x + "," + y;
    }
}