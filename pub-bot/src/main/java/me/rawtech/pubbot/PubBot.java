package me.rawtech.pubbot;

import java.util.Scanner;

public class PubBot {

    public static Kernel kernel;
    public static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        System.out.println("Starting PubBot");
        System.out.println();

        kernel = new Kernel(getPubName(), getTime());

        runProgram();
    }

    private static void runProgram() {
        showMenu();
        Boolean run = true;

        while (run) {
            System.out.println("Waiting for user input...");
            String input = scanner.nextLine();
            int choice = 0;

            try {
                choice = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Cannot parse input '"+input+"'.");
            }

            switch (choice) {
                case -1:
                    kernel.shutdown();
                    run = false;
                    break;
                case 0:
                default:
                    showMenu();
                    break;
                case 1:
                    kernel.setPubName(getPubName());
                    System.out.println("Pub name has been changed.");
                    break;
                case 2:
                    kernel.setTime(getTime());
                    System.out.println("Time has been changed.");
                    break;
                case 3:
                    kernel.printPubInfo();
                    break;
                case 4:
                    kernel.showOrders();
                    break;
            }
        }

        System.exit(0);
    }

    private static String getPubName() {
        System.out.println("Enter the name of the pub:");

        return scanner.nextLine();
    }

    private static String getTime() {
        System.out.println("Enter the time:");

        return scanner.nextLine();
    }

    private static void showMenu() {
        System.out.println("-1:\tShutdown the bot");
        System.out.println("0:\tShow this menu");
        System.out.println("1:\tSet the pub name");
        System.out.println("2:\tSet the time");
        System.out.println("3:\tShows pub information");
        System.out.println("4:\tShows orders");
    }
}
