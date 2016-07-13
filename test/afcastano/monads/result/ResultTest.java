package afcastano.monads.result;

import org.junit.Test;

import static afcastano.monads.result.Result.error;
import static afcastano.monads.result.Result.ok;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

public class ResultTest {

    @Test
    public void testAddLogs() {
        Result<Float> result = ok(-3, "Initial value")
                .flatMap(value -> ok(value + 5, "Add 5"))
                .flatMap(value -> ok((float) value / 2, "Divided into 2"));

        assertThat(result.value(), is(1f));
        assertThat(result.log(), is("Initial value = -3, Add 5 = 2, Divided into 2 = 1.0"));
    }

    @Test
    public void testCombinedLogs() {
        Result<Integer> result8 = ok(5, "Initial value").flatMap(value -> ok(value + 3, "plus 3"));

        Result<Integer> result2 = ok(3, "Initial value").flatMap(value -> ok(value - 1, "minus 1"));

        Result<Double> division = result8.flatMap(value1 ->
                                  result2.flatMap(value2 ->
                                  divideValues(value1, value2)
        ));

        assertThat(division.value(), is(4.0));
        assertThat(division.log(), is(
                "Stream [Initial value = 3, minus 1 = 2]\n" +
                "Stream [Initial value = 5, plus 3 = 8]\n" +
                "Composed result Divide two results = 4.0"
        ));
    }

    @Test
    public void testErrorPropagates() {
        Result<Integer> result8 = ok(5, "Initial value").flatMap(value -> ok(value + 3, "plus 3"));

        Result<Integer> result0 = ok(3, "Initial value").flatMap(value -> ok(value - 3, "minus 3"));

        Result<Double> division = result8.flatMap(value1 ->
                                  result0.flatMap(value2 ->
                                  divideValues(value1, value2)
        ));

        assertTrue(division.isError());
        assertThat(division.log(), is(
                "Stream [Initial value = 3, minus 3 = 0]\n" +
                "Stream [Initial value = 5, plus 3 = 8]\n" +
                "Composed result Value 2 can't be 0"
        ));
    }

    private Result<Double> divideValues(double value1, Integer value2) {
        return value2 == 0 ? error("Value 2 can't be 0") : ok(value1 / value2, "Divide two results");
    }


}