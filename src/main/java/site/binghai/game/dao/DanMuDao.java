package site.binghai.game.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.binghai.game.entity.DanMu;

import java.util.List;

/**
 * Created by binghai on 2017/11/27.
 *
 * @ GrabTicket
 */
public interface DanMuDao extends JpaRepository<DanMu, Integer> {

    @Query(value = "select * from dan_mu order by id desc limit 100",nativeQuery = true)
    List<DanMu> listTop100();
}
