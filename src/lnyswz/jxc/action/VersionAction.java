package lnyswz.jxc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.jxc.bean.Version;
import lnyswz.jxc.service.VersionServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/admin")
@Action("versionAction")
public class VersionAction extends BaseAction implements ModelDriven<Version> {
	private Version version = new Version();
	private VersionServiceI versionService;

	public void getVersion(){
		writeJson(versionService.getVersion(version));
	}

	public Version getModel() {
		return version;
	}

	@Autowired
	public void setVersionService(VersionServiceI versionService) {
		this.versionService = versionService;
	}
}