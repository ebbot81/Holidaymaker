package com.company;

import java.sql.*;
import java.util.Scanner;

public class Program {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private Scanner scanner = new Scanner(System.in);

    public Program() {
     connect();
     registerCustomer(1, "Marie");
        startMenu();
    }

    public void startMenu() {
    int menu;
        do {
        System.out.println("\n         HOLIDAYMAKER");
        System.out.println("-----------------------------");
        System.out.println("[1] REGISTRERA NY KUND");
        System.out.println("[2] SÖK LEDIGA RUM");
        System.out.println("[3] AVBOKA RUM");
        System.out.println("[4] AVSLUTA");

        do {
            try {
                menu = Integer.parseInt(scanner.nextLine());
                if (menu < 1 || menu > 4) {
                    throw new IndexOutOfBoundsException();
                }
                break;
            } catch (Exception e) {
                System.out.println("Välj ett nummer mellan 1-4");
            }
        } while (true);

        switch (menu) {
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
            default:
                break;
        }
    } while (true);
}

    private void registerCustomer(int id, String name) {
        String query = "REPLACE INTO customers ("
                + " id_customer, name )" +
                "VALUES ( ?, ?)";
        try {
            // set all the preparedstatement parameters
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf( id ));
            preparedStatement.setString(2, name);
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
}
