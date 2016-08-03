package afcastano.monads.loggedresult;

import afcastano.monads.log.Log;
import afcastano.monads.log.LogEntry;
import afcastano.monads.result.Result;

import java.util.function.Function;

import static afcastano.monads.log.Log.log;

public class LoggedResult<T> {
    Log<Result<T>> log;

    private LoggedResult(Log<Result<T>> log) {
        this.log = log;
    }

    public Result<T> getResult() {
        return log.getValue();
    }

    public LogEntry getHistory() {
        return log.getHistory().orElse(null);
    }

    public static <V> LoggedResult<V> logResult(Result<V> result, String logMsg) {
        return new LoggedResult<>(log(result, logMsg));
    }

    public <U> LoggedResult<U> flatMap(Function<T, LoggedResult<U>> mapper) {

        Log<Result<U>> mappedLog = log.flatMap(result -> mapResult(mapper, result));

        return new LoggedResult(mappedLog);
    }

    private <U> Log<Result<U>> mapResult(Function<T, LoggedResult<U>> mapper, Result<T> result) {
        if(result.isError()) {
            return Log.unit(Result.error(result.errorVal()));
        }

        return mapper.apply(result.value()).log;
    }

}
