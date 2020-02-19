package com.company;

public class Room {
    private int id;
    private int numberOfBeds;
    private String location;
    private String roomSize;


    public Room(int id, String location, String roomSize, int numberOfBeds) {
        this.id = id;
        this.location = location;
        this.roomSize = roomSize;
        this.numberOfBeds = numberOfBeds;
    }

    public int getId() {
        return id;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public String getLocation() {
        return location;
    }

    public String getRoomSize() {
        return roomSize;
    }
}
