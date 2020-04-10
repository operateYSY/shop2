package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.Fruits;

public interface FruitsDao {
    int deleteById(Integer id);

    int insert(Fruits record);

    int insertSelective(Fruits record);

    Fruits selectById(Integer id);

    int updateByIdSelective(Fruits record);

    int updateById(Fruits record);    
    
    // ����Ϊmybatis generator�Զ����ɽӿ�, ����ʵ����mapper.xml��
    
    // ------------------------------------------------------------
    
    // ���·���ʹ��mybatisע��ʵ��
    
    
	/**
	 * ��ȡ�б�
	 * @param page
	 * @param size
	 * @return
	 */
    @Select("select * from goods order by id desc limit #{begin}, #{size}")
	public List<Fruits> getList(@Param("begin")int begin, @Param("size")int size);
	
	/**
	 * ��ȡ����
	 * @return
	 */
    @Select("select count(*) from goods")
	public long getTotal();
	
	/**
	 * ͨ�����ͻ�ȡ�б�
	 * @param typeid
	 * @param page
	 * @param size
	 * @return
	 */
    @Select("select * from goods where type_id=#{typeid} order by id desc limit #{begin}, #{size}")
	public List<Fruits> getListByType(@Param("typeid")int typeid, @Param("begin")int begin, @Param("size")int size);
	
	/**
	 * ͨ�����ͻ�ȡ����
	 * @param typeid
	 * @return
	 */
    @Select("select count(*) from goods where type_id=#{typeid}")
	public long getTotalByType(@Param("typeid")int typeid);
	
	/**
	 * ͨ�����ƻ�ȡ�б�
	 * @param name
	 * @param page
	 * @param size
	 * @return
	 */
    @Select("select * from goods where name like concat('%',#{name},'%') order by id desc limit #{begin}, #{size}")
	public List<Fruits> getListByName(@Param("name")String name, @Param("begin")int begin, @Param("size")int size);
	
	/**
	 * ͨ�����ƻ�ȡ����
	 * @param name
	 * @return
	 */
    @Select("select count(*) from goods where name like concat('%',#{name},'%')")
	public long getTotalByName(@Param("name")String name);
}