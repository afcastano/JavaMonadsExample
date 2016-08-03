package afcastano.monads;

import java.util.function.Function;

public interface Monad<T> {

    <V, U extends Monad<V>> U flatMap(Function<T, ? extends Monad<V>> mapper);
}
