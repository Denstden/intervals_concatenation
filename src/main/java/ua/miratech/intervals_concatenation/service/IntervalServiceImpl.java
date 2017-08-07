package ua.miratech.intervals_concatenation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.miratech.intervals_concatenation.repository.IntervalRepository;

import java.sql.SQLException;

/**
 * Class for business logic.
 *
 * @author Denys Storozhenko
 */
@Service
public class IntervalServiceImpl implements IntervalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntervalServiceImpl.class);

    private IntervalRepository intervalRepository;

    @Autowired
    public IntervalServiceImpl(IntervalRepository intervalRepository) {
        this.intervalRepository = intervalRepository;
    }

    @Override
    public void coalesceAllIntervals() {
        try {
            intervalRepository.coalesceAll();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void createInterval(Integer start, Integer end) {
        try {
            intervalRepository.createInterval(start, end);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void removeInterval(Integer start, Integer end) {
        try {
            intervalRepository.deleteInterval(start, end);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
