package lnyswz.jxc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Shdz;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.ShdzServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/jxc")
@Action("shdzAction")
public class ShdzAction extends BaseAction implements ModelDriven<Shdz> {
    private static final long serialVersionUID = 1L;
    private Shdz shdz = new Shdz();
    private ShdzServiceI shdzService;

    /**
     * 增加送货地址
     */
    public void add() {
        Json j = new Json();
        try {
            User u = (User) session.get("user");
            shdz.setUserId(u.getId());
            Shdz r = shdzService.add(shdz);
            j.setSuccess(true);
            j.setMsg("增加送货地址成功");
            j.setObj(r);
        } catch (Exception e) {
            j.setMsg("增加计量单位失败");
            e.printStackTrace();
        }
        writeJson(j);
    }

    /**
     * 修改送货地址
     */
    public void edit() {
        Json j = new Json();
        try {
            User u = (User) session.get("user");
            shdz.setUserId(u.getId());
            shdzService.edit(shdz);
            j.setSuccess(true);
            j.setMsg("编辑送货地址成功！");
        } catch (Exception e) {
            j.setMsg("编辑送货地址失败！");
            e.printStackTrace();
        }
        writeJson(j);
    }

    /**
     * 删除送货地址
     */
    public void delete() {
        Json j = new Json();
        try {
            User u = (User) session.get("user");
            shdz.setUserId(u.getId());
            shdzService.delete(shdz);
            j.setSuccess(true);
            j.setMsg("删除送货地址成功！");
        } catch (Exception e) {
            j.setMsg("删除送货地址失败！");
            e.printStackTrace();
        }
        writeJson(j);
    }

    public void listShdz() {
        super.writeJson(shdzService.listShdz(shdz));
    }

    public void datagrid() {
        super.writeJson(shdzService.datagrid(shdz));
    }

    public void shdzDg(){
        super.writeJson(shdzService.shdzDg(shdz));
    }

    public Shdz getModel() {
        return shdz;
    }

    @Autowired
    public void setShdzService(ShdzServiceI shdzService) {
        this.shdzService = shdzService;
    }
}
