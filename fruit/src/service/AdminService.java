package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dao.AdminsDao;
import entity.Admins;
import divide.SafeDivide;

/**
 * ����Ա����
 */
@Service	// ע��Ϊservice��spring����bean
@Transactional	// ע��������з�������spring����, ��������Ĭ��
public class AdminService {

	@Autowired
	private AdminsDao adminDao;
	
	
	/**
	 * ��֤�û�����
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean checkUser(String username, String password){
		return adminDao.getByUsernameAndPassword(username, SafeDivide.encode(password)) != null;
	}
	
	/**
	 * �û����Ƿ����
	 * @param username
	 * @return
	 */
	public boolean isExist(String username) {
		return adminDao.getByUsername(username) != null;
	}

	/**
	 * ͨ���û�����ȡ
	 * @param username
	 * @return
	 */
	public Admins getByUsername(String username) {
		return adminDao.getByUsername(username);
	}
	
	/**
	 * �б�
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Admins> getList(int page, int rows) {
		return adminDao.getList(rows * (page-1), rows);
	}

	/**
	 * ����
	 * @return
	 */
	public long getTotal() {
		return adminDao.getTotal();
	}

	/**
	 * ͨ��id��ѯ
	 * @param id
	 * @return
	 */
	public Admins get(int id) {
		return adminDao.selectById(id);
	}
	
	/**
	 * ���
	 * @param admin
	 */
	public Integer add(Admins admin) {
		admin.setPassword(SafeDivide.encode(admin.getPassword()));
		return adminDao.insert(admin);
	}
	
	/**
	 * ����
	 * @param user
	 */
	public boolean update(Admins admin) {
		return adminDao.updateById(admin) > 0;
	}

	/**
	 * ɾ��
	 * @param user
	 */
	public boolean delete(Admins admin) {
		return adminDao.deleteById(admin.getId()) > 0;
	}

	
}
