package carbon.exceptions;

/**
 * An InvalidCommandException is thrown if the user's input is not a valid command.
 */
public class InvalidCommandException extends RuntimeException {
    /**
     * Constructs an InvalidCommandException with the specified message.
     *
     * @param message The message, which may be retrieved with <code>.getMessage()</code>.
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
