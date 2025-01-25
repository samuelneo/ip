package carbon.exceptions;

/**
 * An InvalidFileFormatException is thrown if the file being read is not in the expected format.
 */
public class InvalidFileFormatException extends RuntimeException {
    /**
     * Constructs an InvalidFileFormatException with the specified message.
     *
     * @param message The message, which may be retrieved with <code>.getMessage()</code>.
     */
    public InvalidFileFormatException(String message) {
        super(message);
    }

    /**
     * Constructs an InvalidFileFormatException with the default message "Invalid file format".
     */
    public InvalidFileFormatException() {
        this("Invalid file format");
    }
}
