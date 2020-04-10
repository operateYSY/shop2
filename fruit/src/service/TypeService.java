package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dao.TypesDao;
import entity.Types;

/**
 * ���ͷ���
 */
@Service	// ע��Ϊservice��spring����bean
@Transactional	// ע��������з�������spring����, ��������Ĭ��
public class TypeService {

	@Autowired	
	private TypesDao typeDao;
	
	
	/**
	 * ��ȡ�б�
	 * @return
	 */
	public List<Types> getList(){
		return typeDao.getList();
	}
	
	/**
	 * ͨ�����ֻ�ȡ�б�
	 * @return
	 */
	public List<Types> getList(String name){
		return typeDao.getListByName(name);
	}

	/**
	 * ͨ��id��ѯ
	 * @param id
	 * @return
	 */
	public Types get(int id) {
		return typeDao.selectById(id);
	}
	
	/**
	 * ���
	 * @param type
	 * @return
	 */
	public Integer add(Types type) {
		return typeDao.insert(type);
	}

	/**
	 * ����
	 * @param type
	 */
	public boolean update(Types type) {
		return typeDao.updateById(type) > 0;
	}

	/**
	 * ɾ��
	 * @param type
	 */
	public boolean delete(Types type) {
		return typeDao.deleteById(type.getId()) > 0;
	}
	
}
