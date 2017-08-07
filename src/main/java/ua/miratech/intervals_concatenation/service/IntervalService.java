package ua.miratech.intervals_concatenation.service;

public interface IntervalService {
    void coalesceAllIntervals();

    void createInterval(Integer start, Integer end);

    void removeInterval(Integer start, Integer end);
}
