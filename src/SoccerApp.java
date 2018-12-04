import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

/*

 javac SoccerApp.java

 java -cp /home/codio/workspace/Soccer/java/mysql-connector-java-8.0.13.jar:.: SoccerApp

*/


public class SoccerApp {
    public static void main(String args[]) {

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/soccer?user=root");

            Class.forName("com.mysql.cj.jdbc.Driver");

            new DatabaseManagerMenu(con).displayMenu();

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            System.out.println("------------------------------------------");
            for (int i = 1; i <= columnsNumber; i++) {
                String columnValue = resultSet.getString(i);
                System.out.println(rsmd.getColumnName(i) + " | " + columnValue);
                System.out.println("------------------------------------------");
            }
            System.out.println();
        }
    }
}

class DatabaseManagerMenu {

    private Scanner sc = new Scanner(System.in);
    private int option = -1;
    private Connection con;

    DatabaseManagerMenu(Connection con) {
        this.con = con;
    }

    void displayMenu() {
        while (option != 6) {
            System.out.println("-----------------------------------------------------");
            System.out.println("                 DATABASE MANAGER MENU");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. FIND");
            System.out.println("2. INSERT");
            System.out.println("3. UPDATE");
            System.out.println("4. DELETE");
            System.out.println("5. AGGREGATE");
            System.out.println("6. Exit");
            System.out.println("-----------------------------------------------------");


            System.out.print("\nChoice : ");
            option = sc.nextInt();
            System.out.println("Press Enter to confirm");
            sc.nextLine(); // Clear the buffer


            if (option == 1) {
                new RetrievalMenu(con).displayMenu();
            } else if (option == 2) {
                new InsertMenu(con).displayMenu();
            } else if (option == 3) {

                new UpdateMenu(con).displayMenu();
            } else if (option == 4) {
                System.out.println("Delete");
            } else if (option == 5) {
                System.out.println("Aggregate");
            } else {
                option = -1;
                sc.nextLine();
                sc.nextLine();
                break;
            }
        }
    }
}

class UpdateMenu {
    private Scanner sc = new Scanner(System.in);
    private int option = -1;
    private Connection con;

    UpdateMenu(Connection con) {
        this.con = con;
    }

    void displayMenu() {
        while (option != 2) {

            System.out.println();
            System.out.println("-----------------------------------------------------");
            System.out.println("                    UPDATE MENU");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Update player overall rating");
            System.out.println("2. Back");

            System.out.print("\nChoice : ");
            option = sc.nextInt();
            System.out.println("Press Enter to confirm");
            sc.nextLine(); // Clear the buffer


            switch (option) {

                case 1:
                    updateOverallRating();
                    break;
                default:
                    sc.nextLine();
                    break;
            }
        }
    }


    private void updateOverallRating() {
        System.out.print("Enter player ID: ");
        int id = sc.nextInt();
        System.out.print("Enter player's overall rating (max 100): ");
        int overallRating = sc.nextInt();
        try {

            PreparedStatement stmt = con.prepareStatement("UPDATE player_attributes " +
                    "SET overall_rating = ? WHERE id = ?");
            stmt.setInt(1, overallRating);
            stmt.setInt(2, id);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}


class RetrievalMenu {
    private Scanner sc = new Scanner(System.in);
    private int option = -1;
    private Connection con;

    RetrievalMenu(Connection con) {
        this.con = con;
    }

    void displayMenu() {
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
            System.out.println("Press Enter to confirm");
            sc.nextLine(); // Clear the buffer

            switch (option) {

                case 1:
                    new RetrieveAllMenu(con).displayMenu();
                    break;
                case 2:
                    new ConditionalRetrievalMenu(con).displayMenu();
                    break;
                default:
                    sc.nextLine();
                    break;

            }
        }
    }
}

class RetrieveAllMenu {
    private Scanner sc = new Scanner(System.in);
    private int option = -1;
    private Connection con;

    RetrieveAllMenu(Connection con) {
        this.con = con;
    }
    void displayMenu() {
        while (option != 7) {

            System.out.println();
            System.out.println("-----------------------------------------------------");
            System.out.println("                  RETRIEVAL ALL MENU");
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
            System.out.println("Press Enter to confirm");
            sc.nextLine(); // Clear the buffer

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
                    sc.nextLine();
                    break;
            }
        }
    }

