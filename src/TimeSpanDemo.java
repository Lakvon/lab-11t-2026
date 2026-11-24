import exceptions.IllegalInputTimeSpanException;
import exceptions.IllegalScaleFactorException;
import exceptions.NegativeTimeSpanException;

public class TimeSpanDemo {
    public static void main(String[] args) {
        TimeSpan span = new TimeSpan(1, 45);
        span.add(30);
        System.out.printf("1 год 45 хв + 30 хв = %d год %d хв%n", span.getHours(), span.getMinutes());
        span.scale(2);
        System.out.println("Після множення на 2: " + span.getTotalMinutes() + " хв");

        try {
            new TimeSpan(1, 60);
        } catch (IllegalInputTimeSpanException error) {
            System.out.println(error.getMessage());
        }
        try {
            span.subtract(300);
        } catch (NegativeTimeSpanException error) {
            System.out.println(error.getMessage());
        }
        try {
            span.scale(0);
        } catch (IllegalScaleFactorException error) {
            System.out.println(error.getMessage());
        }
        System.out.println("Інтервал після відхилених операцій: " + span.getTotalMinutes() + " хв");
    }
}
