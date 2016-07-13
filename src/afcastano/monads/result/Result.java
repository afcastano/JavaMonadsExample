package afcastano.monads.result;

import java.util.Optional;
import java.util.function.Function;

import static afcastano.monads.result.Result.Log.merge;

public class Result<T> {
    private Optional<T> value;
    private Log log;
    private Optional<String> error;

    private Result(T value, String error, Log log) {
        this.value = Optional.ofNullable(value);
        this.error = Optional.ofNullable(error);
        this.log = log;
    }

    public static <S> Result<S> ok(S value, String log) {
        return new Result<>(value, null, new Log(log + " = " + value.toString()));
    }

    public static <R> Result<R> error(String errorStr) {
        return new Result<>(null, errorStr, new Log(errorStr));
    }

    public<U> Result<U> flatMap(Function<? super T, Result<U>> mapper) {

        if(this.isError()) {
            return new Result<>(null, this.errorVal(), this.log);
        }

        Result<U> mappedResult = mapper.apply(value.get());

        Log log = merge(mappedResult.log, this.log);

        if (mappedResult.isError()) {
            return new Result<>(null, mappedResult.errorVal(), log);
        }

        return new Result<>(mappedResult.value.get(), null, log);
    }

    public String errorVal() {
        return error.get();
    }

    public boolean isError() {
        return error.isPresent();
    }

    public T value() {
        return value.get();
    }

    public String log() {
        return log.toString();
    }

    public static class Log {
        private String value;

        // The log could have more than one previous when monads compose.
        private Optional<Log> parent1;
        private Optional<Log> parent2;

        public Log(String value) {
            this.value = value;
            this.parent1 = Optional.empty();
            this.parent2 = Optional.empty();
        }

        public Log(Log parent, String value) {
            this.value = value;
            this.parent1 = Optional.of(parent);
            this.parent2 = Optional.empty();
        }

        public static Log merge(Log newLog, Log parentLog) {
            if (!newLog.parent1.isPresent()) {
                return new Log(parentLog, newLog.value);
            }

            Log log = new Log(newLog.parent1.get(), newLog.value);
            log.parent2 = Optional.ofNullable(parentLog);

            return log;
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();

            boolean twoStreams = this.parent1.isPresent() && this.parent2.isPresent();

            ret.append(printParentLog(twoStreams, this.parent1));
            ret.append(printParentLog(twoStreams, this.parent2));
            ret.append(printValue(twoStreams));
            return ret.toString();
        }

        private String printValue(boolean twoStreams) {
            StringBuilder ret = new StringBuilder();
            if(twoStreams) {
                ret.append("Composed result ");

            } else if (parent1.isPresent()) {
                ret.append(", ");

            }

            ret.append(this.value.toString());

            return  ret.toString();
        }

        private String printParentLog(boolean twoStreams, Optional<Log> parentLog) {
            if (parentLog.isPresent()) {
                return wrapIntoStreams(twoStreams, parentLog.get().toString());
            }

            return "";
        }

        private String wrapIntoStreams(boolean twoStreams, String value) {
            StringBuilder ret = new StringBuilder();

            if (twoStreams) {
                ret.append("Stream [");
            }

            ret.append(value);

            if (twoStreams) {
                ret.append("]\n");
            }

            return ret.toString();
        }
    }
}
