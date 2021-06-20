package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {

    static String url = "jdbc:sqlite:" + Main.getFileName();
    static int numberOfAccounts = 0;

    public static void createNewDatabase() {
        //String dropQuery = "DROP TABLE IF EXISTS card;";
        String creationQuery = "CREATE TABLE IF NOT EXISTS card("
                + "id INTEGER PRIMARY KEY,"
                + "number TEXT UNIQUE,"
                + "pin TEXT,"
                + "balance INTEGER DEFAULT 0);";

        try (Connection conn = DriverManager.getConnection(url)) {
            Statement statement = conn.createStatement();
            //statement.executeUpdate(dropQuery);
            statement.executeUpdate(creationQuery);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setAccountNumber() {
        try (Connection conn = DriverManager.getConnection(url)) {
            Statement statement = conn.createStatement();
            String query = "SELECT id FROM card;";
            ResultSet allRows = statement.executeQuery(query);
           while(allRows.next()) {
               numberOfAccounts++;
           }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addAccount() {
        try (Connection conn = DriverManager.getConnection(url)) {
            Statement statement =   conn.createStatement();

            String cardNumber = CardGeneration.generateCardNumber();
            String PIN = CardGeneration.generatePIN();

            boolean noDuplicate = false;

            while(!noDuplicate) {
                if (Database.getIdFromNumber(cardNumber) == -1) {
                    noDuplicate = true;
                }
            }

            String add = "INSERT INTO card VALUES"
                    + String.format("(%d, %s, '%s', 0);", numberOfAccounts, cardNumber, PIN);
            statement.executeUpdate(add);
            numberOfAccounts++;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getIdFromNumber(String number) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String query = "SELECT id FROM card WHERE " + number + " = number;";
            try(Statement statement =  conn.createStatement()) {
                try(ResultSet correctAccounts = statement.executeQuery(query)) {
                    if(correctAccounts.next()) {
                        return correctAccounts.getInt("id");
                    } else {
                        return -1;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public static String getNumberFromId(int id) {
        try (Connection conn = DriverManager.getConnection(url)) {
            Statement statement =   conn.createStatement();

            String query = "SELECT number FROM card WHERE " + id + " = id;";

            try(ResultSet correctAccounts = statement.executeQuery(query)) {
                if(correctAccounts.next()) {
                    return correctAccounts.getString("number");
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String getPINFromId(int id) {
        try (Connection conn = DriverManager.getConnection(url)) {
            Statement statement =   conn.createStatement();

            String query = "SELECT pin FROM card WHERE " + id + " = id;";

            try(ResultSet correctAccounts = statement.executeQuery(query)) {
                if(correctAccounts.next()) {
                    return correctAccounts.getString("pin");
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static int getBalanceFromId(int id) {
        try (Connection conn = DriverManager.getConnection(url)) {
            Statement statement =   conn.createStatement();

            String query = "SELECT balance FROM card WHERE "  + id + " = id;";

            try(ResultSet correctAccounts = statement.executeQuery(query)) {
                if(correctAccounts.next()) {
                    return correctAccounts.getInt("balance");
                } else {
                    return -1;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public static void transferMoney(int donorId, int recipientId, int amount) {
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);

            String queryRecipient = "UPDATE card SET balance = balance + " + amount +" WHERE id = " + recipientId + ";";
            String queryDonor = "UPDATE card SET balance = balance - " + amount + " WHERE id = " + donorId + ";";

            try(Statement donorStatement =  conn.createStatement();
                Statement recipientStatement =  conn.createStatement(); ) {
                donorStatement.executeUpdate(queryDonor);
                recipientStatement.executeUpdate(queryRecipient);
                conn.commit();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addToBalance(int id, int amount) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String query = "UPDATE card SET balance = balance + " + amount +" WHERE id = " + id + ";";
            try(Statement statement =  conn.createStatement(); ) {
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void closeAccount(int id) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String query = "DELETE FROM card WHERE id = " + id + ";";
            try(Statement statement =  conn.createStatement(); ) {
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
