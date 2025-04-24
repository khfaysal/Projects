import java.util.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class SmartGarageApp {
    static Scanner sc = new Scanner(System.in);

    // ANSI Color Codes
    static final String RESET = "\u001B[0m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String BLUE = "\u001B[34m";
    static final String CYAN = "\u001B[36m";

    static class User {
        String username;
        String password;
        String role;

        User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }

    public static void main(String[] args) {
        System.out.println(BLUE + "=====================================" + RESET);
        System.out.println(YELLOW + "=-   WELCOME TO DIU SMART GARAGE   -=" + RESET);
        System.out.println(BLUE + "=====================================" + RESET);

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> login();
                case "2" -> createAccount();
                case "3" -> {
                    System.out.println(GREEN + "Thank you for using Smart Garage System." + RESET);
                    return;
                }
                default -> System.out.println(RED + "Invalid choice. Try again." + RESET);
            }
        }
    }

    static String readPassword(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    static void login() {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();

        String pass = readPassword("Enter password: ");

        User currentUser = authenticate(uname, pass);

        if (currentUser != null) {
            if (currentUser.role.equals("admin")) {
                adminMenu();
            } else {
                userMenu(currentUser.username);
            }
        } else {
            System.out.println(RED + "[ .. Invalid Username or Password .. ]" + RESET);
        }
    }

    static void createAccount() {
        System.out.print("Choose a username: ");
        String newUser = sc.nextLine();

        // Check if the username already exists in the database
        List<Database.User> dbUsers = Database.getAllUsers();
        for (Database.User u : dbUsers) {
            if (u.getUsername().equals(newUser)) {
                System.out.println(RED + "Username already exists. Try a different one." + RESET);
                return;
            }
        }

        String newPass = readPassword("Choose a password: ");

        // Insert the new user into the database
        if (Database.insertUser(newUser, newPass, "user")) {
            System.out.println(GREEN + "Account created successfully! You can now log in." + RESET);
        } else {
            System.out.println(RED + "Failed to create account. Please try again." + RESET);
        }
    }

    static User authenticate(String username, String password) {
        // Retrieve users from the database
        List<Database.User> dbUsers = Database.getAllUsers();
        for (Database.User u : dbUsers) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return new User(u.getUsername(), u.getPassword(), u.getRole());
            }
        }
        return null;
    }

    static void userMenu(String username) {
        System.out.println(YELLOW + "-------------------------------------" + RESET);
        System.out.println(GREEN + "          Welcome, " + username + "! " + RESET);
        System.out.println(YELLOW + "-------------------------------------" + RESET);

        while (true) {
            System.out.println("\n1. Park a Vehicle");
            System.out.println("2. Ride a Cycle");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> parkVehicle();
                case "2" -> rideCycle();
                case "3" -> {
                    System.out.println(GREEN + "Logged out successfully." + RESET);
                    return;
                }
                default -> System.out.println(RED + "Invalid choice. Try again." + RESET);
            }
        }
    }

    static void adminMenu() {
        System.out.println(YELLOW + "Welcome, Admin! (Admin Panel Here)" + RESET);
        // Future admin features can be added here
    }

    static void rideCycle() {
        System.out.println(CYAN + "\n--- Cycle Riding ---" + RESET);
        System.out.print("Enter time (in minutes): ");

        int minutes;
        try {
            minutes = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(RED + "Invalid input. Please enter a number." + RESET);
            return;
        }

        double ratePerMinute = 1.0 / 5.0;
        double price = minutes * ratePerMinute;

        System.out.printf("Time: %d minutes\n", minutes);
        selectPayment(price);
    }

    static void parkVehicle() {
        System.out.println(CYAN + "\n--- Vehicle Parking ---" + RESET);

        System.out.println("Available vehicles:");
        System.out.println("1. Car");
        System.out.println("2. Bike");
        System.out.print("Choose a vehicle type (1 or 2): ");

        String choice = sc.nextLine();
        String vehicleType;
        double baseRate;

        switch (choice) {
            case "1" -> {
                vehicleType = "car";
                baseRate = 20.0 / 4; // Updated base rate for car
            }
            case "2" -> {
                vehicleType = "bike";
                baseRate = 10.0 / 2; // Updated base rate for bike
            }
            default -> {
                System.out.println(RED + "Invalid choice. Please select 1 for Car or 2 for Bike." + RESET);
                return;
            }
        }

        // Insert the vehicle into the database
        if (!Database.insertVehicle(vehicleType, baseRate)) {
            System.out.println(RED + "Failed to add vehicle to the database." + RESET);
            return;
        }

        System.out.println(GREEN + "Vehicle added to the database successfully!" + RESET);

        // Retrieve the newly added vehicle for parking calculation
        Database.Vehicle selectedVehicle = new Database.Vehicle(vehicleType, baseRate);

        System.out.print("Enter vehicle weight in kg: ");
        double weight;
        try {
            weight = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(RED + "Invalid input. Please enter a number." + RESET);
            return;
        }

        System.out.print("Enter time spent in garage (in hours): ");
        double time;
        try {
            time = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(RED + "Invalid input. Please enter a number." + RESET);
            return;
        }

        double weightFactor = weight / 100;
        double price = selectedVehicle.getBaseRate() * time * weightFactor;

        System.out.printf("Vehicle: %s | Weight: %.2fkg | Time: %.2fhr\n", selectedVehicle.getType(), weight, time);
        selectPayment(price);
    }

    static void selectPayment(double amount) {
        System.out.println(GREEN + "\nAmount to Pay: " + String.format("%.2f", amount) + " Taka" + RESET);
        System.out.println(CYAN + "Choose your payment method:" + RESET);
        System.out.println("1. bKash");
        System.out.println("2. Nagad");
        System.out.println("3. Card");
        System.out.println("4. Cash");
        System.out.print("Your choice: ");
        String paymentChoice = sc.nextLine();

        String method = switch (paymentChoice) {
            case "1" -> "bKash";
            case "2" -> "Nagad";
            case "3" -> "Card";
            case "4" -> "Cash";
            default -> "Unknown";
        };

        if (method.equals("Unknown")) {
            System.out.println(RED + "Invalid payment method. Please try again." + RESET);
            return;
        }

        System.out.println(GREEN + "Payment of " + amount + " Taka completed via " + method + "!" + RESET);
        playBipSound();
        System.out.println(YELLOW + "[ Payment Successful \uD83C\uDF89 ]\n" + RESET);

        // Save transaction to the database
        System.out.print("Enter your username for transaction record: ");
        String username = sc.nextLine();
        Database.saveTransaction(username, "Payment", amount, method);
    }

    static void playBipSound() {
        try {
            float frequency = 1000f;
            int duration = 200;
            byte[] buf = new byte[1];
            AudioFormat af = new AudioFormat(8000f, 8, 1, true, false);
            try (SourceDataLine sdl = AudioSystem.getSourceDataLine(af)) {
                sdl.open(af);
                sdl.start();
                for (int i = 0; i < duration * 8; i++) {
                    double angle = i / (8000f / frequency) * 2.0 * Math.PI;
                    buf[0] = (byte) (Math.sin(angle) * 100);
                    sdl.write(buf, 0, 1);
                }
                sdl.drain();
                sdl.stop();
            }
        } catch (Exception e) {
            System.out.println(RED + "Unable to play sound: " + e.getMessage() + RESET);
        }
    }
}
