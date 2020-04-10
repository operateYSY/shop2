package dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import entity.Items;

public interface ItemsDao {
    int deleteById(Integer id);

    int insert(Items record);

    int insertSelective(Items record);

    Items selectById(Integer id);

    int updateByIdSelective(Items record);

    int updateById(Items record);    
    
    // ����Ϊmybatis generator�Զ����ɽӿ�, ����ʵ����mapper.xml��
    
    // ------------------------------------------------------------
    
    // ���·���ʹ��mybatisע��ʵ��
    
	
	/**
	 * �������б�
	 * @param Ordersid
	 * @param page
	 * @param rows
	 * @return
	 */
    @Select("select * from items where order_id=#{orderid}")
	public List<Items> getItemList(int orderid);
}