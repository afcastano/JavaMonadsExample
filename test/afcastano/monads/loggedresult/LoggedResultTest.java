package afcastano.monads.loggedresult;

import afcastano.monads.result.Result;
import org.junit.Test;

import static afcastano.monads.loggedresult.LoggedResult.logResult;
import static afcastano.monads.result.Result.error;
import static afcastano.monads.result.Result.ok;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoggedResultTest {

    @Test
    public void composeLoggedResults() {
        LoggedResult<String> finalResult =
                logResult(fetchResult1(), "Fetching Result 1").flatMap(res1 ->
                logResult(fetchResult2(), "Fetching Result 2").flatMap(res2 ->
                logResult(operateWithResults(res1, res2), "Calculating final result").flatMap(finalRes ->
                logResult(format(finalRes), "Formatting")
        )));

        assertFalse(finalResult.getResult().isError());
        assertThat(finalResult.getResult().value(), is("This is the formatted value: 2"));
        assertThat(finalResult.getHistory().toString(), is("[ \n\t" +
                "[Fetching Result 1 (7)]\n" +
                " ,  \n\t[Fetching Result 2 (5)]\n" +
                " ,  \n\t[Calculating final result (2)]\n" +
                " ] -> Formatting (This is the formatted value: 2)"));

    }

    @Test
    public void composeLoggedResultsWithError() {
        LoggedResult<String> finalResult =
                logResult(fetchResult1(), "Fetching Result 1").flatMap(res1 ->
                logResult(fetchWrongResult2(), "Fetching Result 2").flatMap(res2 ->
                logResult(operateWithResults(res1, res2), "Calculating final result").flatMap(finalRes ->
                logResult(format(finalRes), "Formatting")
        )));

        assertTrue(finalResult.getResult().isError());
        assertThat(finalResult.getResult().errorVal(), is("val1 (7) can not be less than val2 (12)"));
        assertThat(finalResult.getHistory().toString(), is("[ \n\t" +
                "[Fetching Result 1 (7)]\n" +
                " ,  \n\t[Fetching Result 2 (12)]\n" +
                " ] -> Calculating final result (Error: val1 (7) can not be less than val2 (12))"));

    }

    private Result<Integer> fetchResult1() {
        return ok(7);
    }

    private Result<Integer> fetchResult2() {
        return ok(5);
    }

    private Result<Integer> fetchWrongResult2() {
        return ok(12);
    }

    private Result<Integer> operateWithResults(int val1, int val2) {
        if(val1 > 100) {
            return error("val1 ("+val1+") can not be greater than 100");
        }

        if(val1 < val2) {
            return error("val1 ("+val1+") can not be less than val2 ("+val2+")");
        }

        return ok(val1 - val2);
    }

    private Result<String> format(int toFormat) {
        return ok("This is the formatted value: " + toFormat);
    }

}