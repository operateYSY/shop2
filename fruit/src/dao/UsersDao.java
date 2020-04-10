package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.Users;

public interface UsersDao {
    int deleteById(Integer id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectById(Integer id);

    int updateByIdSelective(Users record);

    int updateById(Users record);    
    
    // ����Ϊmybatis generator�Զ����ɽӿ�, ����ʵ����mapper.xml��
    
    // ------------------------------------------------------------
    
    // ���·���ʹ��mybatisע��ʵ��
    
	/**
	 * ͨ���û��������û�
	 * @return �޼�¼����null
	 */
    @Select("select * from users where username=#{username}")
	public Users getByUsername(String username);
	
	/**
	 * ͨ���û������������
	 * @param username
	 * @param password
	 * @return �޼�¼����null
	 */
    @Select("select * from users where username=#{username} and password=#{password}")
	public Users getByUsernameAndPassword(@Param("username")String username, @Param("password")String password);
	
	/**
	 * ��ȡ�б�
	 * @param page
	 * @param rows
	 * @return �޼�¼���ؿռ���
	 */
    @Select("select * from users order by id desc limit #{begin}, #{size}")
	public List<Users> getList(@Param("begin")int begin, @Param("size")int size);
    
    /**
     * ͨ�����������б�
     * @return �޼�¼���ؿռ���
     */
    @Select("select * from users where username like concat('%',#{name},'%') order by id desc ")
    public List<Users> getListByName(@Param("name")String name);

	/**
	 * ����
	 * @return
	 */
    @Select("select count(*) from users")
	public long getTotal();
	
    
}