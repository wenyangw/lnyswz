package lnyswz.jxc.service.impl;

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
        String hql = "from TVersion t order by versionCode desc";
        TVersion tVersion = versionDao.get(hql);
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
