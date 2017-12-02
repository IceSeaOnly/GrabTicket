package site.binghai.game.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
@Entity
@Data
public class DanMu {
    @Id
    @GeneratedValue
    private int id;
    @Transient
    private String state = "1";
    private String openId;
    @Transient
    private String headimg = "img/tanmuhead.jpg";
    private String message;
    private int vote;
}
