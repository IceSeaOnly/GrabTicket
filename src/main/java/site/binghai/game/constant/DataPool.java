package site.binghai.game.constant;

/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
public class DataPool {
    private static long startTime = 1l;

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime(long startTime) {
        DataPool.startTime = startTime;
    }

    public static boolean gameBegin() {
        return System.currentTimeMillis() > getStartTime();
    }
}
