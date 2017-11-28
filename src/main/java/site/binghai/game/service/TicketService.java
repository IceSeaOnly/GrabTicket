package site.binghai.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import site.binghai.game.dao.TicketDao;
import site.binghai.game.entity.Ticket;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
@Service
public class TicketService {
    @Autowired
    private TicketDao ticketDao;

    public List<Ticket> listAll(){
        return ticketDao.findAll();
    }

    @Transactional
    public void update(Ticket ticket) {
        ticketDao.save(ticket);
    }

    @Transactional
    @Modifying
    public int resetAll() {
        return ticketDao.resetAll();
    }

    public Ticket findByQrCode(String qrcode) {
        return null;
    }
}
