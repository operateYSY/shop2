package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dao.FruitsDao;
import entity.Fruits;
import entity.Tops;

/**
 * 商品服务
 */
@Service	// 注解为service层spring管理bean
@Transactional	// 注解此类所有方法加入spring事务, 具体设置默认
public class FruitService {

	@Autowired	
	private FruitsDao goodDao;
	@Autowired
	private TopService topService;
	@Autowired
	private TypeService typeService;
	
	
	/**
	 * 获取列表
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Fruits> getList(int status, int page, int size){
		if (status == 0) {
			return packTopList(goodDao.getList(size * (page-1), size));
		}
		List<Tops> topList = topService.getList((byte)status, page, size);
		if(topList!=null && !topList.isEmpty()) {
			List<Fruits> goodList = new ArrayList<>();
			for(Tops top : topList) {
				goodList.add(packTop(goodDao.selectById(top.getGoodId())));
			}
			return goodList;
		}
		return null;
	}

	/**
	 * 获取产品总数
	 * @return
	 */
	public long getTotal(int status){
		if (status == 0) {
			return goodDao.getTotal();
		}
		return topService.getTotal((byte)status);
	}
	
	/**
	 * 通过名称获取产品列表
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Fruits> getListByName(String name, int page, int size){
		return goodDao.getListByName(name, size * (page-1), size);
	}
	
	/**
	 * 通过名称获取产品总数
	 * @return
	 */
	public long getTotalByName(String name){
		return goodDao.getTotalByName(name);
	}

	/**
	 * 通过分类搜索
	 * @param typeid
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Fruits> getListByType(int typeid, int page, int size) {
		return typeid > 0 ? goodDao.getListByType(typeid, size * (page-1), size) : goodDao.getList(size * (page-1), size);
	}
	
	/**
	 * 获取数量
	 * @param typeid
	 * @return
	 */
	public long getTotalByType(int typeid){
		return typeid > 0 ? goodDao.getTotalByType(typeid) : goodDao.getTotal();
	}
	
	/**
	 * 通过id获取
	 * @param productid
	 * @return
	 */
	public Fruits get(int id) {
		Fruits Fruits = goodDao.selectById(id);
		if (Objects.nonNull(Fruits)) {
			Fruits.setType(typeService.get(Fruits.getTypeId()));
		}
		return Fruits;
	}
	
	/**
	 * 添加
	 * @param product
	 */
	public Integer add(Fruits good) {
		return goodDao.insert(good);
	}

	/**
	 * 修改
	 * @param product
	 * @return 
	 */
	public boolean update(Fruits good) {
		return goodDao.updateById(good) > 0;
	}
	
	/**
	 * 删除商品
	 * 先删除此商品的推荐信息
	 * @param product
	 */
	public boolean delete(int goodid) {
		topService.deleteByGoodid(goodid);
		return goodDao.deleteById(goodid) > 0;
	}
	

	/**
	 * 封装商品推荐信息
	 * @param list
	 * @return
	 */
	private List<Fruits> packTopList(List<Fruits> list) {
		for(Fruits good : list) {
			good.setType(typeService.get(good.getTypeId()));
			good = packTop(good);
		}
		return list;
	}

	/**
	 * 封装商品推荐信息
	 * @param good
	 * @return
	 */
	private Fruits packTop(Fruits good) {
		if(good != null) {
			List<Tops> topList = topService.getListByGoodid(good.getId());
			if (Objects.nonNull(topList) && !topList.isEmpty()) {
				for(Tops top : topList) {
					if(top.getType()==Tops.TYPE_SCROLL) {
						good.setTopScroll(true);
					}else if (top.getType()==Tops.TYPE_LARGE) {
						good.setTopLarge(true);
					}else if (top.getType()==Tops.TYPE_SMALL) {
						good.setTopSmall(true);
					}
				}
			}
		}
		return good;
	}

}