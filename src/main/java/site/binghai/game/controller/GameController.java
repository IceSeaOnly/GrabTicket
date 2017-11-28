package site.binghai.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.binghai.game.constant.DataPool;
import site.binghai.game.entity.DanMu;
import site.binghai.game.entity.JsonReturn;
import site.binghai.game.entity.Ticket;
import site.binghai.game.service.DanMuService;
import site.binghai.game.service.TicketService;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static site.binghai.game.constant.DataPool.*;

/**
 * Created by binghai on 2017/11/27.
 * 抢票主要逻辑
 *
 * @ GrabTicket
 */
@RequestMapping("user")
@RestController
@CrossOrigin
public class GameController {
    @Autowired
    private DanMuService danMuService;
    @Autowired
    private TicketService ticketService;

    /**
     * 抢票是否开始查询
     */
    @RequestMapping("gameBegin")
    public Object begin() {
        return DataPool.gameBegin() ? gameBegin() : gameNotBegin();
    }

    private Object gameBegin() {
        return JsonReturn.success(null, "抢票开始!");
    }

    private Object gameNotBegin() {
        return JsonReturn.fail(null, "抢票尚未开始!");
    }

    /**
     * 拉取弹幕
     */
    @CrossOrigin
    @RequestMapping("danMuList")
    public Object danMuList() {
        List<DanMu> arr = danMuService.listTop100();
        return JsonReturn.success(arr.get(randomIndex(arr.size())));
    }

    private int randomIndex(int max) {
        Random random = new Random();
        int v = random.nextInt(max - 1);
        return v >= 0 ? v : 0;
    }

    /**
     * 发弹幕
     */
    @RequestMapping(value = "danMu", method = RequestMethod.POST)
    public Object danMu(@RequestParam String content) {
        danMuService.save(content);
        return JsonReturn.success(null);
    }

    /**
     * 获取文本
     */
    @RequestMapping("textShow")
    public Object textShow() {
        return JsonReturn.success(getTEXTSHOW(), getTEXTSHOW());
    }


    /**
     * 抢票入口
     */
    @RequestMapping("grabTicket")
    public Object grabTicket(@RequestParam String openId) {
        if (!DataPool.gameBegin()) {
            return gameNotBegin();
        }

        Ticket ticket = grabOneTicket(openId);
        if (ticket != null) {
            return JsonReturn.success(ticket, "恭喜!抢票成功!");
        }
        return JsonReturn.fail(ticket, "好遗憾,没有抢到!");
    }

    /**
     * 同步抢票
     */
    private synchronized Ticket grabOneTicket(String openId) {
        if (!getDBWRITE()) {
            setDBWRITE(Boolean.TRUE);
            startWriteThread();
        }
        try {
            Ticket ticket = getBelongs().get(openId);
            if (ticket != null) {
                return ticket;
            }
            ticket = getTickets().poll(10, TimeUnit.MILLISECONDS);
            if (ticket != null) {
                ticket.setOpenId(openId);
                getBelongs().put(openId, ticket);
                getDbQueue().put(ticket);
                return ticket;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取剩余时间
     * */
    @RequestMapping("timeBeforBegin")
    public String timeBeforBegin(){
        return DataPool.getHowLongBeforeBegin();
    }

    /**
     * 开启写磁盘线程
     */
    private void startWriteThread() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Ticket ticket = getDbQueue().take();
                        if (ticket != null) {
                            ticketService.update(ticket);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
