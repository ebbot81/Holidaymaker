package com.company;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Program {
    private DatabaseConnector database = new DatabaseConnector();
    private Scanner scanner = new Scanner(System.in);

    public Program() {

     startMenu();
    }

    public void startMenu() {
    int menu;
        do {
        System.out.println("\n         HOLIDAYMAKER");
        System.out.println("-----------------------------");
        System.out.println("[1] REGISTRERA NY KUND");
        System.out.println("[2] SE VILKA RUM SOM FINNS I SYSTEMET");
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
                ArrayList<Room> vacantRooms = database.getVacantRoom();
                printRooms(vacantRooms, 0);
                break;
            case 3:
                bookRoom();
                break;
            case 4:
                headLinesForBooking();
                ArrayList<Booking> bookings = database.getBookings();
                printBookings(bookings);
                cancelReservation();
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
        System.out.println("Ange kundens för och efternamn");
        String name = scanner.nextLine();
        database.addCustomer(name);

        System.out.println("Så där ja! Nu finns " + name + " registrerad i vårt system och kan när som helst boka sin drömresa via Holiday Maker");
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
        System.out.print(String.format("%23s", "ROOM SIZE"));
        System.out.print(String.format("%10s", "BEDS"));
        System.out.print(String.format("%10s", "POOL"));
        System.out.print(String.format("%13s", "LIVE MUSIC"));
        System.out.print(String.format("%17s", "CHILDRENS CLUB"));
        System.out.print(String.format("%13s", "RESTAURANT"));
        System.out.print(String.format("%15s", "BOOKED DAYS\n"));
        System.out.print(" ***********************************************************************************************************************\n");
    }

    public void headLinesForBooking() {
        System.out.print(String.format("%3s","ID"));
        System.out.print(String.format("%16s", "CUSTOMER"));
        System.out.print(String.format("%22s", "LOCATION"));
        System.out.print(String.format("%18s", "START-DATE"));
        System.out.print(String.format("%18s", "END-DATE\n"));
        System.out.print(" ***************************************************************************\n");
    }

    private void printRooms (ArrayList<Room> rooms, int minNumberOfBeds) {
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);

            if(room.getNumberOfBeds() < minNumberOfBeds)
                continue;

            System.out.printf(" %-10d%-10s            %-10s      %s         %d         %d              %d              %d        %s \n", room.getId(), room.getLocation(), room.getRoomSize(),
                    room.getNumberOfBeds(), room.getPool(), room.getLiveMusic(), room.getChildrensClub(), room.getRestaurant(), room.getBookedDates());
        }
    }

    private void printBookings (ArrayList<Booking> bookings) {
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);

            System.out.printf(" %-10d%-16s      %-9s       %10s         %10s\n", booking.getId(), booking.getCustomerName(), booking.getLocation(),
                    booking.getStartDate(), booking.getEndDate());
        }
    }

    private int getCustomerForBooking(){
        System.out.println("Ange kundens namn");
        String customerName = scanner.nextLine();

        return database.getCustomer(customerName);
    }

    private Room getRoomForBooking(boolean includeWithChildren, boolean includeWithLiveMusic, boolean includeWithPool, boolean includeWithRestaurant, int numberOfBeds){

        ArrayList<Room> vacantRooms = database.searchRoomWithCriteria(includeWithChildren, includeWithLiveMusic, includeWithPool, includeWithRestaurant);

        printRooms(vacantRooms, numberOfBeds);

        System.out.println("Ange rums-id");
        int id = Integer.parseInt(scanner.nextLine());

        for(Room room : vacantRooms) {
            if(room.getId() == id) {
                return room;
            }
        }

        return null;
    }

    private void bookRoom() {
        boolean includeWithChildren;
        boolean includeWithLiveMusic;
        boolean includeWithPool;
        boolean includeWithRestaurant;

        int customerId = getCustomerForBooking();

        if(customerId < 1){
            System.out.println("Kan inte hitta kund");
            return;
        }

        System.out.println("Önskas POOL på resan? j eller n");
        String addPool = scanner.nextLine();
        if (addPool.equalsIgnoreCase("j")) {
            includeWithPool = true;
        } else
            includeWithPool = false;

        System.out.println("Önskas LIVEMUSIK på resan? j eller n");
        String addLiveMusic = scanner.nextLine();
        if (addLiveMusic.equalsIgnoreCase("j")) {
            includeWithLiveMusic = true;
        } else {
            includeWithLiveMusic = false;
        }

        System.out.println("Önskas BARNKLUBB på resan? j eller n");
        String addChildrensClub = scanner.nextLine();
        if (addChildrensClub.equalsIgnoreCase("j")) {
            includeWithChildren = true;
        } else {
            includeWithChildren = false;
        }

        System.out.println("Önskas RESTAURANG på resan? j eller n");
        String addRestaurant = scanner.nextLine();
        if (addRestaurant.equalsIgnoreCase("j")) {
            includeWithRestaurant = true;
        } else
            includeWithRestaurant = false;

        System.out.println("Ange antal resande");
        int numberOfTravelers = Integer.parseInt(scanner.nextLine());
        if (numberOfTravelers > 8) {
            System.out.println("Våra största rum har en resekapacitet på max åtta personer, vänligen dela upp sällskapet så att alla får plats");
            return;
        } else {

        }
        headLines();

        Room room = getRoomForBooking(includeWithChildren, includeWithLiveMusic, includeWithPool, includeWithRestaurant, numberOfTravelers);

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

        database.addBooking(customerId, startDate, endDate, numberOfTravelers, room.getId());

        System.out.println("Resan är nu bokad och finns tillgänglig i vår databas");
    }

    private void cancelReservation() {
        System.out.println("Ange id på vilken resa som ska avbokas");
        String id = scanner.nextLine();

        database.deleteBooking(Integer.parseInt(id));

        System.out.println("Bokning är nu borttagen");

    }

    private void exitProgram() {
        System.exit(0);
    }
}