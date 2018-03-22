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

	public void listCar() {
		writeJson(carService.listCar(car));
	}

	public Car getModel() {
		return car;
	}

	@Autowired
	public void setCarService(CarServiceI carService) {
		this.carService = carService;
	}
}