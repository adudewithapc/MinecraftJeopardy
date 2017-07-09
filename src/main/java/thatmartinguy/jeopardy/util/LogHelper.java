package thatmartinguy.jeopardy.util;

import org.apache.logging.log4j.*;

public class LogHelper
{
    private static Logger logger = LogManager.getLogger();
    private static final Marker MARKER = MarkerManager.getMarker(Reference.MOD_ID);

    public static void log(Object message, Level level)
    {
        logger.log(level, MARKER, message);
    }

    public static void fatal(Object message)
    {
        log(message, Level.FATAL);
    }

    public static void error(Object message)
    {
        log(message, Level.ERROR);
    }

    public static void WARN(Object message)
    {
        log(message, Level.WARN);
    }

    public static void info(Object message)
    {
        log(message, Level.INFO);
    }
}