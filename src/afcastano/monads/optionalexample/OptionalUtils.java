package afcastano.monads.optionalexample;

import java.util.Optional;
import java.util.function.BiFunction;

public class OptionalUtils {
    public static <T, U, R> Optional<R> flatMap2(Optional<T> opt1,
                                                 Optional<U> opt2, BiFunction<T, U, Optional<R>> mapper) {
        return
                opt1.flatMap(a ->
                opt2.flatMap(b ->
                mapper.apply(a, b)
        ));
    }
}
