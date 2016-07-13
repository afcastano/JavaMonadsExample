package afcastano.monads.optionalexample;


import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class TotalCountsCalculatorTest {

    private CounterRepo repo;
    private TotalCountsCalculator calculator;
    private Counter fiveColourCounter;
    private Counter sevenColourCounter;
    private Counter noColorCounter;

    @Before
    public void before() {
        repo = mock(CounterRepo.class);
        calculator = new TotalCountsCalculator(repo);

        fiveColourCounter = new Counter() {
            public Optional<Integer> colour() {
                return Optional.of(5);
            }
        };

        sevenColourCounter = new Counter() {
            public Optional<Integer> colour() {
                return Optional.of(7);
            }
        };

        noColorCounter = new Counter() {
            public Optional<Integer> colour() {
                return Optional.empty();
            }
        };
    }

    @Test
    public void calculatesTotalCorrectly() {
        when(repo.getThisMonthCounts()).thenReturn(Optional.of(fiveColourCounter));
        when(repo.getPreviousMonthCounts()).thenReturn(Optional.of(sevenColourCounter));

        assertThat(calculator.totalColour(), is(Optional.of(12)));

    }

    @Test
    public void returnAbsentIfThereIsNoCounter() {
        when(repo.getThisMonthCounts()).thenReturn(Optional.of(fiveColourCounter));
        when(repo.getPreviousMonthCounts()).thenReturn(Optional.empty());

        assertThat(calculator.totalColour(), is(Optional.empty()));
    }

    @Test
    public void returnAbsentIfThereIsCounterButNoColour() {
        when(repo.getThisMonthCounts()).thenReturn(Optional.of(fiveColourCounter));
        when(repo.getPreviousMonthCounts()).thenReturn(Optional.of(noColorCounter));

        assertThat(calculator.totalColour(), is(Optional.empty()));
    }

    @Test
    public void returnAbsentIfThisMonthIsEmpty() {
        when(repo.getThisMonthCounts()).thenReturn(Optional.empty());

        assertThat(calculator.totalColour(), is(Optional.empty()));

        // getPreviousMonthCounts is never called thanks to the Optional monad.
        verify(repo, never()).getPreviousMonthCounts();
    }

    @Test
    public void returnAbsentIfThisMonthHasEmptyColour() {
        when(repo.getThisMonthCounts()).thenReturn(Optional.of(noColorCounter));

        assertThat(calculator.totalColour(), is(Optional.empty()));

        // getPreviousMonthCounts is never called thanks to the Optional monad.
        verify(repo, never()).getPreviousMonthCounts();
    }

}