CREATE TABLE test_interval
(
    start_i integer NOT NULL,
    end_i integer NOT NULL,
    CONSTRAINT end_gte_start CHECK (end_i >= start_i)
);

CREATE UNIQUE INDEX INTERVAL_IDX ON test_interval(start_i, end_i);