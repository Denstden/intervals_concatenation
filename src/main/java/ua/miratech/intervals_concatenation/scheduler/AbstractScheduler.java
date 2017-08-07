package ua.miratech.intervals_concatenation.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import ua.miratech.intervals_concatenation.service.IntervalService;

public abstract class AbstractScheduler {
    final IntervalService intervalService;

    @Autowired
    public AbstractScheduler(IntervalService intervalService) {
        this.intervalService = intervalService;
    }
}
