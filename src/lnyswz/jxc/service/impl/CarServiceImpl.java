package lnyswz.jxc.service.impl;

import freemarker.ext.beans.HashAdapter;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.jxc.bean.Car;
import lnyswz.jxc.model.TCar;
import lnyswz.jxc.model.TThCar;
import lnyswz.jxc.service.CarServiceI;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("carService")
public class CarServiceImpl implements CarServiceI {
	private BaseDaoI<TCar> carDao;
	private BaseDaoI<TThCar> thCarDao;

	@Override
	public void updateCar(Car car) {
		String lshs = car.getLsh();
		String carIds = car.getCarIds();
		for (String lsh : lshs.split(",")) {
			String hql = "from TThCar t where t.lsh = :lsh";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("lsh", lsh);
			TThCar tThCar = thCarDao.get(hql, params);
			if(tThCar != null) {
				thCarDao.delete(tThCar);
			}
			for (String id : carIds.split(",")) {
				TThCar t = new TThCar();
				t.setLsh(lsh);
				t.setCarId(Integer.valueOf(id));
				t.setCreateId(car.getCreateId());
				t.setCreateName(car.getCreateName());
				t.setCreateTime(new Date());
				thCarDao.save(t);
			}
		}
	}

    @Override
    public List<Car> getSelectCar(Car car) {
        String hql = "from TThCar t where t.lsh = :lsh";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lsh", car.getLsh());
        List<TThCar> ts = thCarDao.find(hql, params);
        List<Car> cars = null;
        if(ts != null && ts.size() > 0){
            cars = new ArrayList<Car>();
            Car c = null;
            for (TThCar t : ts) {
                c = new Car();
                c.setId(t.getCarId());
                cars.add(c);
            }
        }
        return cars;
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

	@Autowired
    public void setThCarDao(BaseDaoI<TThCar> thCarDao) {
        this.thCarDao = thCarDao;
    }
}
