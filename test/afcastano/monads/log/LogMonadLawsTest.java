package afcastano.monads.log;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.function.Function;

import static afcastano.monads.log.Log.log;
import static afcastano.monads.log.Log.unit;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LogMonadLawsTest {

    @Test //unit(a) flatMap f === f(a)
    public void leftIdentity() {
        Log<Integer> left = unit(2).flatMap(f);
        Log<Integer> right = f.apply(2);

        assertThat(left, is(right));

    }

    @Test //m flatMap unit === m
    public void rightIdentity() {
        Log<Integer> log = unit(2);

        assertThat(log.flatMap(unit_), is(log));
    }


    @Test //(m flatMap f) flatMap g === m flatMap ( f(x) flatMap g )
    public void associativity() {
        Log<Integer> log = unit(2);
        Log<Integer> left = (log.flatMap(f)).flatMap(g);
        Log<Integer> right = log.flatMap(val -> f.apply(val).flatMap(g));
        assertThat(left, Is.is(right));
    }

    private Function<Integer, Log<Integer>> unit_ = integer -> unit(integer);
    private Function<Integer, Log<Integer>> f = val -> log(val +1, "function f executed");
    private Function<Integer, Log<Integer>> g = val -> log(val +3, "function g executed");

}