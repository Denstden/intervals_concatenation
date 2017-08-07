package ua.miratech.intervals_concatenation.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.miratech.intervals_concatenation.domain.Interval;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for working with database.
 *
 * @author Denys Storozhenko
 */
@Repository
public class IntervalRepositoryImpl implements IntervalRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntervalRepositoryImpl.class);

    private static final int FETCH_SIZE = 50;
    private static final String START_COLUMN_NAME = "start_i";
    private static final String END_COLUMN_NAME = "end_i";
    private static final String INSERT_STATEMENT = "insert into test_interval(start_i, end_i) values (?, ?)";
    private static final String RETRIEVE_STATEMENT = "select * from test_interval order by start_i";
    private static final String REMOVE_STATEMENT = "delete from test_interval where start_i = ?  and end_i = ?";
    private static final String REMOVE_TWO_STATEMENT = "delete from test_interval where (start_i = ? or start_i = ?)  and (end_i = ? or end_i = ?)";

    private final DataSource dataSource;

    @Autowired
    public IntervalRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createInterval(Integer start, Integer end) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            insertInterval(connection, start, end);
        }
    }

    @Override
    public void deleteInterval(Integer start, Integer end) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            removeInterval(connection, start, end);
        }
    }

    @Override
    public void coalesceAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(RETRIEVE_STATEMENT);
            preparedStatement.setFetchSize(FETCH_SIZE);

            ResultSet resultSet = preparedStatement.executeQuery();
            Interval prev = null;
            Interval current = null;
            Interval next = null;

            while (resultSet.next()) {
                if (prev == null) {
                    prev = current;
                } else {
                    prev = next;
                }

                Integer start = resultSet.getInt(START_COLUMN_NAME);
                Integer end = resultSet.getInt(END_COLUMN_NAME);
                current = new Interval(start, end);

                if (prev != null && prev.isOverlapped(current)) {
                    LOGGER.debug("Coalesce intervals [{}, {}] and [{}, {}]",
                            prev.getStart(), prev.getEnd(), current.getStart(), current.getEnd());
                    next = prev.coalesce(current);

                    removeCoalescedIntervals(connection, prev, start, end);
                    insertInterval(connection, next.getStart(), next.getEnd());
                } else {
                    next = current;
                }
            }
        }
    }

    private void insertInterval(Connection connection, int start, int end) throws SQLException {
        PreparedStatement removeStatement = connection.prepareStatement(INSERT_STATEMENT);

        removeStatement.setInt(1, start);
        removeStatement.setInt(2, end);

        LOGGER.debug("Inserting interval [{}, {}]", start, end);
        removeStatement.execute();
    }

    private void removeCoalescedIntervals(Connection connection, Interval prev, Integer start, Integer end) throws SQLException {
        PreparedStatement removeStatement = connection.prepareStatement(REMOVE_TWO_STATEMENT);

        removeStatement.setInt(1, prev.getStart());
        removeStatement.setInt(2, start);
        removeStatement.setInt(3, prev.getEnd());
        removeStatement.setInt(4, end);

        LOGGER.debug("Removing intervals [{}, {}] and [{}, {}]", prev.getStart(), prev.getEnd(), start, end);
        removeStatement.execute();
    }

    private void removeInterval(Connection connection, Integer start, Integer end) throws SQLException {
        PreparedStatement removeStatement = connection.prepareStatement(REMOVE_STATEMENT);

        removeStatement.setInt(1, start);
        removeStatement.setInt(2, end);

        LOGGER.debug("Removing interval [{}, {}]", start, end);
        removeStatement.execute();
    }
}
