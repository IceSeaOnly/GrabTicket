package site.binghai.game.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.binghai.game.entity.JsonReturn;
import site.binghai.game.entity.Ticket;
import site.binghai.game.service.TicketService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static site.binghai.game.constant.DataPool.*;

/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
@RequestMapping("admin")
@RestController
public class AdminController {

    @Autowired
    private TicketService ticketService;

    /**
     * 编辑文本
     */
    @RequestMapping(value = "editTextShow", method = RequestMethod.POST)
    public Object textShow(@RequestParam String pass, @RequestParam String content) {
        if (!pass.equals(getPASSCODE())) {
            return JsonReturn.fail(null, "非法访问");
        }
        setTEXTSHOW(content);
        return JsonReturn.success(getTEXTSHOW(), getTEXTSHOW());
    }

    /**
     * 系统预热
     */
    @RequestMapping("preHeatSys")
    public Object preHeatSys(@RequestParam String pass) {
        if (!pass.equals(getPASSCODE())) {
            return JsonReturn.fail(null, "非法访问");
        }
        setDBWRITE(Boolean.FALSE);
        List<Ticket> all = ticketService.listAll();
        getTickets().clear();
        getBelongs().clear();
        getDbQueue().clear();
        for (Ticket ticket : all) {
            if (ticket.isBound()) {
                getBelongs().put(ticket.getOpenId(), ticket);
            } else {
                getTickets().add(ticket);
            }
        }
        return JsonReturn.success(null, "OK");
    }

    /**
     * dump数据
     */
    @RequestMapping("dump")
    public Object dump(@RequestParam String pass) {
        if (!pass.equals(getPASSCODE())) {
            return JsonReturn.fail(null, "非法访问");
        }
        try {
            String jsonData = JSONObject.toJSONString(getBelongs());
            File dump = new File(System.currentTimeMillis() + ".dump");
            dump.createNewFile();
            FileWriter fw = new FileWriter(dump);
            fw.write(jsonData);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonReturn.success(null, "OK");
    }

    /**
     * 重置座位
     */
    @RequestMapping("resetDb")
    public Object resetDb(@RequestParam String pass) {
        if (!pass.equals(getPASSCODE())) {
            return JsonReturn.fail(null, "非法访问");
        }
        return JsonReturn.success(null, ticketService.resetAll() + " reset success.");
    }

    /**
     * 获取剩余座位
     */
    @RequestMapping("howManyEmpty")
    public Object howManyEmpty(@RequestParam String pass) {
        if (!pass.equals(getPASSCODE())) {
            return JsonReturn.fail(null, "非法访问");
        }
        return JsonReturn.success(null, getTickets().size() + " 个座位尚未锁定.");
    }

    /**
     * 设置开始时间
     */
    @RequestMapping("setStartTime")
    public Object setStartTimeTs(@RequestParam String pass, @RequestParam Long time) {
        if (!pass.equals(getPASSCODE())) {
            return JsonReturn.fail(null, "非法访问");
        }
        setStartTime(time);
        return JsonReturn.success(null, "OK");
    }

    /**
     * 扫描
     */
    @RequestMapping("scanQrCode")
    public Object scanQrCode(@RequestParam String pass, @RequestParam String qrcode) {
        Ticket ticket = ticketService.findByQrCode(qrcode);
        return ticket == null ? JsonReturn.fail(null, "此票不存在!") : JsonReturn.success(ticket, "");
    }

    /**
     * 检票
     */
    public Object checkTickets(@RequestParam String pass, @RequestParam String qrcode) {
        Ticket ticket = ticketService.findByQrCode(qrcode);
        if (ticket.isConsumed()) {
            return JsonReturn.fail(null, "此票已检");
        }
        ticket.setConsumed(true);
        ticketService.update(ticket);
        return JsonReturn.success(ticket, "检票成功!");
    }
}
