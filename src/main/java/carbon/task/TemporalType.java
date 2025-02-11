package carbon.task;

/**
 * A TemporalType is an enum that is created to work with Temporal.
 * It represents one of the following categories:
 * <ul>
 *     <li>Date</li>
 *     <li>Time</li>
 *     <li>Datetime</li>
 *     <li>Text (none of the above)</li>
 * </ul>
 */
public enum TemporalType {
    DATE,
    TIME,
    DATETIME,
    TEXT
}
