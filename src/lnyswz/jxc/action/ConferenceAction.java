package lnyswz.jxc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;

import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Conference;

import lnyswz.jxc.service.ConferenceServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/jxc")
@Action("conferenceAction")
public class ConferenceAction extends BaseAction implements ModelDriven<Conference> {
	private static final long serialVersionUID = 1L;
	private Conference conference = new Conference();
	private ConferenceServiceI conferenceService;



	public  void add() {
		Json j = new Json();
		Conference c = conferenceService.add(conference);
		j.setObj(c);
	}

	public  void getCon() {
		writeJson(conferenceService.getCon());
	}

	public Conference getModel() {
		return conference;
	}

	@Autowired
	public void setConferencService(ConferenceServiceI conferenceService) {
		this.conferenceService = conferenceService;
	}

}

