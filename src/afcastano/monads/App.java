package afcastano.monads;

import java.util.Optional;
import java.util.Scanner;

import static afcastano.monads.ExampleHelper.bind;
import static afcastano.monads.ExampleHelper.getNonZeroVal;
import static afcastano.monads.ExampleHelper.returnM;

public class App {

    DomainLogic logic;

    public App() {
        logic = new DomainLogic();
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

        return
        bind(firstVal, val1 ->
        bind(secondVal, val2 ->
        bind(thirdVal, val3 ->
        bind(getNonZeroVal(val3), divisor ->
        returnM(logic.calc(val1, val2, val3))

        ))));

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
