package afcastano.monads.optionalexample;

import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class OptionalBasicTest {

    @Test
    public void testOptionalMethods() {
        //Optional implementation of unit
        Optional<String> example = Optional.of("Example");

        //Optional implementation of bind
        Optional<String> newMonad = example.flatMap(val -> Optional.of("Modified " + val));

        System.out.println(newMonad); //Optional[Modified Example]

        assertTrue(newMonad.isPresent());
    }

    @Test
    public void testMapVsFlatMap() {
        //Divide by 0 example
        final int divisor = 0;

        Optional<Double> dividend = Optional.of(10d);

        //Throws exception since divisor is 0
        Optional<Double> result1 = dividend.map(val -> val / divisor);

        //Result two has wrong type
        Optional<Optional<Double>> result2 = dividend.map(val -> {
            if (divisor != 0) {
                return Optional.of(val / divisor);
            }

            return Optional.empty();
        });

        Optional<Double> result3 = dividend.flatMap(val -> {
            if (divisor != 0) {
                return Optional.of(val / divisor);
            }

            return Optional.empty();
        });

        assertThat(result3, is(Optional.empty()));

    }

    @Test
    public void testOptionalAdd() {
        Optional<Integer> int2 = Optional.of(2);
        Optional<Integer> int3 = Optional.of(3);
        Optional<Integer> empty = Optional.empty();

        assertThat(optionalAdd(int2, int3), is(Optional.of(5)));
        assertThat(optionalAdd(int2, empty), is(Optional.empty()));
        assertThat(optionalAdd(empty, int3), is(Optional.empty()));
        assertThat(optionalAdd(empty, empty), is(Optional.empty()));
    }

    private Optional<Integer> optionalAdd(Optional<Integer> val1, Optional<Integer> val2) {
        if(val1.isPresent() && val2.isPresent()) {
            return Optional.of(val1.get() + val2.get());
        }

        return Optional.empty();
    }

    @Test
    public void testMonadAdd() {
        Optional<Integer> int2 = Optional.of(2);
        Optional<Integer> int3 = Optional.of(3);
        Optional<Integer> empty = Optional.empty();

        assertThat(monadAdd(int2, int3), is(Optional.of(5)));
        assertThat(monadAdd(int2, empty), is(Optional.empty()));
        assertThat(monadAdd(empty, int3), is(Optional.empty()));
        assertThat(monadAdd(empty, empty), is(Optional.empty()));
    }

    private Optional<Integer> monadAdd(Optional<Integer> val1, Optional<Integer> val2) {
        return
                val1.flatMap( first ->
                val2.flatMap( second ->
                Optional.of(first + second)
        ));
    }

}