package banking;


public class Main {
    private static String fileName;
    public static String getFileName() {
        return fileName;
    }
    public static void setFileName(String name) {
        fileName = name;
    }

    public static void main(String[] args) {
        setFileName(args[1]);
        Database.createNewDatabase();
        Database.setAccountNumber();

        Client.accountDialogue();
    }

}