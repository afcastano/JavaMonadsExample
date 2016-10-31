package afcastano.monads.result;

import afcastano.monads.result.monad.Result;
import org.junit.Test;

import java.util.function.Function;

import static afcastano.monads.result.monad.Result.ok;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

public class ResultMonadLawsTest {

    @Test //unit(a) flatMap f === f(a)
    public void leftIdentity() {
        int val = 2;
        assertThat(ok(val).flatMap(f), is(f.apply(val)));
    }

    @Test //m flatMap unit === m
    public void rightIdentity() {
        Result<Integer> result = ok(2);
        assertThat(result.flatMap(ok_), is(result));
    }

    @Test //(m flatMap f) flatMap g === m flatMap ( f(x) flatMap g )
    public void associativity() {
        Result<Integer> result = ok(2);
        Result<Integer> left = (result.flatMap(f)).flatMap(g);
        Result<Integer> right = result.flatMap(val -> f.apply(val).flatMap(g));

        assertThat(left, is(right));
    }


    //Helpers
    private Function<Integer, Result<Integer>> ok_ = integer -> ok(integer);

    private Function<Integer, Result<Integer>> f = val -> ok(val +1);

    private Function<Integer, Result<Integer>> g = val -> ok(val +3);


}