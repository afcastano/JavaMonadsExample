package afcastano.monads.log;

import afcastano.monads.result.Result;
import com.sun.istack.internal.Nullable;

import java.util.function.Function;

import static afcastano.monads.log.Log.log;
import static afcastano.monads.result.Result.error;
import static afcastano.monads.result.Result.ok;

public class LoggedResult<T> {
    Log<Result<T>> log;

    private LoggedResult(@Nullable T value, @Nullable String logStr, @Nullable String errorStr) {
        if (errorStr != null) {
            log = log(error(errorStr), errorStr);
            return;
        }

        log = log(ok(value), logStr);
    }

    private LoggedResult(Log<Result<T>> log) {
        this.log = log;
    }



    public <U> LoggedResult<U> flatMap(Function<T, LoggedResult<U>> mapper) {

        Log<Result<U>> mappedLog = this.log.flatMap(result -> {
            if(result.isError()) {
                return Log.empty();
            }

            return mapper.apply(result.value()).log;
        });

        return new LoggedResult(mappedLog);
    }

}
