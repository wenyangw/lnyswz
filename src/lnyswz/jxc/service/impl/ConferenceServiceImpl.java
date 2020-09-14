package lnyswz.jxc.service.impl;

import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Conference;
import lnyswz.jxc.model.TConference;

import lnyswz.jxc.service.ConferenceServiceI;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("conferenceService")
public class ConferenceServiceImpl implements ConferenceServiceI {
	private BaseDaoI<TConference> conDao;

    @Override
    public Conference getCon() {
        TConference t = conDao.get(" from TConference where valid = '1' ");
        Conference c = new Conference();
        c.setText(t.getText());
        return c;
    }

    @Override
    public Conference add(Conference conference) {
        TConference t = conDao.get(" from TConference where valid = '1' ");
        if( t != null) {
            t.setValid("0");
            conDao.update(t);
        }
        TConference tc = new TConference();
        tc.setValid("1");
        tc.setText(conference.getText());
        tc.setCreateTime(new Date());
        conDao.save(tc);
        BeanUtils.copyProperties(tc,conference);
        return conference;
    }

    @Autowired
    public void setConferenceDao(BaseDaoI<TConference> conDao) {
        this.conDao = conDao;
    }
}
