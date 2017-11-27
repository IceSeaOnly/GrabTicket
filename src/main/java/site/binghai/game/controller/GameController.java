package site.binghai.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.binghai.game.constant.DataPool;
import site.binghai.game.entity.JsonReturn;
import site.binghai.game.entity.Ticket;
import site.binghai.game.service.DanMuService;
import site.binghai.game.service.TicketService;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by binghai on 2017/11/27.
 * 抢票主要逻辑
 *
 * @ GrabTicket
 */
@RequestMapping("user")
@RestController
public class GameController {
    private static String TEXTSHOW = "默认字幕...默认字幕...默认字幕...默认字幕";
    private static String PASSCODE = "qwerasdf";
    private static Boolean DBWRITE = Boolean.FALSE;
    private static BlockingQueue<Ticket> tickets = new LinkedBlockingQueue<>();
    private static BlockingQueue<Ticket> dbQueue = new LinkedBlockingQueue<>();
    private static Map<String, Ticket> belongs = new ConcurrentHashMap<>();

    @Autowired
    private TicketService ticketService;
    @Autowired
    private DanMuService danMuService;

    @RequestMapping("gameBegin")
    public Object begin() {
        return DataPool.gameBegin() ? gameBegin() : gameNotBegin();
    }

    private Object gameNotBegin() {
        return JsonReturn.success(null, "抢票开始!");
    }

    private Object gameBegin() {
        return JsonReturn.fail(null, "抢票尚未开始!");
    }

    @RequestMapping("danMuList")
    public Object danMuList() {
        return JsonReturn.success(danMuService.listTop100());
    }

    @RequestMapping("danMu")
    public Object danMu(@RequestParam String content) {
        danMuService.save(content);
        return JsonReturn.success(null);
    }

    @RequestMapping("textShow")
    public Object textShow() {
        return JsonReturn.success(TEXTSHOW, TEXTSHOW);
    }

    @RequestMapping(value = "editTextShow", method = RequestMethod.POST)
    public Object textShow(@RequestParam String pass, @RequestParam String content) {
        if (!pass.equals(PASSCODE)) {
            return JsonReturn.fail(null, "非法访问");
        }
        TEXTSHOW = content;
        return JsonReturn.success(TEXTSHOW, TEXTSHOW);
    }

    @RequestMapping("preHeatSys")
    public Object preHeatSys(@RequestParam String pass) {
        if (!pass.equals(PASSCODE)) {
            return JsonReturn.fail(null, "非法访问");
        }
        DBWRITE = Boolean.FALSE;
        tickets.clear();
        tickets.addAll(ticketService.listAll());
        return JsonReturn.success(null, "OK");
    }

    @RequestMapping
    public Object grabTicket(@RequestParam String openId) {
        Ticket ticket = grabOneTicket(openId);
        if (ticket != null) {
            return JsonReturn.success(ticket, "恭喜!抢票成功!");
        }
        return JsonReturn.fail(ticket, "好遗憾,没有抢到!");
    }

    /**
     * 同步抢票
     * */
    private synchronized Ticket grabOneTicket(String openId) {
        if (!DBWRITE) {
            DBWRITE = Boolean.TRUE;
            startWriteThread();
        }
        try {
            Ticket ticket = tickets.poll(10, TimeUnit.MILLISECONDS);
            if (ticket != null) {
                ticket.setOpenId(openId);
                belongs.put(openId, ticket);
                dbQueue.put(ticket);
                return ticket;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 开启写磁盘线程
     */
    private void startWriteThread() {

    }
}
