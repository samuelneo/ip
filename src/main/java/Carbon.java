public class Carbon {
    private static final String horizontalLine = "_".repeat(60);

    /**
     * Prints the message in between two horizontal lines, with an indentation of 4 spaces
     */
    private static void printMessage(String message) {
        System.out.println("    " + horizontalLine);
        System.out.println("    " + message.replace("\n", "\n    "));
        System.out.println("    " + horizontalLine);
    }

    public static void main(String[] args) {
        String logo = """
                   ____           _
                  / ___|__ _ _ __| |__   ___  _ __
                 | |   / _` | '__| '_ \\ / _ \\| '_ \\
                 | |__| (_| | |  | |_) | (_) | | | |
                  \\____\\__,_|_|  |_.__/ \\___/|_| |_|""";
        String welcomeMessage = "Hello! What can I do for you?";
        String exitMessage = "Goodbye!";
        printMessage(logo + "\n" + welcomeMessage);
        printMessage(exitMessage);
    }
}
