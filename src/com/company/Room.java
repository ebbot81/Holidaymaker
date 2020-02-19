package com.company;

public class Room {
    private int id;
    private int numberOfBeds;
    private String location;
    private String roomSize;
    private int pool;
    private int liveMusic;
    private int childrensClub;
    private int restaurant;


    public int getPool() {
        return pool;
    }

    public int getLiveMusic() {
        return liveMusic;
    }

    public int getChildrensClub() {
        return childrensClub;
    }

    public int getRestaurant() {
        return restaurant;
    }

    public Room(int id, String location, String roomSize, int numberOfBeds, int pool, int liveMusic, int childrensClub, int restaurant) {
        this.id = id;
        this.location = location;
        this.roomSize = roomSize;
        this.numberOfBeds = numberOfBeds;
        this.pool = pool;
        this.liveMusic = liveMusic;
        this.childrensClub = childrensClub;
        this.restaurant = restaurant;
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
