package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.Admins;

public interface AdminsDao {
    int deleteById(Integer id);

    int insert(Admins record);

    int insertSelective(Admins record);

    Admins selectById(Integer id);

    int updateByIdSelective(Admins record);

    int updateById(Admins record);
    
    
    
    // ����Ϊmybatis generator�Զ����ɽӿ�, ����ʵ����mapper.xml��
    
    // ------------------------------------------------------------
    
    // ���·���ʹ��mybatisע��ʵ��
    
    
	/**
	 * ͨ���û�������
	 * @param username
	 * @return
	 */
    @Select("select * from admins where username=#{username}")
	public Admins getByUsername(String username);
	
	/**
	 * ͨ���û������������
	 * @param username
	 * @param password
	 * @return �޼�¼����null
	 */
    @Select("select * from admins where username=#{username} and password=#{password}")
	public Admins getByUsernameAndPassword(@Param("username")String username, @Param("password")String password);

	/**
	 * ��ȡ�б�
	 * @param page
	 * @param rows
	 * @return �޼�¼���ؿռ���
	 */
    @Select("select * from admins order by id desc limit #{begin}, #{size}")
	public List<Admins> getList(@Param("begin")int begin, @Param("size")int size);

	/**
	 * ����
	 * @return
	 */
    @Select("select count(*) from admins")
	public long getTotal();
}