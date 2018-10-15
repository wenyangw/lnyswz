package lnyswz.jxc.service.impl;

import java.util.HashMap;
import java.util.Map;

import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Version;
import lnyswz.jxc.model.TVersion;
import lnyswz.jxc.service.VersionServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("versionService")
public class VersionServiceImpl implements VersionServiceI {
	private BaseDaoI<TVersion> versionDao;

    @Override
    public Version getVersion(Version version) {
  
        String hql = "from TVersion t where appName = :appName order by versionCode desc";
        Map<String, Object> params = new HashMap<String, Object>();
		String appName = version.getAppName();
        if( appName == null ){
        	appName = "lwt";
		}
        params.put("appName",appName);
        TVersion tVersion = versionDao.get(hql, params);

        if(tVersion != null){
            version.setVersionCode(tVersion.getVersionCode());
            version.setVersionName(tVersion.getVersionName());
        }
        
        return version;
    }

	@Autowired
	public void setVersionDao(BaseDaoI<TVersion> versionDao) {
		this.versionDao = versionDao;
	}
}
