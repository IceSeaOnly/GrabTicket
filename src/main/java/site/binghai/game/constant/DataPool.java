package site.binghai.game.constant;

import site.binghai.game.entity.Ticket;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
public class DataPool {
    private static long startTime = System.currentTimeMillis() + 999999999;
    private static String TEXTSHOW = "抢票尚未开始，敬请期待哦~";
    private static String PASSCODE = "qwerasdf";
    private static Boolean DBWRITE = Boolean.FALSE;
    private static String indexPage = "";
    private static BlockingQueue<Ticket> tickets = new LinkedBlockingQueue<>();
    private static BlockingQueue<Ticket> dbQueue = new LinkedBlockingQueue<>();
    private static Map<String, Ticket> belongs = new ConcurrentHashMap<>();
    private static String TimeRemaining = null;

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime(long startTime) {
        DataPool.startTime = startTime;
    }

    public static boolean gameBegin() {
        return System.currentTimeMillis() > getStartTime();
    }

    public static String getTEXTSHOW() {
        return TEXTSHOW;
    }

    public static String getPASSCODE() {
        return PASSCODE;
    }

    public static void setPASSCODE(String PASSCODE) {
        DataPool.PASSCODE = PASSCODE;
    }

    public static void setTEXTSHOW(String TEXTSHOW) {
        DataPool.TEXTSHOW = TEXTSHOW;
    }

    public static Boolean getDBWRITE() {
        return DBWRITE;
    }

    public static void setDBWRITE(Boolean DBWRITE) {
        DataPool.DBWRITE = DBWRITE;
    }

    public static BlockingQueue<Ticket> getTickets() {
        return tickets;
    }

    public static BlockingQueue<Ticket> getDbQueue() {
        return dbQueue;
    }

    public static Map<String, Ticket> getBelongs() {
        return belongs;
    }

    public static String getIndexPage() {
        return indexPage;
    }

    public static void setIndexPage(String indexPage) {
        DataPool.indexPage = indexPage;
    }

    public static String getHowLongBeforeBegin() {
        if (TimeRemaining == null) {
            startTimeCountDown();
        }
        return TimeRemaining;
    }

    private synchronized static void startTimeCountDown() {
        if (TimeRemaining != null) return;
        setTimeString();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(1000);
                        setTimeString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private static void setTimeString() {
        long time = getStartTime() - System.currentTimeMillis();
        time /= 1000;
        time = time > 0 ? time : 0;
        time = time > 3599 ? 3599 : time;
        TimeRemaining = String.format("%02d:%02d", (int) (time / 60), time % 60);
    }
}
