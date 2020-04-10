package service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dao.TopsDao;
import entity.Tops;

/**
 * ��Ʒ�Ƽ�����
 */
@Service	// ע��Ϊservice��spring����bean
@Transactional	// ע��������з�������spring����, ��������Ĭ��
public class TopService {

	@Autowired	
	private TopsDao topDao;
	@Autowired
	private FruitService goodService;
	
	
	/**
	 * ��ȡ�б�
	 * @return
	 */
	public List<Tops> getList(byte type, int page, int size){
		List<Tops> topList = topDao.getList(type, (page-1)*size, size);
		for(Tops top : topList) {
			top.setGood(goodService.get(top.getGoodId()));
		}
		return topList;
	}
	
	/**
	 * ��ȡ����
	 * @param type
	 * @return
	 */
	public long getTotal(byte type) {
		return topDao.getTotal(type);
	}
	
	/**
	 * ��ȡ�б�
	 * @return
	 */
	public List<Tops> getListByGoodid(int goodid){
		return topDao.getListByGoodid(goodid);
	}

	/**
	 * ͨ��id��ѯ
	 * @param id
	 * @return
	 */
	public Tops get(int id) {
		return topDao.selectById(id);
	}
	
	/**
	 * ���
	 * @param top
	 * @return
	 */
	public Integer add(Tops top) {
		return topDao.insert(top);
	}

	/**
	 * ����
	 * @param top
	 */
	public boolean update(Tops top) {
		return topDao.updateById(top) > 0;
	}

	/**
	 * ɾ��
	 * @param top
	 */
	public boolean delete(Tops top) {
		return (Objects.nonNull(top.getId())) ? (topDao.deleteById(top.getId()) > 0) : 
			topDao.deleteByGoodidAndType(top.getGoodId(), top.getType());
	}
	
	/**
	 * ����Ʒɾ��
	 * @param goodid
	 * @return
	 */
	public boolean deleteByGoodid(int goodid) {
		return topDao.deleteByGoodid(goodid);
	}
	
}
