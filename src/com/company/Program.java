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
        System.out.println("[3] HITTA KUNDENS DRÖMRESA");
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
                headLines();
                ArrayList<Room> vacantRooms = searchVacantRoom();
                printRooms(vacantRooms, 0);
                break;
            case 3:
                bookingMenu();
                break;
            case 4:
                headLines();
                ArrayList<Room> occupiedRooms = searchOccupiedRoom();
                printRooms(occupiedRooms, 0);
                getRoomForCancellation(0);
//                cancelReservation();
                break;
            case 5:
                exitProgram();

                break;
            default:
                break;
        }
    } while (true);
}

    public void bookingMenu() {
        int bookMenu;
        do {
            System.out.println("\n         HOLIDAYMAKER");
            System.out.println("-----------------------------");
            System.out.println("NEDAN FÖLJER OLIKA SÖKKRITERIER FÖR ATT HITTA KUNDENS DRÖMRESA\n");
            System.out.println("[1] SORTERA EFTER PLATSER MED POOL");
            System.out.println("[2] SORTERA EFTER PLATSER MED LIVEMUSIK");
            System.out.println("[3] SORTERA EFTER PLATSER MED BARNKLUBBAR");
            System.out.println("[4] SORTERA EFTER PLATSER MED RESTAURANGER");
            System.out.println("[5] BOKA KUNDENS RESA HÄR");
            System.out.println("[6] AVSLUTA");

            do {
                try {
                    bookMenu = Integer.parseInt(scanner.nextLine());
                    if (bookMenu < 1 || bookMenu > 6) {
                        throw new IndexOutOfBoundsException();
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Välj ett nummer mellan 1-6");
                }
            } while (true);

            switch (bookMenu) {
                case 1:
                    headLines();
                    printRooms(searchRoomWithPool(), 0);
                    break;
                case 2:
                    headLines();
                    printRooms(searchRoomWithLiveMusic(), 0);
                    break;
                case 3:
                    headLines();
                    printRooms(searchRoomWithChildrensClub(), 0);
                    break;
                case 4:
                    headLines();
                    printRooms(searchRoomWithRestaurant(), 0);
                    break;
                case 5:
                    bookRoom();

                    break;
                case 6:
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
        System.out.println("Så där ja! Nu finns " + name + " registrerad i vårt system och kan när som helst boka sin drömresa via Holiday Maker");
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

    public void headLines() {
        System.out.print(String.format("%3s","ID"));
        System.out.print(String.format("%16s", "LOCATION"));
        System.out.print(String.format("%23s", "ROOM_SIZE"));
        System.out.print(String.format("%13s", "NR_OF_BEDS"));
        System.out.print(String.format("%7s", "POOL"));
        System.out.print(String.format("%13s", "LIVE_MUSIC"));
        System.out.print(String.format("%17s", "CHILDRENS_CLUB"));
        System.out.print(String.format("%14s", "RESTAURANT\n"));
        System.out.print(" ********************************************************************************************************\n");
    }

    private void printRooms (ArrayList<Room> rooms, int minNumberOfBeds) {
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);

            if(room.getNumberOfBeds() < minNumberOfBeds)
                continue;

            System.out.printf(" %-10d%-10s            %-10s      %s         %d         %d               %d              %d \n", room.getId(), room.getLocation(), room.getRoomSize(),
                    room.getNumberOfBeds(), room.getPool(), room.getLiveMusic(), room.getChildrensClub(), room.getRestaurant());
        }
    }

    private ArrayList<Room> searchVacantRoom() {
        String query = "SELECT r.id_room, r.location, rs.number_of_beds, rs.name 'room_size_name', rs.number_of_beds, pool, live_music, childrens_club, restaurant FROM rooms AS r INNER JOIN room_size AS rs ON r.room_size_id = rs.id WHERE room_availability = 1";
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
                Room room = new Room(id, location, roomSizeName, numberOfBeds, pool, liveMusic, childrensClub, restaurant);
                vacantRooms.add(room);
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vacantRooms;
    }

    private ArrayList<Room> searchOccupiedRoom() {
        String query = "SELECT r.id_room, r.location, rs.name 'room_size_name', rs.number_of_beds, rs.number_of_beds, pool, live_music, childrens_club, restaurant FROM rooms AS r INNER JOIN room_size AS rs ON r.room_size_id = rs.id WHERE room_availability = 0";
        ArrayList<Room> occupiedRooms = new ArrayList<>();
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
                Room room = new Room(id, location, roomSizeName, numberOfBeds, pool, liveMusic, childrensClub, restaurant);
                occupiedRooms.add(room);
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return occupiedRooms;
    }

    private ArrayList<Room> searchRoomWithPool() {
        String query = "SELECT r.id_room, r.location, rs.number_of_beds,  rs.name  'room_size_name', r.pool, r.live_music, r.childrens_club, r.restaurant FROM rooms AS r INNER JOIN room_size AS rs ON r.room_size_id = rs.id   WHERE pool = 1";
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
                Room room = new Room(id, location, roomSizeName, numberOfBeds, pool, liveMusic, childrensClub, restaurant);
                vacantRooms.add(room);
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vacantRooms;
    }

    private ArrayList<Room> searchRoomWithLiveMusic() {
        String query = "SELECT r.id_room, r.location, rs.number_of_beds,  rs.name  'room_size_name', r.pool, r.live_music, r.childrens_club, r.restaurant FROM rooms AS r INNER JOIN room_size AS rs ON r.room_size_id = rs.id   WHERE live_music = 1";
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
                Room room = new Room(id, location, roomSizeName, numberOfBeds, pool, liveMusic, childrensClub, restaurant);
                vacantRooms.add(room);
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vacantRooms;
    }

    private ArrayList<Room> searchRoomWithChildrensClub() {
        String query = "SELECT r.id_room, r.location, rs.number_of_beds,  rs.name  'room_size_name', r.pool, r.live_music, r.childrens_club, r.restaurant FROM rooms AS r INNER JOIN room_size AS rs ON r.room_size_id = rs.id   WHERE childrens_club = 1";
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
                Room room = new Room(id, location, roomSizeName, numberOfBeds, pool, liveMusic, childrensClub, restaurant);
                vacantRooms.add(room);
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vacantRooms;
    }

    private ArrayList<Room> searchRoomWithRestaurant() {
        String query = "SELECT r.id_room, r.location, rs.number_of_beds,  rs.name  'room_size_name', r.pool, r.live_music, r.childrens_club, r.restaurant FROM rooms AS r INNER JOIN room_size AS rs ON r.room_size_id = rs.id   WHERE restaurant = 1";
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
                Room room = new Room(id, location, roomSizeName, numberOfBeds, pool, liveMusic, childrensClub, restaurant);
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

        System.out.println("Ange rums-id");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println("Resan är nu bokad och finns tillgänglig i vår databas");

        for(Room room : vacantRooms) {
            if(room.getId() == id) {
                return room;
            }
        }

        return null;
    }

    private Room getRoomForCancellation(int numberOfBeds){

        ArrayList<Room> occupiedRooms = searchOccupiedRoom();

        printRooms(occupiedRooms, numberOfBeds);

        System.out.println("Ange rummets id på resan kunden vill avboka");
        int id = Integer.parseInt(scanner.nextLine());

        for(Room room : occupiedRooms) {
            if(room.getId() == id) {
                occupiedRooms.remove(id);
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
        if (endDate.before(startDate)) {
            System.out.println("Kunden kan inte resa hem innan hen rest iväg :)");
            return;
        }

        Date openingDate = parseDate("2020-06-01");
        Date closedDate = parseDate("2020-07-31");


        if (startDate.before(openingDate) || endDate.after(closedDate) ) {
            System.out.println("Säsongen är inte öppen, välj ett datum mellan 2020-06-01 - 2020-07-31");
            return;
        }

        System.out.println("Ange antal resande");
        int numberOfTravelers = Integer.parseInt(scanner.nextLine());
        if (numberOfTravelers > 8) {
            System.out.println("Våra största rum har en resekapacitet på max åtta personer, vänligen dela upp sällskapet så att alla får plats");
            return;
        }
        headLines();

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
        System.out.println("Ange id på vilken resa som ska avbokas");
        String id = scanner.nextLine();
        String query = "DELETE FROM bookings ("
                + " room_id )" +
                "VALUES ( ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(id));
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
