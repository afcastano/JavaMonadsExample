package afcastano.monads.optionalexample;

import java.util.Optional;

public class TotalCountsCalculator {

    private CounterRepo repo;

    public TotalCountsCalculator(CounterRepo repo) {
        this.repo = repo;
    }

    public Optional<Integer> totalColour() {

        return
               repo.getThisMonthCounts().flatMap(Counter::colour).flatMap(colour1 ->
               repo.getPreviousMonthCounts().flatMap(Counter::colour).flatMap(colour2 ->

               Optional.of(colour1 + colour2)
        ));
    }
}
