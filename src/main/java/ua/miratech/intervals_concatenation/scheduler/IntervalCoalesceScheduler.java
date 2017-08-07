package ua.miratech.intervals_concatenation.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.miratech.intervals_concatenation.service.IntervalService;

/**
 * Class-scheduler for coalescing overlapped intervals.
 *
 * @author Denys Storozhenko
 */
@Component
@Profile({"coalesce", "default"})
public class IntervalCoalesceScheduler extends AbstractScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntervalCoalesceScheduler.class);

    public IntervalCoalesceScheduler(IntervalService intervalService) {
        super(intervalService);
    }

    @Scheduled(fixedRateString = "${coalesce.fixed.rate}")
    public void coalesceAllIntervals() {
        intervalService.coalesceAllIntervals();
        LOGGER.info("Coalesce intervals...");
    }
}
