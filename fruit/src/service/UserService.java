package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dao.UsersDao;
import entity.Users;
import divide.SafeDivide;

/**
 * �û�����
 */
@Service	// ע��Ϊservice��spring����bean
@Transactional	// ע��������з�������spring����, ��������Ĭ��
public class UserService {

	@Autowired		//springע�������
	private UsersDao userDao;
	
	/**
	 * ��֤�û�����
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean checkUser(String username, String password){
		return userDao.getByUsernameAndPassword(username, SafeDivide.encode(password)) != null;
	}

	/**
	 * �û��Ƿ����
	 * @param username
	 * @return
	 */
	public boolean isExist(String username) {
		return userDao.getByUsername(username) != null;
	}

	/**
	 * ���
	 * @param user
	 * @return
	 */
	public boolean add(Users user) {
		user.setPassword(SafeDivide.encode(user.getPassword()));
		return userDao.insert(user) > 0;
	}
	
	/**
	 * ͨ��id��ȡ
	 * @param userid
	 * @return
	 */
	public Users get(int userid){
		return userDao.selectById(userid);
	}
	
	/**
	 * ͨ��username��ȡ
	 * @param username
	 * @return
	 */
	public Users get(String username){
		return userDao.getByUsername(username);
	}
	
	/**
	 * �б�
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Users> getList(int page, int rows) {
		return userDao.getList(rows * (page-1), rows);
	}
	
	/**
	 * ͨ�����������б�
	 * @return
	 */
	public List<Users> getListByName(String name) {
		return userDao.getListByName(name);
	}

	/**
	 * ����
	 * @return
	 */
	public long getTotal() {
		return userDao.getTotal();
	}

	/**
	 * ����
	 * @param user
	 */
	public boolean update(Users user) {
		return userDao.updateById(user) > 0;
	}

	/**
	 * ɾ��
	 * @param id
	 */
	public boolean delete(Users user) {
		return userDao.deleteById(user.getId()) > 0;
	}
	
}
