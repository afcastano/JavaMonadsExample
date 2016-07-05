package afcastano.monads;

import java.util.Optional;
import java.util.function.Function;

public class ExampleHelper {

    public static <U, T> Optional<U> bind(Optional<T> opt, Function<T, Optional<U>> mapper) {
        if(opt == null) {
            return Optional.empty();
        }

        return opt.flatMap(mapper);
    }

    public static <T> Optional<T> returnM(T val) {
        return Optional.ofNullable(val);
    }

    public static Optional<Number> getNonZeroVal(Number z) {
        if(z.doubleValue() == 0) {
            return Optional.empty();
        }

        return Optional.of(z);
    }

}
