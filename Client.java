package banking;

import static java.lang.System.*;
import java.util.Scanner;

public class Client {


    public static void accountDialogue() {
        Scanner scanner = new Scanner(in);

        while(true) {
            out.println("1. Create an account");
            out.println("2. Log into account");
            out.println("0. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    accountCreationDialogue();
                    break;
                case 2:
                    accountLoginDialogue();
                    break;
                case 0:
                    out.print("Bye!");
                    System.exit(0);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + choice);
            }
        }
    }

    public static void accountCreationDialogue() {

        Database.addAccount();
        out.println("Your card has been created.");
        out.println("Your card number:");
        out.println(Database.getNumberFromId(Database.numberOfAccounts - 1) );
        out.println("Your card PIN: \n" + Database.getPINFromId(Database.numberOfAccounts - 1) );


    }

    public static void accountLoginDialogue() {
        String tempCardNumber;
        String tempPIN;
        int accountId;

        Scanner scanner = new Scanner(in);
        out.println("Enter your card number.");
        tempCardNumber = scanner.nextLine();
        out.println("Enter your PIN.");
        tempPIN = scanner.next();

        accountId = Database.getIdFromNumber(tempCardNumber);

        if(accountId == -1 || tempPIN != Database.getPINFromId(accountId)) {
            out.println("Wrong card number or PIN! \n");
        } else {
            out.println("You have successfully logged in! \n");
            inAccountDialogue(accountId);
        }
    }

    public static void inAccountDialogue(int accountId) {
        Scanner scanner = new Scanner(in);
        boolean isDone = false;

        while (!isDone) {
            out.println("1. Balance");
            out.println("2. Add income");
            out.println("3. Do transfer");
            out.println("4. Close account");
            out.println("5. Log out");
            out.println("0. Exit");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    out.println("Balance: " + Database.getBalanceFromId(accountId));
                    break;
                case 2:
                    incomeDialogue(accountId);
                    break;
                case 3:
                    transferDialogue(accountId);
                    break;
                case 4:
                    isDone = true;
                    Database.closeAccount(accountId);
                    out.println("The account has been closed! \n");
                    break;
                case 5:
                    isDone = true;
                    out.println("You have successfully logged out! \n");
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + choice);
            }
        }
    }

    public static void incomeDialogue(int accountId) {
        Scanner scanner = new Scanner(System.in);
        out.println("Enter income:");
        int income = scanner.nextInt();
        Database.addToBalance(accountId, income);
        out.println("Income was added!");
    }

    public static void transferDialogue(int accountId) {
        Scanner scanner = new Scanner(in);
        out.println("Enter card number:");
        long goalNumber = scanner.nextLong();

        if (CardGeneration.getChecksum(goalNumber) != goalNumber % 10) {
         out.println("Probably you made a mistake in the card number. Please try again!");
        } else {
            int goalId = Database.getIdFromNumber(Long.toString(goalNumber));
            if( goalId == -1) {
                out.println("Such a card does not exist.");
            } else {
                out.println("Enter how much money you want to transfer:");
                int goalAmount = scanner.nextInt();
                if(goalAmount > Database.getBalanceFromId(accountId)) {
                    System.out.println("Not enough money!");
                } else {
                    Database.transferMoney(accountId, goalId, goalAmount);
                    System.out.println("Success!");
                }
            }
        }

    }
}


