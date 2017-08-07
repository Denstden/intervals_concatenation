package ua.miratech.intervals_concatenation.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.miratech.intervals_concatenation.service.IntervalService;

import java.util.Random;

/**
 * Class-scheduler for creating intervals.
 *
 * @author Denys Storozhenko
 */
@Component
@Profile({"create"})
public class IntervalCreateScheduler extends AbstractScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntervalCreateScheduler.class);
    private static final Random RANDOM = new Random();

    public IntervalCreateScheduler(IntervalService intervalService) {
        super(intervalService);
    }

    @Scheduled(fixedRateString = "${create.fixed.rate}")
    public void createInterval() {
        Integer start = RANDOM.nextInt();
        Integer end;
        while ((end = RANDOM.nextInt()) < start);

        LOGGER.info("Creating interval [{}, {}]", start, end);
        intervalService.createInterval(start, end);
    }
}
