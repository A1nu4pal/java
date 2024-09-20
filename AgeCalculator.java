//wap in java to do the following
//1)age calculator as on any date given by user
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

public class AgeCalculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter birth year (YYYY): ");
        int birthYear = sc.nextInt();
        System.out.print("Enter birth month (MM): ");
        int birthMonth = sc.nextInt();
        System.out.print("Enter birth day (DD): ");
        int birthDay = sc.nextInt();

        System.out.print("Enter year to calculate age on (YYYY): ");
        int year = sc.nextInt();
        System.out.print("Enter month to calculate age on (MM): ");
        int month = sc.nextInt();
        System.out.print("Enter day to calculate age on (DD): ");
        int day = sc.nextInt();

        // Creating LocalDate objects for birthdate and the given date
        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
        LocalDate givenDate = LocalDate.of(year, month, day);

        // Calculating the age
        if (givenDate.isBefore(birthDate)) {
            System.out.println("The given date is before the birthdate. Please provide a valid date.");
        } else {
            Period age = Period.between(birthDate, givenDate);
            System.out.println("Age on " + givenDate + ": " + age.getYears() + " years, " + age.getMonths() + " months, " + age.getDays() + " days.");
        }

        sc.close();
    }
}
