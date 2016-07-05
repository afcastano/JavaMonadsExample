package afcastano.monads;

import java.util.Optional;
import java.util.Scanner;

public class App {

    DomainLogic domainLogic;

    public App() {
        domainLogic = new DomainLogic();
    }

    public void exec(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first number:\t");
        Optional<Integer> firstVal = readIntSafe(scanner);

        System.out.print("Enter second number:\t");
        Optional<Integer> secondVal = readIntSafe(scanner);

        System.out.print("Enter third number:\t");
        Optional<Integer> thirdVal = readIntSafe(scanner);

        Optional<Double> result = doLogic(firstVal, secondVal, thirdVal);

        printResult(result);
    }



    private Optional<Double> doLogic(Optional<Integer> firstVal,
                                     Optional<Integer> secondVal,
                                     Optional<Integer> thirdVal) {

        return firstVal.flatMap(val1 ->
               secondVal.flatMap(val2 ->
               thirdVal.flatMap(val3 -> {

                    if(val3 == 0) {
                        return Optional.empty();
                    }

                    return Optional.of(domainLogic.calculate(val1, val2, val3));

               })));

    }


    public static void main (String[] args) {
        new App().exec();
    }

    private static void printResult(Optional<Double> result) {

        if (result.isPresent()) {
            System.out.println("Great, the result is: " + result);

        } else {
            System.out.println("Ups something went wrong");

        }
    }

    private Optional<Integer> readIntSafe(Scanner scanner) {
        Optional<Integer> firstVal;

        try {
            firstVal = Optional.of(scanner.nextInt());

        } catch (Exception e) {
            firstVal = Optional.empty();

        }

        scanner.nextLine();
        return firstVal;
    }


}
