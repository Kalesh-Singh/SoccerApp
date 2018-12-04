import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

/*

 javac SoccerApp.java

 java -cp /home/codio/workspace/Soccer/java/mysql-connector-java-8.0.13.jar:.: SoccerApp

*/


public class SoccerApp {

    private static Connection con;

    public static void main(String args[]) {

        try {

            con = DriverManager.getConnection("jdbc:mysql://localhost/soccer?user=root");

            Class.forName("com.mysql.cj.jdbc.Driver");

            DatabaseManagerMenu.displayMenu();

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            System.out.println("------------------------------------------");
            for (int i = 1; i <= columnsNumber; i++) {
                String columnValue = resultSet.getString(i);
                System.out.println(rsmd.getColumnName(i) + " | " + columnValue);
                System.out.println("------------------------------------------");
            }
            System.out.println("");
        }
    }

    static class DatabaseManagerMenu {

        static Scanner sc = new Scanner(System.in);
        static int option = -1;

        static void displayMenu() {
            while (option != 6) {
                System.out.println("-----------------------------------------------------");
                System.out.println("                 DATABASE MANAGER MENU");
                System.out.println("-----------------------------------------------------");
                System.out.println("1. FIND");
                System.out.println("2. INSERT");
                System.out.println("3. UPDATE");
                System.out.println("4. JOIN");
                System.out.println("5. AGGREGATE");
                System.out.println("6. Exit");
                System.out.println("-----------------------------------------------------");

                System.out.print("\nChoice : ");

                option = sc.nextInt();

                switch (option) {
                    case 1:
                        RetrievalMenu.displayMenu();
                        break;
                    case 2:
                        // TODO: Insert Menu
                        break;
                    case 3:
                        // TODO: Update Menu
                        break;
                    case 4:
                        // TODO: Join Menu
                        break;
                    case 5:
                        // TODO: Aggregate Menu
                        break;
                    default:
                        break;
                }
            }
        }
    }


    static class RetrievalMenu {
        static Scanner sc = new Scanner(System.in);
        static int option = -1;

        static void displayMenu() {
            while (option != 3) {

                System.out.println();
                System.out.println("-----------------------------------------------------");
                System.out.println("                    RETRIEVAL MENU");
                System.out.println("-----------------------------------------------------");
                System.out.println("1. Retrieval all records in a table");
                System.out.println("2. Conditional retrievals");
                System.out.println("3. Back");

                System.out.print("\nChoice : ");
                option = sc.nextInt();

                switch (option) {

                    case 1:
                        RetrieveAllMenu.displayMenu();
                        break;
                    case 2:
                        ConditionalRetrievallMenu.displayMenu();
                        break;
                    default:
                        break;

                }
            }
        }
    }

    static class RetrieveAllMenu {
        static Scanner sc = new Scanner(System.in);
        static int option = -1;

        static void displayMenu() {
            while (option != 7) {

                System.out.println();
                System.out.println("-----------------------------------------------------");
                System.out.println("                    RETRIEVAL MENU");
                System.out.println("-----------------------------------------------------");
                System.out.println("Select the table to retrieve all records from:");
                System.out.println("1. PLAYER");
                System.out.println("2. PLAYER_ATTRIBUTES");
                System.out.println("3. COUNTRY");
                System.out.println("4. LEAGUE");
                System.out.println("5. TEAM");
                System.out.println("6. SOCCER_MATCH");
                System.out.println("7. Back");

                System.out.print("\nChoice : ");

                option = sc.nextInt();

                switch (option) {

                    case 1:
                        retrieveAllFrom("player");
                        break;
                    case 2:
                        retrieveAllFrom("player_attributes");
                        break;
                    case 3:
                        retrieveAllFrom("country");
                        break;
                    case 4:
                        retrieveAllFrom("league");
                        break;
                    case 5:
                        retrieveAllFrom("team");
                        break;
                    case 6:
                        retrieveAllFrom("soccer_match");
                        break;
                    default:
                        break;
                }
            }
        }

        static void retrieveAllFrom(String tableName) {
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName+" LIMIT 10");
                printResultSet(rs);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    static class ConditionalRetrievallMenu {
        static Scanner sc = new Scanner(System.in);
        static int option = -1;

        static void displayMenu() {
            while (option != 3) {

                System.out.println();
                System.out.println("-----------------------------------------------------");
                System.out.println("              CONDITIONAL RETRIEVAL MENU");
                System.out.println("-----------------------------------------------------");
                System.out.println("1. Get player details by name");
                System.out.println("2. Find players above a given rating");
                System.out.println("3. Back");

                System.out.print("\nChoice : ");

                option = sc.nextInt();

                switch (option) {

                    case 1:
                        findPlayerDetailsByName();
                        break;
                    case 2:
                        findPlayersByRating();
                        break;
                    default:
                        break;
                }
            }
        }

        static void findPlayerDetailsByName() {

            System.out.print("Enter name: ");
            sc.nextLine();
            String name = sc.nextLine();

            try {

                PreparedStatement stmt = con.prepareStatement("SELECT * FROM player INNER JOIN player_attributes " +
                        "ON player.id = player_attributes.id WHERE player_name LIKE ?");
                stmt.setString(1, "%" + name + "%");

                ResultSet rs = stmt.executeQuery();

                printResultSet(rs);

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        static void findPlayersByRating() {

            System.out.print("Enter lower bound for rating (max 100): ");
            int rating = sc.nextInt();

            try {

                PreparedStatement stmt = con.prepareStatement("SELECT player_name, overall_rating FROM player " +
                        "INNER JOIN player_attributes ON player.id = player_attributes.id " +
                        "WHERE overall_rating >= ?");
                stmt.setInt(1, rating);

                ResultSet rs = stmt.executeQuery();

                printResultSet(rs);

            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

}

