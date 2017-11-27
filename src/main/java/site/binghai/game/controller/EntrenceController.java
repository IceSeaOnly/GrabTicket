package site.binghai.game.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by binghai on 2017/11/26.
 * 入口类，重定向到指定页面
 * @ GrabTicket
 */
@RequestMapping("user")
public class EntrenceController {
    private static String indexPage = "";

    /**
     * 请求入口
     */
    @RequestMapping("entrance")
    public Object entrance(@RequestParam String openId) {
        //todo 日志埋点
        return "redirect:" + indexPage + "?openId=" + openId;
    }
}
