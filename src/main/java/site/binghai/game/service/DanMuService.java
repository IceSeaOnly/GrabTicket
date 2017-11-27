package site.binghai.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.game.dao.DanMuDao;
import site.binghai.game.entity.DanMu;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
@Service
public class DanMuService {
    @Autowired
    private DanMuDao danMuDao;

    public List<DanMu> listTop100() {
        return danMuDao.listTop100();
    }

    @Transactional
    public void save(String content) {
        DanMu danMu = new DanMu();
        danMu.setContent(content);
        danMu.setGood(randomGoodPoints());
        danMuDao.save(danMu);
    }

    private int randomGoodPoints() {
        long v = System.currentTimeMillis() % 50;
        return (int) v;
    }
}
