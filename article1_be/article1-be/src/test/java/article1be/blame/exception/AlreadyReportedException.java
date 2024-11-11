package article1be.blame.exception;

public class AlreadyReportedException extends RuntimeException {

    public AlreadyReportedException() {
        super("이미 신고된 항목입니다.");
    }

    public AlreadyReportedException(String message) {
        super(message);
    }

    public AlreadyReportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyReportedException(Throwable cause) {
        super(cause);
    }
}
