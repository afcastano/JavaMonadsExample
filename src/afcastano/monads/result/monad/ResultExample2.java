package afcastano.monads.result.monad;

import static afcastano.monads.result.monad.Utils.calculateAverage;
import static afcastano.monads.result.monad.Utils.readIntFromFile;

public class ResultExample2 {

    /**
     * Read ints from two files, Adjust the first one and then calculate average.
     * Returns a Result wrapping the positive outcome or any error.
     */
    public Result<Double> businessOperation(String fileName, String fileName2) {

        Result<Integer> adjustedValue = readIntFromFile(fileName)
                .flatMap(Utils::adjustValue);

        return
                adjustedValue.flatMap( val1 ->
                readIntFromFile(fileName2).flatMap( val2 ->
                Result.ok(calculateAverage(val1, val2))
        ));
    }

}
