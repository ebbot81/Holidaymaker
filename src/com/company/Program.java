package com.company;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Program {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Scanner scanner = new Scanner(System.in);

    public Program() {
     connect();
     startMenu();
    }

    public void startMenu() {
    int menu;
        do {
        System.out.println("\n         HOLIDAYMAKER");
        System.out.println("-----------------------------");
        System.out.println("[1] REGISTRERA NY KUND");
        System.out.println("[2] SÖK LEDIGA RUM");
        System.out.println("[3] BOKA RUM");
        System.out.println("[4] AVBOKA RUM");
        System.out.println("[5] AVSLUTA");

        do {
            try {
                menu = Integer.parseInt(scanner.nextLine());
                if (menu < 1 || menu > 5) {
                    throw new IndexOutOfBoundsException();
                }
                break;
            } catch (Exception e) {
                System.out.println("Välj ett nummer mellan 1-5");
            }
        } while (true);

        switch (menu) {
            case 1:
                registerCustomer();
                break;
            case 2:
                ArrayList<Room> vacantRooms = searchVacantRoom();
                printRooms(vacantRooms, 0);
                break;
            case 3:
                bookRoom();
                break;
            case 4:
                //avboka rum
                break;
            case 5:
                exitProgram();

                break;
            default:
                break;
        }
    } while (true);
}

    private void registerCustomer() {
        System.out.println("Ange kundens namn");
        String name = scanner.nextLine();
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

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private void printRooms (ArrayList<Room> rooms, int minNumberOfBeds) {
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);

            if(room.getNumberOfBeds() < minNumberOfBeds)
                continue;

            System.out.printf("id: %d, location: %s, size %s, beds %s \n", room.getId(), room.getLocation(), room.getRoomSize(), room.getNumberOfBeds());
        }
    }

    private ArrayList<Room> searchVacantRoom() {
        String query = "SELECT r.id_room, r.location, rs.name 'room_size_name', rs.number_of_beds FROM rooms AS r INNER JOIN room_size AS rs ON r.room_size_id = rs.id WHERE room_availability = 1";
        ArrayList<Room> vacantRooms = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet result =  preparedStatement.executeQuery();
            while (result.next()) {
                int id = result.getInt("id_room");
                int numberOfBeds = result.getInt("number_of_beds");
                String location = result.getString("location");
                String roomSizeName = result.getString("room_size_name");
                Room room = new Room(id, location, roomSizeName, numberOfBeds);
                vacantRooms.add(room);
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vacantRooms;
    }

    private int getCustomerForBooking(){
        System.out.println("Ange kundens namn");
        String customerName = scanner.nextLine();

        String query = "SELECT id FROM customers WHERE name = '" + customerName + "'";

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

    private Room getRoomForBooking(int numberOfBeds){

        ArrayList<Room> vacantRooms = searchVacantRoom();

        printRooms(vacantRooms, numberOfBeds);

        System.out.println("Ange rumsid");
        int id = Integer.parseInt(scanner.nextLine());

        for(Room room : vacantRooms) {
            if(room.getId() == id) {
                return room;
            }
        }

        return null;
    }

    private void bookRoom() {

        int customerId = getCustomerForBooking();

        if(customerId < 1){
            System.out.println("Kan inte hitta kund");
            return;
        }

        System.out.println("Ange startdatum");
        Date startDate = parseDate(scanner.nextLine());

        System.out.println("Ange slutdatum");
        Date endDate = parseDate(scanner.nextLine());

        Date openingDate = parseDate("2020-06-01");
        Date closedDate = parseDate("2020-07-31");


        if (startDate.before(openingDate) || endDate.after(closedDate) ) {
            System.out.println("Säsongen är inte öppen, välj ett datum mellan 2020-06-01 - 2020-07-31");
            return;
        }

        System.out.println("Ange antal resande");
        int numberOfTravelers = Integer.parseInt(scanner.nextLine());

        Room room = getRoomForBooking(numberOfTravelers);

        String query = "REPLACE INTO bookings ("
                + "customer_id, start_date, end_date, number_of_customers, room_id)" + "VALUES (?,?,?,?,?)";

        try {
            preparedStatement = connection.prepareStatement((query));
            preparedStatement.setString(1, String.valueOf(customerId));
            preparedStatement.setDate(2, new java.sql.Date(startDate.getTime()), Calendar.getInstance());
            preparedStatement.setDate(3, new java.sql.Date(endDate.getTime()), Calendar.getInstance());
            preparedStatement.setString(4, String.valueOf(numberOfTravelers));
            preparedStatement.setInt(5, room.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelReservation() {

    }

    private void connect() {
        try {
            Class.forName ("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/holiday_maker?user=root&password=mysql&serverTimezone=UTC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exitProgram() {
        System.exit(0);
    }
}
