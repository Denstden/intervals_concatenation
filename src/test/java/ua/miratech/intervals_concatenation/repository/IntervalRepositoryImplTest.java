package ua.miratech.intervals_concatenation.repository;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import ua.miratech.intervals_concatenation.domain.Interval;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableTransactionManagement
public class IntervalRepositoryImplTest {
    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();

            // create database
            statement.executeUpdate("CREATE TABLE test_interval (\n" +
                    "    start_i integer NOT NULL,\n" +
                    "    end_i integer NOT NULL,\n" +
                    "    CONSTRAINT end_gte_start CHECK (end_i >= start_i)\n" +
                    ")");

            // add intervals
            statement.executeUpdate("insert into test_interval(start_i, end_i) values (-10,10)");
            statement.executeUpdate("insert into test_interval(start_i, end_i) values (12,20)");
            statement.executeUpdate("insert into test_interval(start_i, end_i) values (22,30)");
            statement.executeUpdate("insert into test_interval(start_i, end_i) values (30,40)");
            statement.executeUpdate("insert into test_interval(start_i, end_i) values (-5,80)");
            statement.executeUpdate("insert into test_interval(start_i, end_i) values (15,18)");
            statement.executeUpdate("insert into test_interval(start_i, end_i) values (-6,25)");
            statement.executeUpdate("insert into test_interval(start_i, end_i) values (100,150)");
        }
    }

    @Test
    public void shouldCoalesceOverlappedIntervals() throws SQLException, InterruptedException {
        // waiting for scheduler
        Thread.sleep(1500);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from test_interval");
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Interval> coalescedIntervals = gatherResults(resultSet);

            Interval coalesced1 = new Interval(100, 150);
            Interval coalesced2 = new Interval(-10, 80);
            assertEquals(2, coalescedIntervals.size());
            assertThat(coalescedIntervals, containsInAnyOrder(coalesced1, coalesced2));
        }
    }

    private List<Interval> gatherResults(ResultSet resultSet) throws SQLException {
        List<Interval> coalescedIntervals = new ArrayList<>();
        while (resultSet.next()) {
            coalescedIntervals.add(new Interval(resultSet.getInt("start_i"), resultSet.getInt("end_i")));
        }
        return coalescedIntervals;
    }
}