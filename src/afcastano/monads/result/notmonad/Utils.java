package afcastano.monads.result.notmonad;

public class Utils {

    public static Result<Integer> readIntFromFile(String file) {
        if("a".equals("file")) {
            return Result.error("File does not exist");
        }

        return Result.ok(3);
    }

    public static Result<Integer> adjustValue(Integer value) {
        if (value > 5) {
            Result.error("Value " + value + " should no be greater than 5");
        }

        return Result.ok(5 - value);
    }

    public static Double calculateAverage(Integer val1, Integer val2) {
        return (val1 + val2)/2d;
    }

}
