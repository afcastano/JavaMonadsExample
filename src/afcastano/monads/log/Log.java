package afcastano.monads.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Log<T> {
    private T value;

    private Optional<LogEntry> history;

    private Log(T value, Optional<LogEntry> logEntry) {
        this.value =value;
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

        Log<U> mappedLog = mapper.apply(this.getValue());

        Optional<LogEntry> newEntry;

        if(this.getHistory().isPresent()) {
            LogEntry thisHistory = this.getHistory().get();
            if (mappedLog.getHistory().isPresent()) {
                newEntry = mergeLogEntries(mappedLog, thisHistory);

            } else {
                newEntry = Optional.of(LogEntry.newEntry(thisHistory.getPrevious(), thisHistory.getLogText()));

            }

        } else {
            if (mappedLog.getHistory().isPresent()) {
                newEntry = mappedLog.getHistory();

            } else {
                newEntry = Optional.empty();

            }

        }

        //If new history is not present, then use the old one.
        return new Log<>(mappedLog.getValue(), newEntry.isPresent() ? newEntry : this.getHistory());
    }

    private <U> Optional<LogEntry> mergeLogEntries(Log<U> mappedLog, LogEntry thisHistory) {
        Optional<LogEntry> newEntry;
        List<LogEntry> previousEntries = new ArrayList();
        previousEntries.add(thisHistory);
        LogEntry mappedEntry = mappedLog.getHistory().get();
        previousEntries.addAll(mappedEntry.getPrevious());
        newEntry = Optional.of(LogEntry.newEntry(previousEntries, mappedEntry.getLogText()));
        return newEntry;
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