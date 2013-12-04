package ca.quadrilateral.jua.game.exception;

public class JUARuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JUARuntimeException() {
        super();
    }

    public JUARuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JUARuntimeException(String message) {
        super(message);
    }

    public JUARuntimeException(Throwable cause) {
        super(cause);
    }
}
