package site.binghai.game.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue
    private int id;
    private String position;
    private String openId;
    private String passwd;
    private String name;
    private String phone;
    private boolean bound;
    private long bindTime;

    public void setOpenId(String openId) {
        this.openId = openId;
        this.bound = true;
        this.bindTime = System.currentTimeMillis();
        this.passwd = UUID.randomUUID().toString();
    }
}
