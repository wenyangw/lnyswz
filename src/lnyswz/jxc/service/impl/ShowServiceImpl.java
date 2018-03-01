package lnyswz.jxc.service.impl;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Show;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.ShowServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("showService")
public class ShowServiceImpl implements ShowServiceI {
    private BaseDaoI<TSp> dao;

    @Override
    public DataGrid showXsth(Show show) {

        String sql = "select ywymc, khmc, xsthlsh, createTime, dbo.getXsthStatusInfo(xsthlsh) status, dbo.getXsthStatusTime(xsthlsh) delayTime, dbo.getXsthStatus(xsthlsh) statusId";
        String where = " from t_xsth where bmbh = '05' and isCancel = '0' and isZs = '0' and YEAR(createTime) = 2018 and needAudit <> '0'";
        String orderBy = " order by createTime desc";
        List<Show> lists = new ArrayList<Show>();
        List<Object[]> results = dao.findBySQL(sql + where + orderBy, show.getPage(), show.getRows());
        Show s = null;
        for (Object[] result : results) {
            s = new Show();
            s.setYwymc(result[0].toString());
            s.setKhmc(result[1].toString());
            s.setXsthlsh(result[2].toString());
            s.setCreateTime(DateUtil.stringToDate(result[3].toString(), DateUtil.DATETIME_PATTERN));
            s.setStatus(result[4].toString());
            s.setDelayTime(result[5].toString());
            s.setStatusId(result[6].toString());

            lists.add(s);
        }

        DataGrid dg = new DataGrid();
        dg.setRows(lists);
        dg.setTotal(dao.countBySQL("select count(*)" + where));

        return dg;
    }

    @Autowired
    public void setDao(BaseDaoI<TSp> dao) {
        this.dao = dao;
    }
}
