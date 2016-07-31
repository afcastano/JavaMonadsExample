package afcastano.monads.log;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LogEntry {
    private String logText;
    private List<LogEntry> previous;

    private LogEntry(List<LogEntry> previous, String logText) {
        this.logText = logText;
        this.previous = previous;
    }

    public static LogEntry newEntry(String logText) {
        return new LogEntry(Collections.EMPTY_LIST, logText);
    }

    public static LogEntry newEntry(List<LogEntry> previous, String logText) {
        return new LogEntry(previous, logText);
    }

    public String getLogText() {
        return logText;
    }

    public List<LogEntry> getPrevious() {
        return previous;
    }

    @Override
    public String toString() {
        String previousStr
                = previous.size() == 0 ? ""
                : previous.size() == 1 ? previous.stream().map(entry -> entry.toString() + " -> ").reduce(String::concat).orElse("")
                : previous.stream().map(entry -> " \n\t[" + entry.toString() + "]\n " ).collect(Collectors.toList()).toString() + " -> ";

        return previousStr + logText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogEntry logEntry = (LogEntry) o;

        if (!logText.equals(logEntry.logText)) return false;
        return previous.equals(logEntry.previous);

    }

    @Override
    public int hashCode() {
        int result = logText.hashCode();
        result = 31 * result + previous.hashCode();
        return result;
    }
}
