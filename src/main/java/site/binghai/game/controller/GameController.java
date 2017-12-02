package site.binghai.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
        if (arr.isEmpty()) {
            return JsonReturn.fail(null, "");
        }
        return JsonReturn.success(arr.get(randomIndex(arr.size())));
    }

    private int randomIndex(int max) {
        Random random = new Random();
        int v = random.nextInt(max);
        return v > 0 ? v - 1 : 0;
    }

    /**
     * 发弹幕
     */
    @RequestMapping(value = "danMu", method = RequestMethod.POST)
    public Object danMu(@RequestParam String content,@RequestParam String openId) {
        danMuService.save(content,openId);
        return JsonReturn.success(null);
    }

    /**
     * 获取文本
     */
    @RequestMapping("textShow")
    public Object textShow() {
        return JsonReturn.success(getTEXTSHOW(), getTEXTSHOW());
    }


    private static final String GRAB_FAILED = JsonReturn.fail(null, "好遗憾,没有抢到!").toJSONString();

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
        return GRAB_FAILED;
    }

    /**
     * 同步抢票
     */
    private Ticket grabOneTicket(String openId) {
        if (!getDBWRITE()) {
            setDbWrite();
        }
        try {
            Ticket ticket = getBelongs().get(openId);
            if (ticket != null) {
                return ticket;
            }
            if (getTickets().size() == 0) {
                return null;
            }
            ticket = getTickets().poll(4, TimeUnit.MILLISECONDS);
            if (ticket != null) {
                ticket.setOpenId(openId);
                getBelongs().put(openId, ticket);
                getDbQueue().put(ticket);
                System.out.println(String.format("%s 已抢,openId=%s, 剩余 %d 个", ticket.getPosition(), ticket.getOpenId(), getTickets().size()));
                return ticket;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String OK = JsonReturn.success("ok", "ok").toJSONString();

    @RequestMapping("updateUserInfo")
    public Object updateUserInfo(@RequestParam String openId, @RequestParam String name, @RequestParam String phone) {
        Ticket ticket = getBelongs().get(openId);
        if (ticket != null && !ticket.isConsumed() && StringUtils.isEmpty(ticket.getName()) && StringUtils.isEmpty(ticket.getPhone())) {
            ticket.setName(name);
            ticket.setPhone(phone);
            getDbQueue().add(ticket);
            System.out.println(String.format("更新成功,openId=%s,name=%s,phone=%s", openId, name, phone));
        } else {
            System.out.println(String.format("非法更新,openId=%s,name=%s,phone=%s", openId, name, phone));
        }
        return OK;
    }

    private synchronized void setDbWrite() {
        if (getDBWRITE()) return;
        setDBWRITE(Boolean.TRUE);
        startWriteThread();
    }

    /**
     * 获取剩余时间
     */
    @RequestMapping("timeBeforBegin")
    public String timeBeforBegin() {
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
