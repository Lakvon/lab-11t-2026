import exceptions.IllegalInputTimeSpanException;
import exceptions.IllegalScaleFactorException;
import exceptions.NegativeTimeSpanException;

public class TimeSpan {
    private int totalMinutes;

    public TimeSpan() {
        this(0);
    }

    public TimeSpan(int minutes) {
        setTotal(minutes);
    }

    public TimeSpan(int hours, int minutes) {
        setTotal(toMinutes(hours, minutes));
    }

    public TimeSpan(TimeSpan span) {
        this(requireSpan(span).getHours(), span.getMinutes());
    }

    private static TimeSpan requireSpan(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException("Часовий інтервал не задано.");
        }
        return span;
    }

    private static long toMinutes(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException("Години мають бути невід’ємні, хвилини — від 0 до 59.");
        }
        return 60L * hours + minutes;
    }

    private void setTotal(long value) {
        if (value < 0 || value > Integer.MAX_VALUE) {
            throw new IllegalInputTimeSpanException("Кількість хвилин поза допустимим діапазоном.");
        }
        totalMinutes = (int) value;
    }

    public int getHours() {
        return totalMinutes / 60;
    }

    public int getMinutes() {
        return totalMinutes % 60;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public double getTotalHours() {
        return totalMinutes / 60.0;
    }

    public void add(int hours, int minutes) {
        setTotal(totalMinutes + toMinutes(hours, minutes));
    }

    public void add(int minutes) {
        add(new TimeSpan(minutes));
    }

    public void add(TimeSpan span) {
        add(requireSpan(span).getHours(), span.getMinutes());
    }

    public void subtract(int hours, int minutes) {
        long result = totalMinutes - toMinutes(hours, minutes);
        if (result < 0) {
            throw new NegativeTimeSpanException("Результат віднімання був би від’ємним.");
        }
        setTotal(result);
    }

    public void subtract(int minutes) {
        subtract(new TimeSpan(minutes));
    }

    public void subtract(TimeSpan span) {
        subtract(requireSpan(span).getHours(), span.getMinutes());
    }

    public void scale(int factor) {
        if (factor <= 0) {
            throw new IllegalScaleFactorException("Множник має бути більшим за нуль.");
        }
        setTotal((long) totalMinutes * factor);
    }
}