    private void retrieveAllFrom(String tableName) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 10");
            SoccerApp.printResultSet(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class ConditionalRetrievalMenu {
    private Scanner sc = new Scanner(System.in);
    private int option = -1;
    private Connection con;

    ConditionalRetrievalMenu(Connection con) {
        this.con = con;
    }

    void displayMenu() {
        while (option != 4) {

            System.out.println();
            System.out.println("-----------------------------------------------------");
            System.out.println("              CONDITIONAL RETRIEVAL MENU");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Get player details by name");
            System.out.println("2. Find players above a given rating");
            System.out.println("3. Find by ID");
            System.out.println("4. Back");

            System.out.print("\nChoice : ");

            option = sc.nextInt();
            System.out.println("Press Enter to confirm");
            sc.nextLine(); // Clear the buffer

            switch (option) {

                case 1:
                    findPlayerDetailsByName();
                    break;
                case 2:
                    findPlayersByRating();
                    break;
                case 3:
                    new FindByIdMenu(con).displayMenu();
                default:
                    sc.nextLine();
                    break;
            }
        }
    }

    private void findPlayerDetailsByName() {

        System.out.print("Enter name: ");
        String name = sc.nextLine();

        try {

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM player INNER JOIN player_attributes " +
                    "ON player.id = player_attributes.id WHERE player_name LIKE ?");
            stmt.setString(1, "%" + name + "%");

            ResultSet rs = stmt.executeQuery();

            SoccerApp.printResultSet(rs);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void findPlayersByRating() {

        System.out.print("Enter lower bound for rating (max 100): ");
        int rating = sc.nextInt();

        try {

            PreparedStatement stmt = con.prepareStatement("SELECT player_name, overall_rating FROM player " +
                    "INNER JOIN player_attributes ON player.id = player_attributes.id " +
                    "WHERE overall_rating >= ?");
            stmt.setInt(1, rating);

            ResultSet rs = stmt.executeQuery();

            SoccerApp.printResultSet(rs);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}

class FindByIdMenu {
    private Scanner sc = new Scanner(System.in);
    private int option = -1;
    private Connection con;

    FindByIdMenu(Connection con) {
        this.con = con;
    }

    void displayMenu() {
        while (option != 7) {

            System.out.println();
            System.out.println("-----------------------------------------------------");
            System.out.println("              FIND BY ID MENU");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Find Player by ID");
            System.out.println("2. Find Player Attributes by ID");
            System.out.println("3. Find Country by ID");
            System.out.println("4. Find League by ID");
            System.out.println("5. Find Team by ID");
            System.out.println("6. Find Soccer Match by ID");
            System.out.println("7. Back");

            System.out.print("\nChoice : ");

            option = sc.nextInt();
            System.out.println("Press Enter to confirm");
            sc.nextLine(); // Clear the buffer

            switch (option) {

                case 1:
                    findById("player");
                    break;
                case 2:
                    findById("player_attributes");
                    break;
                case 3:
                    findById("country");
                    break;
                case 4:
                    findById("league");
                    break;
                case 5:
                    findById("team");
                    break;
                case 6:
                    findById("soccer_match");
                    break;
                default:
                    sc.nextLine();
                    break;
            }
        }
    }


    private void findById(String tableName) {
        System.out.print("Enter the ID: ");
        int id = sc.nextInt();

        try {

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM " + tableName
                    + " WHERE id = ?");
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            SoccerApp.printResultSet(rs);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class InsertMenu {
    private Scanner sc = new Scanner(System.in);
    private int option = -1;
    private Connection con;

    InsertMenu(Connection con) {
        this.con = con;
    }

    void displayMenu() {
        while (option != 3) {

            System.out.println();
            System.out.println("-----------------------------------------------------");
            System.out.println("                   INSERT MENU");
            System.out.println("-----------------------------------------------------");
            System.out.println("Select the table to insert into:");
            System.out.println("1. PLAYER");
            System.out.println("2. PLAYER_ATTRIBUTES");
            System.out.println("3. Back");

            System.out.print("\nChoice : ");

            option = sc.nextInt();
            System.out.println("Press Enter to confirm");
            sc.nextLine(); // Clear the buffer

            switch (option) {

                case 1:
                    insertIntoPlayer();
                    break;
                case 2:
                    insertIntoPlayer_Attributes();
                    break;
                default:
                    sc.nextLine();
                    break;
            }
        }
    }

    private void insertIntoPlayer_Attributes() {
        System.out.print("Enter the player ID: ");
        int id = sc.nextInt();
        sc.nextLine();      // Clear the buffer
        System.out.print("Enter the date of assessment (yyyy-mm-dd HH:MM:SS): ");
        String date = sc.nextLine();
        System.out.print("Enter the player's overall rating (max 100): ");
        int overallRating = sc.nextInt();
        System.out.print("Enter the player's potential (max 100): ");
        int potential = sc.nextInt();
        sc.nextLine();      // Clear the buffer
        System.out.print("Enter the player's preferred foot (left or right): ");
        String preferredFoot = sc.nextLine();
        System.out.print("Enter the player's attacking work rate (max 100): ");
        int attackingWorkrate = sc.nextInt();
        System.out.print("Enter the player's defensive work rate (max 100): ");
        int defensiveWorkrate = sc.nextInt();

        try {

            PreparedStatement stmt = con.prepareStatement("INSERT INTO player_attributes " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, id);
            stmt.setString(2, date);
            stmt.setInt(3, overallRating);
            stmt.setInt(4, potential);
            stmt.setString(5, preferredFoot);
            stmt.setInt(6, attackingWorkrate);
            stmt.setInt(7, defensiveWorkrate);

            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void insertIntoPlayer() {
        System.out.print("Enter the player ID: ");
        int id = sc.nextInt();
        sc.nextLine();      // Clear the buffer
        System.out.print("Enter the player name: ");
        String name = sc.nextLine();
        System.out.print("Enter the player's birthday: (yyyy-mm-dd HH:MM:SS): ");
        String birthday = sc.nextLine();
        System.out.print("Enter the player's height: ");
        float height = sc.nextFloat();
        System.out.print("Enter the player's weight: ");
        float weight = sc.nextFloat();
        sc.nextLine();      // Clear the buffer
        try {

            PreparedStatement stmt = con.prepareStatement("INSERT INTO player " +
                    "VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, birthday);
            stmt.setFloat(4, height);
            stmt.setFloat(5, weight);

            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}



