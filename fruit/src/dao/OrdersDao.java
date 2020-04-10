package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.Orders;

public interface OrdersDao {
    int deleteById(Integer id);

    int insert(Orders record);

    int insertSelective(Orders record);

    Orders selectById(Integer id);

    int updateByIdSelective(Orders record);

    int updateById(Orders record);
  
    
    // ����Ϊmybatis generator�Զ����ɽӿ�, ����ʵ����mapper.xml��
    
    // ------------------------------------------------------------
    
    // ���·���ʹ��mybatisע��ʵ��
    
	/**
	 * ��ȡ�б�
	 * @param status
	 * @param page
	 * @param row
	 */
    @Select("select * from orders order by id desc limit #{begin}, #{size}")
	public List<Orders> getList(@Param("begin")int begin, @Param("size")int size);

	/**
	 * ��ȡ����
	 * @param status
	 * @return
	 */
    @Select("select count(*) from orders")
	public long getTotal();
    @Select("select *from orders")
    public List<Orders> findAll();
    /**
     * ��ȡ�б�
     * @param status
     * @param page
     * @param row
     */
    @Select("select * from orders where status=#{status} order by id desc limit #{begin}, #{size}")
    public List<Orders> getListByStatus(@Param("status")byte status, @Param("begin")int begin, @Param("size")int size);
    
    /**
     * ��ȡ����
     * @param status
     * @return
     */
    @Select("select count(*) from orders where status=#{status}")
    public long getTotalByStatus(@Param("status")byte status);

	/**
	 * ͨ���û���ȡ�б�
	 * @param userid
	 */
    @Select("select * from orders where user_id=#{userid} order by id desc")
	public List<Orders> getListByUserid(@Param("userid")int userid);

}