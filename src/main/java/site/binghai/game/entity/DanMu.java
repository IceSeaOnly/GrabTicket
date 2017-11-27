package site.binghai.game.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    private String content;
    private int good;
}
