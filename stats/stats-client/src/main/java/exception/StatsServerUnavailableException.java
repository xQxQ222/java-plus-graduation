package exception;

public class StatsServerUnavailableException extends RuntimeException {
    public StatsServerUnavailableException(String message, Exception e) {
        super(message, e);
    }
}
