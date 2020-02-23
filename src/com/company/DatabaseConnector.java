package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DatabaseConnector {
        private PreparedStatement preparedStatement;
        private Connection connection;

    public DatabaseConnector() {
        connect();
    }

    private void connect() {
        try {
            Class.forName ("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/holiday_maker?user=root&password=mysql&serverTimezone=UTC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCustomer(String name) {


        String query = "REPLACE INTO customers ("
                + " name )" +
                "VALUES ( ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Room> getVacantRoom() {
        String query = "SELECT r.id_room, r.location, rs.number_of_beds, rs.name 'room_size_name', rs.number_of_beds, pool, live_music, childrens_club, restaurant FROM rooms AS r INNER JOIN room_size AS rs ON r.room_size_id = rs.id ORDER BY r.id_room ASC";
        ArrayList<Room> vacantRooms = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet result =  preparedStatement.executeQuery();
            while (result.next()) {
                int id = result.getInt("id_room");
                int numberOfBeds = result.getInt("number_of_beds");
                int pool = result.getInt("pool");
                int liveMusic = result.getInt("live_music");
                int childrensClub = result.getInt("childrens_club");
                int restaurant = result.getInt("restaurant");
                String location = result.getString("location");
                String roomSizeName = result.getString("room_size_name");

                ArrayList<Booking> bookingsForRoom = getBookings(id);
                Room room = new Room(id, location, roomSizeName, numberOfBeds, pool, liveMusic, childrensClub, restaurant, bookingsForRoom);
                vacantRooms.add(room);
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vacantRooms;
    }

    public ArrayList<Booking> getBookings() {

        String query = "SELECT b.id, b.start_date, b.end_date, c.name, r.location " +
                "FROM bookings AS b " +
                "INNER JOIN customers AS c ON c.id = b.customer_id " +
                "INNER JOIN rooms AS r ON r.id_room = b.room_id";

        ArrayList<Booking> bookings = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet result =  preparedStatement.executeQuery();
            while (result.next()) {
                int id = result.getInt("id");
                Date startDate = result.getDate("start_date");
                Date endDate = result.getDate("end_date");
                String customerName = result.getString("name");
                String location = result.getString("location");
                Booking booking = new Booking(id, startDate, endDate, customerName, location);
                bookings.add(booking);
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public ArrayList<Room> searchRoomWithCriteria(boolean includeWithChildren, boolean includeWithLiveMusic, boolean includeWithPool, boolean includeWithRestaurant) {
        String query = "SELECT r.id_room, r.location, rs.number_of_beds,  rs.name  'room_size_name', r.pool, r.live_music, r.childrens_club, r.restaurant " +
                "FROM rooms AS r INNER JOIN room_size AS rs ON r.room_size_id = rs.id  " +
                "WHERE childrens_club = ? AND live_music = ? AND pool = ? AND restaurant = ?";
        ArrayList<Room> vacantRooms = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, includeWithChildren);
            preparedStatement.setBoolean(2, includeWithLiveMusic);
            preparedStatement.setBoolean(3, includeWithPool);
            preparedStatement.setBoolean(4, includeWithRestaurant);
            ResultSet result =  preparedStatement.executeQuery();
            while (result.next()) {
                int id = result.getInt("id_room");
                int numberOfBeds = result.getInt("number_of_beds");
                int pool = result.getInt("pool");
                int liveMusic = result.getInt("live_music");
                int childrensClub = result.getInt("childrens_club");
                int restaurant = result.getInt("restaurant");
                String location = result.getString("location");
                String roomSizeName = result.getString("room_size_name");

                ArrayList<Booking> bookingsForRoom = getBookings(id);
                Room room = new Room(id, location, roomSizeName, numberOfBeds, pool, liveMusic, childrensClub, restaurant, bookingsForRoom);
                vacantRooms.add(room);
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vacantRooms;
    }

    private ArrayList<Booking> getBookings(int roomId) {

        String query = "SELECT b.id, b.start_date, b.end_date, c.name, r.location " +
                "FROM bookings AS b " +
                "INNER JOIN customers AS c ON c.id = b.customer_id " +
                "INNER JOIN rooms AS r ON r.id_room = b.room_id " +
                "WHERE r.id_room = ? " +
                "ORDER BY start_date";

        ArrayList<Booking> bookings = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, roomId);
            ResultSet result =  preparedStatement.executeQuery();
            while (result.next()) {
                int id = result.getInt("id");
                Date startDate = result.getDate("start_date");
                Date endDate = result.getDate("end_date");
                String customerName = result.getString("name");
                String location = result.getString("location");
                Booking booking = new Booking(id, startDate, endDate, customerName, location);
                bookings.add(booking);
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public int getCustomer(String name){
        String query = "SELECT id FROM customers WHERE name = '" + name + "'";

        int customerId = 0;

        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet result =  preparedStatement.executeQuery();

            while (result.next()) {
                customerId = result.getInt("id");
            }

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customerId;
    }

    public void addBooking(int customerId, Date startDate, Date endDate, int numberOfTravelers, int roomId ){
        String query = "REPLACE INTO bookings ("
                + "customer_id, start_date, end_date, number_of_customers, room_id)" + "VALUES (?,?,?,?,?)";

        try {
            preparedStatement = connection.prepareStatement((query));
            preparedStatement.setInt(1, customerId);
            preparedStatement.setDate(2, new java.sql.Date(startDate.getTime()), Calendar.getInstance());
            preparedStatement.setDate(3, new java.sql.Date(endDate.getTime()), Calendar.getInstance());
            preparedStatement.setInt(4, numberOfTravelers);
            preparedStatement.setInt(5, roomId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void deleteBooking(int id) {
        String query = "DELETE FROM bookings WHERE id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
