package fr.democraft.rcs.core.loggers;
import fr.democraft.rcs.core.SmartProvider;

public class FancyLogger implements SmartLogger {
    public static String prefix = " SmartRC ";

    @Override
    public void log(String message) {
        System.out.println("\uD83D\uDEF0\uFE0F " + Color.CYAN + prefix + Color.RESET + message);
    }

    @Override
    public void debug(String message) {
        if (SmartProvider.DEBUG) {
            System.out.println("\uD83D\uDC1B " + Color.CYAN + prefix + message);
        }
    }

    @Override
    public void error(String message) {
        System.out.println("\uD83D\uDD25 " + Color.RED + prefix + message);
    }
}
