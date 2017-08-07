package ua.miratech.intervals_concatenation.domain;

/**
 * Class for representation interval of numbers [start, end].
 *
 * @author Denys Storozhenko
 */
public class Interval {
    private Integer start;
    private Integer end;

    public Interval(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Interval coalesce(Interval next) {
        Integer start = Math.min(this.start, next.getStart());
        Integer end = Math.max(this.end, next.getEnd());
        return new Interval(start, end);
    }

    public boolean isOverlapped(Interval current) {
        return current.getStart() <= end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interval interval = (Interval) o;

        if (start != null ? !start.equals(interval.start) : interval.start != null) return false;
        return end != null ? end.equals(interval.end) : interval.end == null;
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }
}
