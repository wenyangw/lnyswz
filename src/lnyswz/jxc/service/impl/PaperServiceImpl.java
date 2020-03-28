package lnyswz.jxc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.jxc.bean.Message;
import lnyswz.jxc.bean.Paper;
import lnyswz.jxc.model.*;
import lnyswz.jxc.service.MessageServiceI;
import lnyswz.jxc.service.PaperServiceI;
import lnyswz.jxc.util.Upload;
import lnyswz.jxc.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("paperService")
public class PaperServiceImpl implements PaperServiceI {
	private BaseDaoI<TPaper> paperDao;

	@Override
	public DataGrid getPapers(Paper paper){
		DataGrid dg = new DataGrid();
		String hql = "from TPaper t where t.TMessage.id = :messageId and t.type = :type";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("messageId", paper.getMessageId());
		params.put("type", paper.getType());
		List<TPaper> tPapers = paperDao.find(hql, params);
		List<Paper> papers = null;
		if(tPapers != null && tPapers.size() > 0 ){
			papers = new ArrayList<Paper>();
			Paper p = null;
			for (TPaper tPaper : tPapers) {
				p = new Paper();
				BeanUtils.copyProperties(tPaper, p);
				p.setFilepath(p.getFilepath().replace("/lnyswz", ""));
				papers.add(p);
			}
			dg.setRows(papers);
		}

		return dg;
	}

	@Autowired
	public void setPaperDao(BaseDaoI<TPaper> paperDao) {
		this.paperDao = paperDao;
	}
}
