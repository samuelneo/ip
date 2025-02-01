package carbon;

import carbon.utils.Ui;

/**
 * Manages overall program logic.
 */
public class Carbon {
    private final Ui ui;

    /**
     * Creates a Carbon object.
     */
    public Carbon() {
        ui = new Ui();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        return ui.reply(input);
    }

    /**
     * Returns the most recent command processed by UI.
     *
     * @return Most recent command.
     */
    public String getCommand() {
        return ui.getMostRecentCommand();
    }
}
