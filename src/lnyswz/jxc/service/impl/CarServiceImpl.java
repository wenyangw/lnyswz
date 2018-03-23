package lnyswz.jxc.service.impl;

import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Car;
import lnyswz.jxc.model.TCar;
import lnyswz.jxc.service.CarServiceI;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("carService")
public class CarServiceImpl implements CarServiceI {
	private BaseDaoI<TCar> carDao;

	@Override
	public void updateCar(Car car) {
		String lsh = car.getLsh();
		String carIds = car.getCarIds();
	}

	@Override
	public List<Car> listCar(Car car) {
		String hql = "from TCar t";
		Map<String, Object> params = new HashMap<String, Object>();
		List<TCar> l = carDao.find(hql);
		return changeCar(l);
	}

	/**
	 * 数据转换
	 * 
	 * @param l
	 * @return
	 */
	private List<Car> changeCar(List<TCar> l) {
		List<Car> nl = new ArrayList<Car>();
		for (TCar t : l) {
			Car r = new Car();
			BeanUtils.copyProperties(t, r);
			nl.add(r);
		}
		return nl;
	}

	@Autowired
	public void setCarDao(BaseDaoI<TCar> carDao) {
		this.carDao = carDao;
	}
}
