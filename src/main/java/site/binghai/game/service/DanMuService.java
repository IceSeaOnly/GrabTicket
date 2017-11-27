package site.binghai.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.game.dao.DanMuDao;
import site.binghai.game.entity.DanMu;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    private List<DanMu> dmCache = new ArrayList<>();
    private long lastUpdate = 0;

    public List<DanMu> listTop100() {
        if(lastUpdate + 10000 < System.currentTimeMillis()){
            updateDM();
        }
        return dmCache;
    }

    private synchronized void updateDM() {
        if(lastUpdate + 10000 > System.currentTimeMillis()){
            return;
        }
        lastUpdate = System.currentTimeMillis();
        dmCache.clear();
        List<DanMu> old = dmCache;
        List<DanMu> ls = danMuDao.listTop100();
        dmCache = ls;
        old.clear();
    }

    @Transactional
    public void save(String content) {
        DanMu danMu = new DanMu();
        danMu.setMessage(content);
        danMu.setVote(randomGoodPoints());
        danMuDao.save(danMu);
    }

    private int randomGoodPoints() {
        long v = System.currentTimeMillis() % 50;
        return (int) v;
    }
}
