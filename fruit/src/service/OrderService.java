package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dao.ItemsDao;
import dao.OrdersDao;
import entity.Fruits;
import entity.Items;
import entity.Orders;

/**
 * ��Ʒ��������
 */
@Service	// ע��Ϊservice��spring����bean
@Transactional	// ע��������з�������spring����, ��������Ĭ��
public class OrderService {

	@Autowired
	private OrdersDao orderDao;
	@Autowired
	private ItemsDao itemDao;
	@Autowired
	private FruitService FruitService;
	@Autowired
	private UserService userService;
	
	
	/**
	 * ��������
	 * @param good
	 * @return
	 */
	public Orders add(Fruits good) {
		List<Items> itemList = new ArrayList<Items>();
		itemList.add(addItem(good));
		Orders order = new Orders();
		order.setItemList(itemList);
		order.setTotal(good.getPrice());
		order.setAmount(1);
		return order;
	}

	/**
	 * �򶩵������Ŀ
	 * @param order
	 * @param good
	 * @return
	 */
	public Orders addOrderItem(Orders order, Fruits good) {
		List<Items> itemList = order.getItemList();
		itemList = itemList==null ? new ArrayList<Items>() : itemList;
		// ������ﳵ���д���Ŀ, ����+1
		boolean notThis = true;
		for (Items item : itemList) {
			if (item.getGoodId() == good.getId()) {
				item.setAmount(item.getAmount() + 1);
				item.setTotal(good.getPrice() * item.getAmount());
				notThis = false;
			}
		}
		// �����ǰ���ﳵû�д���Ŀ, ��������Ŀ
		if (notThis) {
			itemList.add(addItem(good));
		}
		order.setTotal(order.getTotal() + good.getPrice());
		order.setAmount(order.getAmount() + 1);
		return order;
	}
	
	/**
	 * �Ӷ����м�����Ŀ
	 * @param order
	 * @param good
	 * @return
	 */
	public Orders lessenIndentItem(Orders order, Fruits good) {
		List<Items> itemList = order.getItemList();
		itemList = itemList==null ? new ArrayList<Items>() : itemList;
		// ������ﳵ���д���Ŀ, ����-1
		boolean noneThis = true;
		for (Items item : itemList) {
			if (item.getGoodId() == good.getId()) {
				if (item.getAmount() - 1 <= 0) { // ���ٵ�0��ɾ��
					return deleteIndentItem(order, good);
				}
				item.setAmount(item.getAmount() - 1);
				item.setTotal(good.getPrice() * item.getAmount());
				noneThis = false;
			}
		}
		// �����ǰ���ﳵû����Ŀ, ֱ�ӷ���
		if (noneThis) {
			return order;
		}
		order.setTotal(order.getTotal() - good.getPrice());
		order.setAmount(order.getAmount() - 1);
		return order;
	}
	
	/**
	 * �Ӷ�����ɾ����Ŀ
	 * @param order
	 * @param good
	 * @return
	 */
	public Orders deleteIndentItem(Orders order, Fruits good) {
		List<Items> itemList = order.getItemList();
		itemList = itemList==null ? new ArrayList<Items>() : itemList;
		// ������ﳵ���д���Ŀ, ��������
		boolean noneThis = true;
		int itemAmount = 0;
		List<Items> resultList = new ArrayList<Items>();
		for (Items item : itemList) {
			if (item.getGoodId() == good.getId()) {
				itemAmount = item.getAmount();
				noneThis = false;
				continue;
			}
			resultList.add(item);
		}
		// ����Ѿ�û����Ŀ, ����null
		if (resultList.isEmpty()) {
			return null;
		}
		order.setItemList(resultList);
		// �����ǰ���ﳵû����Ŀ, ֱ�ӷ���
		if (noneThis) {
			return order;
		}
		order.setTotal(order.getTotal() - good.getPrice() * itemAmount);
		order.setAmount(order.getAmount() - itemAmount);
		return order;
	}

