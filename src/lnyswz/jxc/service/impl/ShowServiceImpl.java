package lnyswz.jxc.service.impl;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.dao.impl.BaseDaoImpl;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Show;
import lnyswz.jxc.model.TSp;
import lnyswz.jxc.service.ShowServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("showService")
public class ShowServiceImpl implements ShowServiceI {
    private BaseDaoI<TSp> dao;

    @Override
    public DataGrid showXsth(Show show) {
        StringBuilder sqlSb = new StringBuilder();
        StringBuilder sqlCountSb = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sqlSb.append("select ywymc, khmc, xsthlsh, createTime, thfs, dbo.getXsthStatusInfo(xsthlsh) status, dbo.getXsthStatusTime(xsthlsh) delayTime, dbo.getXsthStatus(xsthlsh) statusId");
        sqlCountSb.append("select count(*)");
        String where = " from v_show_xsth";
        sqlSb.append(where);
        sqlCountSb.append(where);
        String bmWhere = null;
        if(show.getBmbh().equals("01") || show.getBmbh().equals("05")){
            bmWhere = " where bmbh = ?";
            params.put("0", show.getBmbh());
        }else{
            bmWhere = " where bmbh in ('05', '01')";
        }
        sqlSb.append(bmWhere);
        sqlCountSb.append(bmWhere);
        sqlSb.append(" order by createTime desc");

        List<Show> lists = new ArrayList<Show>();
        List<Object[]> results = dao.findBySQL(sqlSb.toString(), params, show.getPage(), show.getRows());
        Show s = null;
        for (Object[] result : results) {
            s = new Show();
            s.setYwymc(result[0].toString());
            s.setKhmc(result[1].toString());
            s.setXsthlsh(result[2].toString());
            s.setCreateTime(DateUtil.stringToDate(result[3].toString(), DateUtil.DATETIME_NOSECOND_PATTERN));
            s.setThfs(result[4].toString().equals("0") ? "送货" : "自提");
            s.setStatus(result[5].toString());
            s.setDelayTime(result[6].toString());
            s.setStatusId(result[7].toString());

            lists.add(s);
        }

        DataGrid dg = new DataGrid();
        dg.setRows(lists);
        dg.setTotal(dao.countSQL(sqlCountSb.toString(), params));

        return dg;
    }

    @Autowired
    public void setDao(BaseDaoI<TSp> dao) {
        this.dao = dao;
    }
}
