package hr.mikec.webstore.util;

import java.util.Scanner;

public class Tools {

    public static Scanner scanner;

    public static int parseInt(String message, String errorMessage, int min) {
        int number = 0;
        while (true) {
            System.out.print(message);
            try {
                number = Integer.parseInt(scanner.nextLine());
                if (number < min) {
                    System.out.println("Enter number greater than " + min);
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println(errorMessage);
            }
        }
        return number;
    }

    public static double parseDouble(String message, String errorMessage, double min) {
        double number = 0;
        while (true) {
            System.out.print(message);
            try {
                number = Double.parseDouble(scanner.nextLine());
                if (number < min) {
                    System.out.println("Enter number greater than " + min);
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println(errorMessage);
            }
        }
        return number;
    }

    public static String parseString(String message, String errorMessage) throws BaseException {
        String str = "";
        while (true) {
            System.out.print(message);
            str = scanner.nextLine();
            if (str.trim().equals("")) {
                throw new BaseException(errorMessage);
            }
            break;
        }
        return str;
    }

    public static boolean parseYesNo(String message, String errorMessage) {
        String str;
        while (true) {
            System.out.print(message);
            str = scanner.nextLine().trim().toLowerCase();
            if (str.equals("y") || str.equals("yes")) {
                return true;
            }
            if (str.equals("n") || str.equals("no")) {
                return false;
            }
            System.out.println(errorMessage);
        }
    }

    public static void printHeading(String title) {
        title = "-" + title + "-";
        String dashes = "";
        for (int i = 1; i <= title.length(); i++) {
            dashes += "-";
        }
        System.out.println();
        System.out.println(dashes);
        System.out.println(title);
        System.out.println(dashes);

    }

}

