package afcastano.monads.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static afcastano.monads.log.LogEntry.newEntry;

public class Log<T> {
    private T value;

    private Optional<LogEntry> history;

    private Log(T value, Optional<LogEntry> logEntry) {
        this.value = value;
        this.history = logEntry;
    }


    public T getValue() {
        return value;
    }

    public Optional<LogEntry> getHistory() {
        return history;
    }

    public static<V> Log<V> unit(V value) {
        return log(value, null);
    }

    public static<V> Log<V> log(V value, String logS) {
        Optional<String> logStr = Optional.ofNullable(logS);
        if(logStr.isPresent()) {
            String logText = logStr.get() + " (" + value.toString() + ")";
            return new Log<>(value, Optional.of(LogEntry.newEntry(logText)));
        }

        return new Log<>(value, Optional.empty());
    }

    public <U> Log<U> flatMap(Function<T, Log<U>> mapper) {

        List<LogEntry> previousEntries = new ArrayList();
        this.history.ifPresent(previousEntries::add);

        Log<U> mappedLog = mapper.apply(value);
        Optional<LogEntry> mappedEntry = mappedLog.history;

        Optional<LogEntry> newEntry = mappedEntry.map(entry -> {
            previousEntries.addAll(entry.getPrevious());
            return LogEntry.newEntry(previousEntries, entry.getLogText());
        });

        return new Log<>(mappedLog.value, newEntry);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Log<?> log = (Log<?>) o;

        if (!value.equals(log.value)) return false;
        return history.equals(log.history);

    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + history.hashCode();
        return result;
    }

}