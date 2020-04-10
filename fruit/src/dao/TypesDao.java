package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.Types;

public interface TypesDao {
    int deleteById(Integer id);

    int insert(Types record);

    int insertSelective(Types record);

    Types selectById(Integer id);

    int updateByIdSelective(Types record);

    int updateById(Types record);    
    
    // ����Ϊmybatis generator�Զ����ɽӿ�, ����ʵ����mapper.xml��
    
    // ------------------------------------------------------------
    
    // ���·���ʹ��mybatisע��ʵ��
    
	/**
	 * ��ȡ�б�
	 * @return
	 */
    @Select("select * from types order by id desc")
	public List<Types> getList();
    
    /**
	 * ͨ�����ֻ�ȡ�б�
	 * @return
	 */
    @Select("select * from types where name like concat('%', #{name}, '%') order by id desc")
    public List<Types> getListByName(@Param("name")String name);
    
}