package site.binghai.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.game.dao.TicketDao;
import site.binghai.game.entity.Ticket;

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
}
