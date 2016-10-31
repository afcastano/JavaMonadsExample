package afcastano.monads.result.notmonad;

/**
 * Read ints from two files, Adjust the first one and then calculate average.
 * Returns a Result wrapping the positive outcome or any error.
 */
public class ResultExample1 {

    public Result<Double> businessOperation(String fileName, String fileName2) {
        Result<Integer> val1 = Utils.readIntFromFile(fileName);

        if (val1.isError()) {
            return Result.error(val1.getError());
        }

        Result<Integer> adjusted1 = Utils.adjustValue(val1.getValue());

        if (adjusted1.isError()) {
            return Result.error(adjusted1.getError());
        }

        Result<Integer> val2 = Utils.readIntFromFile(fileName2);

        if (val2.isError()) {
            return Result.error(val2.getError());
        }

        Double average = Utils.calculateAverage(adjusted1.getValue(), val2.getValue());
        return Result.ok(average);
    }

}
