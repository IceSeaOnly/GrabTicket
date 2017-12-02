package site.binghai.game.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import site.binghai.game.entity.Ticket;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
public interface TicketDao extends JpaRepository<Ticket, Integer> {
    @Transactional
    @Modifying
    @Query(value = "update ticket set open_id = null ,`name` = null ,passwd = null ,phone = null ,bound = 0,bind_time=0", nativeQuery = true)
    int resetAll();

    List<Ticket> findAllByPasswd(String qrcode);
}
