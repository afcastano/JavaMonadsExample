package afcastano.monads.simplelog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Log<T> {
    private T value;
    private List<String> trace;

    private Log(T value, List<String> newTrace) {
        this.value = value;
        this.trace = newTrace;
    }

    public static <U> Log trace(U value, String log) {
        return new Log(value, Arrays.asList(log + ": " + value));
    }

    public static <U> Log unit(U value) {
        return new Log(value, new ArrayList<>());
    }

    public <U> Log<U> flatMap(Function<T, Log<U>> mapper) {
        Log<U> mapped = mapper.apply(value);

        List<String> newTrace =  new ArrayList<>(trace);
        newTrace.addAll(mapped.trace);

        return new Log(mapped.value, newTrace);
    }

    public T getValue() {
        return value;
    }

    public List<String> getTrace() {
        return trace;
    }
}
