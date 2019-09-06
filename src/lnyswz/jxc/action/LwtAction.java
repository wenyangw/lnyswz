package lnyswz.jxc.action;

import lnyswz.jxc.bean.User;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.jxc.bean.Lwt;
import lnyswz.jxc.service.LwtServiceI;


@Namespace("/jxc")
@Action("lwtAction")
public class LwtAction extends BaseAction implements
		ModelDriven<Lwt> {
	private static final long serialVersionUID = 1L;
	private Lwt lwt = new Lwt();
	private LwtServiceI lwtService;

	
	/**
	 *手机
	 *根据业务员获取客户信息 
	 */
	public void listKhByYwy() {

		// super.writeJson(selectCommonService.selectCommonList(selectCommon));
		writeJson(lwtService.listKhByYwy(lwt));
	}

	/**
	 *手机
	 *根据业务员,客户获取销售提货信息 
	 */
	public void listKhByYwyXsth() {
		
		// super.writeJson(selectCommonService.selectCommonList(selectCommon));
		writeJson(lwtService.listKhByYwyXsth(lwt));
	}
	
	
	
	/**
	 *手机
	 *根据业务员,获取近三年销售统计 
	 */
	public void listXstjByYears() {
		// super.writeJson(selectCommonService.selectCommonList(selectCommon));
		writeJson(lwtService.listXstjByYears(lwt));
	}
	
	public void listKcsps() {
		writeJson(lwtService.listKcsps(lwt));
	}
	public void listKcspsBySpbh() {
		writeJson(lwtService.listKcspsBySpbh(lwt));
	}

	/**
	 * 获取商品列表
	 * 指定库存
	 * 当前实际可销售数量，
	 **/
	public void dgSpByCk(){
		writeJson(lwtService.dgSpByCk(lwt));
	}

	/**
	 * 获取部门全部商品列表
	 * 库存 is null
	 * 销售价格、成本
	 */
	public void dgSp(){
		writeJson(lwtService.dgSp(lwt));
	}

	/**
	 * 获取指定全部的客户列表，可查询
	 */
	public void dgKhs() {
		super.writeJson(lwtService.dgKhs(lwt));
	}

	/**
	 * 获取指定全部的客户列表，带指定业务员的授信信息，可查询
	 */
	public void dgKhDets() {
		super.writeJson(lwtService.dgKhDets(lwt));
	}


	/**
	 * 获取账号关联业务员数量
	 */
	public void countYwyByYwy() {
		super.writeJson(lwtService.countYwyByYwy(lwt));
	}
	

	public  void listXstjByMonth() {
		writeJson(lwtService.listXstjByMonth(lwt));
	}
	/**
	 * 获取账号关联业务员信息(含应收，未开票)
	 */
	public void getYwyByYwy() {
		super.writeJson(lwtService.getYwyByYwy(lwt));
	}
	
	/**
	 * 根据部门获取业务员(含应收，未开票)
	 */
	public void getYwyByBmbh() {
		super.writeJson(lwtService.getYwyByBmbh(lwt));
	}

	/**
	 * 获取指定业务员的客户列表（默认近三年有销售），可查询
	 */
	public void dgKhsByYwy() {
		super.writeJson(lwtService.dgKhsByYwy(lwt));
	}

	/**
	 * 获取销售提货明细
	 */
	public void getXsths(){
		User u = (User)session.get("user");
		if (u != null) {
			lwt.setCreateId(u.getId());
			if ("1".equals(u.getIsYwy())) {
				lwt.setYwyId(u.getId());
			}
		}
		writeJson(lwtService.getXsths(lwt));
	}

	public Lwt getModel() {
		return lwt;
	}

	@Autowired
	public void setLwtService(LwtServiceI lwtService) {
		this.lwtService = lwtService;
	}
}