	/**
	 * ���涩��
	 * @param order
	 */
	public int save(Orders order) {
		order.setStatus(Orders.STATUS_UNPAY);
		order.setSystime(new Date());
		orderDao.insert(order);
		int orderid = order.getId();
		for(Items item : order.getItemList()){
			item.setOrderId(orderid);
			itemDao.insert(item);
		}
		return orderid;
	}
	
	/**
	 * ����֧��
	 * @param order
	 */
	public void pay(Orders order) {
		Orders old = orderDao.selectById(order.getId());
		// ΢�Ż�֧����֧��ʱ, ģ��֧�����
		int paytype = order.getPaytype();
		if(paytype == Orders.PAYTYPE_WECHAT || paytype == Orders.PAYTYPE_ALIPAY) {
			old.setStatus(Orders.STATUS_PAYED);
		}else {
			old.setStatus(Orders.STATUS_SEND);
		}
		old.setPaytype(order.getPaytype());
		old.setName(order.getName());
		old.setPhone(order.getPhone());
		old.setAddress(order.getAddress());
		orderDao.updateById(old);
	}
	
	/**
	 * ��ȡ�����б�
	 * @param page
	 * @param row
	 * @return
	 */
	public List<Orders> getList(byte status, int page, int row) {
		List<Orders> orderList = status>0 ? orderDao.getListByStatus(status, row * (page-1), row) 
				: orderDao.getList(row * (page-1), row);
		for(Orders order : orderList) {
			order.setItemList(this.getItemList(order.getId()));
			order.setUser(userService.get(order.getUserId()));
		}
		return orderList;
	}
	
	/**
	 * ��ȡ�����б�
	 * @param page
	 * @param row
	 * @return
	 */
	public List<Orders> getListById(int id) {
		Orders order = this.get(id);
		if(Objects.nonNull(order)) {
			order.setItemList(this.getItemList(order.getId()));
			return Arrays.asList(order);
		}
		return null;
	}
	
	/**
	 * ��ȡ����
	 * @return
	 */
	public int getTotal(byte status) {
		return (int)orderDao.getTotalByStatus(status);
	}

	/**
	 * ��������
	 * @param id
	 * @return 
	 */
	public boolean dispose(int id) {
		Orders order = orderDao.selectById(id);
		order.setStatus(Orders.STATUS_SEND);
		return orderDao.updateByIdSelective(order) > 0;
	}
	
	/**
	 * �������
	 * @param id
	 * @return 
	 */
	public boolean finish(int id) {
		Orders order = orderDao.selectById(id);
		order.setStatus(Orders.STATUS_FINISH);
		return orderDao.updateByIdSelective(order) > 0;
	}

	/**
	 * ɾ������
	 * @param id
	 */
	public boolean delete(int id) {
		return orderDao.deleteById(id) > 0;
	}
	
	/**
	 * ��ȡĳ�û�ȫ������
	 * @param userid
	 */
	public List<Orders> getListByUserid(int userid) {
		return orderDao.getListByUserid(userid);
	}

	/**
	 * ͨ��id��ȡ
	 * @param orderid
	 * @return
	 */
	public Orders get(int orderid) {
		return orderDao.selectById(orderid);
	}
	
	
	/**
	 * ����������
	 * @param good
	 * @return
	 */
	private Items addItem(Fruits good) {
		Items item = new Items();
		item.setGoodId(good.getId());
		item.setAmount(1);
		item.setPrice(good.getPrice());
		item.setTotal(good.getPrice());
		item.setGood(FruitService.get(item.getGoodId()));
		return item;
	}
	
	/**
	 * ��ȡ������Ŀ�б�
	 * @param orderid
	 * @return
	 */
	public List<Items> getItemList(int orderid){
		List<Items> itemList = itemDao.getItemList(orderid);
		for(Items item : itemList) {
			item.setGood(FruitService.get(item.getGoodId()));
		}
		return itemList;
	}
	public List<Orders>findAll1(){
		return orderDao.findAll();
	}
}
