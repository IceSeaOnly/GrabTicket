package site.binghai.game.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import site.binghai.game.entity.Ticket;

/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
public interface TicketDao extends JpaRepository<Ticket, Integer> {
}
