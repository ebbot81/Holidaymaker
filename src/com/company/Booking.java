package com.company;

import java.util.Date;

public class Booking {
    private int id;
    private Date startDate;
    private Date endDate;
    private String customerName;
    private String location;

    public Booking(int id, Date startDate, Date endDate, String customerName, String location) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerName = customerName;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getLocation() {
        return location;
    }
}
