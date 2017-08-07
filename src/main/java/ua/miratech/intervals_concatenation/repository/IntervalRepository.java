package ua.miratech.intervals_concatenation.repository;

import java.sql.SQLException;

public interface IntervalRepository {
    void createInterval(Integer start, Integer end) throws SQLException;

    void deleteInterval(Integer start, Integer end) throws SQLException;

    void coalesceAll() throws SQLException;
}
