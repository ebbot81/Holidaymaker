package com.company;

import java.util.ArrayList;
import java.util.Date;

public class Room {
    private int id;
    private int numberOfBeds;
    private String location;
    private String roomSize;
    private int pool;
    private int liveMusic;
    private int childrensClub;
    private int restaurant;
    private ArrayList<Booking> bookings;


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

    public Room(int id, String location, String roomSize, int numberOfBeds, int pool, int liveMusic, int childrensClub, int restaurant, ArrayList bookings) {
        this.id = id;
        this.location = location;
        this.roomSize = roomSize;
        this.numberOfBeds = numberOfBeds;
        this.pool = pool;
        this.liveMusic = liveMusic;
        this.childrensClub = childrensClub;
        this.restaurant = restaurant;
        this.bookings = bookings;
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

    public String getBookedDates() {
        String dates = "";

        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);

            dates += String.format("%s - %s, ", booking.getStartDate(), booking.getEndDate());
        }

        return dates;
    }
}
