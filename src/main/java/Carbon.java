public class Carbon {
    public static void main(String[] args) {
        String horizontalLine = "_".repeat(60);
        String logo = """
                   ____           _
                  / ___|__ _ _ __| |__   ___  _ __
                 | |   / _` | '__| '_ \\ / _ \\| '_ \\
                 | |__| (_| | |  | |_) | (_) | | | |
                  \\____\\__,_|_|  |_.__/ \\___/|_| |_|""";
        String welcomeMessage = "Hello! What can I do for you?";
        String exitMessage = "Goodbye!";
        System.out.printf("%s\n%s\n\n%s\n", horizontalLine, logo, welcomeMessage);
        System.out.printf("%s\n%s\n%s\n", horizontalLine, exitMessage, horizontalLine);
    }
}
