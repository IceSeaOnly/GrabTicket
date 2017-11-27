package site.binghai.game.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
public class JsonReturn {
    public static JSONObject success(Object data) {
        return success(data, "");
    }

    public static JSONObject success(Object data, String msg) {
        JSONObject obj = new JSONObject();
        obj.put("code", "success");
        obj.put("msg", msg == null ? "" : msg);
        obj.put("data", data);
        return obj;
    }

    public static JSONObject fail(Object data) {
        return success(data, "");
    }

    public static JSONObject fail(Object data, String msg) {
        JSONObject obj = new JSONObject();
        obj.put("code", "failed");
        obj.put("msg", msg == null ? "" : msg);
        obj.put("data", data);
        return obj;
    }
}
