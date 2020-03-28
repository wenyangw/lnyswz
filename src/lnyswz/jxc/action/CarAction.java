package lnyswz.jxc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Car;
import lnyswz.jxc.bean.User;
import lnyswz.jxc.service.CarServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/jxc")
@Action("carAction")
public class CarAction extends BaseAction implements ModelDriven<Car> {
	private static final long serialVersionUID = 1L;
	private Car car = new Car();
	private CarServiceI carService;

	public void updateCar(){
		User user = (User)session.get("user");
		car.setCreateId(user.getId());
		car.setCreateName(user.getRealName());

		Json j = new Json();
		try{
			carService.updateCar(car);
			j.setSuccess(true);
			j.setMsg("车辆安排成功！");
		}catch(Exception e){
			j.setMsg("车辆安排失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	public void listCar() {
		writeJson(carService.listCar(car));
	}

	public void getSelectCar(){writeJson(carService.getSelectCar(car));};

	public Car getModel() {
		return car;
	}

	@Autowired
	public void setCarService(CarServiceI carService) {
		this.carService = carService;
	}
}