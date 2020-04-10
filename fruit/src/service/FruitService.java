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
 * ��Ʒ����
 */
@Service	// ע��Ϊservice��spring����bean
@Transactional	// ע��������з�������spring����, ��������Ĭ��
public class FruitService {

	@Autowired	
	private FruitsDao goodDao;
	@Autowired
	private TopService topService;
	@Autowired
	private TypeService typeService;
	
	
	/**
	 * ��ȡ�б�
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
	 * ��ȡ��Ʒ����
	 * @return
	 */
	public long getTotal(int status){
		if (status == 0) {
			return goodDao.getTotal();
		}
		return topService.getTotal((byte)status);
	}
	
	/**
	 * ͨ�����ƻ�ȡ��Ʒ�б�
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Fruits> getListByName(String name, int page, int size){
		return goodDao.getListByName(name, size * (page-1), size);
	}
	
	/**
	 * ͨ�����ƻ�ȡ��Ʒ����
	 * @return
	 */
	public long getTotalByName(String name){
		return goodDao.getTotalByName(name);
	}

	/**
	 * ͨ����������
	 * @param typeid
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Fruits> getListByType(int typeid, int page, int size) {
		return typeid > 0 ? goodDao.getListByType(typeid, size * (page-1), size) : goodDao.getList(size * (page-1), size);
	}
	
	/**
	 * ��ȡ����
	 * @param typeid
	 * @return
	 */
	public long getTotalByType(int typeid){
		return typeid > 0 ? goodDao.getTotalByType(typeid) : goodDao.getTotal();
	}
	
	/**
	 * ͨ��id��ȡ
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
	 * ���
	 * @param product
	 */
	public Integer add(Fruits good) {
		return goodDao.insert(good);
	}

	/**
	 * �޸�
	 * @param product
	 * @return 
	 */
	public boolean update(Fruits good) {
		return goodDao.updateById(good) > 0;
	}
	
	/**
	 * ɾ����Ʒ
	 * ��ɾ������Ʒ���Ƽ���Ϣ
	 * @param product
	 */
	public boolean delete(int goodid) {
		topService.deleteByGoodid(goodid);
		return goodDao.deleteById(goodid) > 0;
	}
	

	/**
	 * ��װ��Ʒ�Ƽ���Ϣ
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
	 * ��װ��Ʒ�Ƽ���Ϣ
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