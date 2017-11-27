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
    private static String TEXTSHOW = "默认字幕...默认字幕...默认字幕...默认字幕";
    private static String PASSCODE = "qwerasdf";
    private static Boolean DBWRITE = Boolean.FALSE;
    private static String indexPage = "";
    private static BlockingQueue<Ticket> tickets = new LinkedBlockingQueue<>();
    private static BlockingQueue<Ticket> dbQueue = new LinkedBlockingQueue<>();
    private static Map<String, Ticket> belongs = new ConcurrentHashMap<>();

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
}
