package afcastano.monads.log;

import afcastano.monads.result.Result;
import org.junit.Test;

import static afcastano.monads.log.Log.log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LogTest {

    @Test
    public void testAddSimpleLogs() {
        Log<Float> result = log(-3, "start")
                .flatMap(value -> log(value + 5, "Method 1 to add 5"))
                .flatMap(value -> log((float) value / 2, "Method 2 to Divide into 2"));

        assertThat(result.getValue(), is(1f));
        assertThat(result.getHistory().get().toString(),
                is("start (-3) -> Method 1 to add 5 (2) -> Method 2 to Divide into 2 (1.0)"));

    }

    @Test
    public void testFlatComposeLogs() {
        Log<Float> log1 = log(-3, "start")
                .flatMap(value -> log(value + 5, "Add 5"))
                .flatMap(value -> log((float) value / 2, "Divided into 2"));

        Log<Integer> log2 = log(7, "start")
                .flatMap(value -> log(value - 5, "Remove 5"));

        Log<Integer> log3 = log(12, "start")
                .flatMap(value -> log(value * 3, "times 3"))
                .flatMap(value -> log(value - 7, "subtract 7"));

        Log<Float> finalResult =
                log1.flatMap(val1 ->
                log2.flatMap(val2 ->
                log3.flatMap(val3 ->
                log((val1 * val2) + val3, "Operate 3 values")
        )));

        assertThat(finalResult.getValue(), is(31f));
        assertThat(finalResult.getHistory().get().toString(),
                is("[ \n" +
                    "\t[start (-3) -> Add 5 (2) -> Divided into 2 (1.0)]\n" +
                    " ,  \n" +
                    "\t[start (7) -> Remove 5 (2)]\n" +
                    " ,  \n" +
                    "\t[start (12) -> times 3 (36) -> subtract 7 (29)]\n" +
                    " ] -> Operate 3 values (31.0)"));
    }

    @Test
    public void testNestComposeLogs() {
        Log<Float> log1 = log(-3, "start")
                .flatMap(value -> log(value + 5, "Add 5"))
                .flatMap(value -> log((float) value / 2, "Divided into 2"));

        Log<Integer> log2 = log(7, "start")
                .flatMap(value -> log(value - 5, "Remove 5"));

        Log<Integer> log3 = log(12, "start")
                .flatMap(value -> log(value * 3, "times 3"))
                .flatMap(value -> log(value - 7, "subtract 7"));

        Log<Integer> composed2_3 =
                log2.flatMap(val2 ->
                log3.flatMap(val3 ->
                log(val2 * val3, "Operate 2 and 3")
        ));

        Log<Float> finalResult =
                log1.flatMap(val1 ->
                composed2_3.flatMap(val2_3 ->
                log((val1 * val2_3), "Operate nested values")
        ));

        assertThat(finalResult.getValue(), is(58f));
        assertThat(finalResult.getHistory().get().toString(),
                is("[ \n" +
                    "\t[start (-3) -> Add 5 (2) -> Divided into 2 (1.0)]\n" +
                    " ,  \n" +
                    "\t[[ \n" +
                        "\t[start (7) -> Remove 5 (2)]\n" +
                        " ,  \n" +
                        "\t[start (12) -> times 3 (36) -> subtract 7 (29)]\n" +
                        " ] -> Operate 2 and 3 (58)]\n" +
                    " ] -> Operate nested values (58.0)"));
    }

}