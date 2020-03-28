package lnyswz.jxc.service;

import lnyswz.common.bean.Json;
import lnyswz.jxc.bean.Car;

import java.util.List;

public interface CarServiceI {
	public void updateCar(Car car);
	public List<Car> listCar(Car car);
	public List<Car> getSelectCar(Car car);
}
