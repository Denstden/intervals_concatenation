package ua.miratech.intervals_concatenation.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class IntervalTest {

    private int firstStart;
    private int firstEnd;
    private int secondStart;
    private int secondEnd;
    private int expectedStart;
    private int expectedEnd;

    public IntervalTest(int firstStart, int firstEnd, int secondStart, int secondEnd, int expectedStart, int expectedEnd) {
        this.firstStart = firstStart;
        this.firstEnd = firstEnd;
        this.secondStart = secondStart;
        this.secondEnd = secondEnd;
        this.expectedStart = expectedStart;
        this.expectedEnd = expectedEnd;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { -25, 0, -15, 22, -25, 22 },
                { -25, 0, -26, 22, -26, 22 },
                { -25, 0, -20, -2, -25, 0 }
        });
    }

    @Test
    public void shouldCoalesce() {
        Interval first = new Interval(firstStart, firstEnd);
        Interval second = new Interval(secondStart, secondEnd);

        Interval coalesce = first.coalesce(second);

        assertEquals(expectedStart, (int)coalesce.getStart());
        assertEquals(expectedEnd, (int)coalesce.getEnd());
    }

}