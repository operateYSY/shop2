package controller;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import entity.Fruits;
import entity.Items;
import entity.Orders;
import entity.Users;
import service.FruitService;
import service.OrderService;
import service.TypeService;
import service.UserService;
import divide.SafeDivide;

/**
 * �û���ؽӿ�
 */
@Controller
@RequestMapping("/index")
public class UserController{
	
	private static final String INDENT_KEY = "order";
	
	@Resource
	private UserService userService;
	@Resource
	private OrderService orderService;
	@Resource
	private FruitService goodService;
	@Resource
	private TypeService typeService;
private int num;
private Users users;
	
	/**
	 * ע���û�
	 * @return
	 */
	@RequestMapping("/register")
	public String register(@RequestParam(required=false, defaultValue="0")int flag, Users user, Model model){
		model.addAttribute("typeList", typeService.getList());
		if(flag==-1) {
			model.addAttribute("flag", 5); // ע��ҳ��
			return "/index/register.jsp";
		}
		if (user.getUsername().isEmpty()) {
			model.addAttribute("msg", "�û�������Ϊ��!");
			return "/index/register.jsp";
		}else if (userService.isExist(user.getUsername())) {
			model.addAttribute("msg", "�û����Ѵ���!");
			return "/index/register.jsp";
		}else {
			String password = user.getPassword();
			userService.add(user);
			user.setPassword(password);
			return "redirect:login?flag=-1"; // ע��ɹ���תȥ��¼
		}
	}
	
	/**
	 * �û���¼
	 * @return
	 */
	@RequestMapping("/login")
	public String login(@RequestParam(required=false, defaultValue="0")int flag, Users user, HttpSession session, Model model) {
		model.addAttribute("typeList", typeService.getList());
		if(flag==-1) {
			flag = 6; // ��¼ҳ��
			return "/index/login.jsp";
		}
		if(userService.checkUser(user.getUsername(), user.getPassword())){
			this.users=userService.get(user.getUsername());
			session.setAttribute("user",users);
			return "redirect:index";
		} else {
			model.addAttribute("msg", "�û������������!");
			return "/index/login.jsp";
		}
	}

	/**
	 * ע����¼
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		session.removeAttribute("order");
		return "/index/login.jsp";
	}
	
	/**
	 * �鿴���ﳵ
	 * @return
	 */
	@RequestMapping("/cart")
	public String cart(Model model) {
		model.addAttribute("typeList", typeService.getList());
		return "/index/cart.jsp";
	}
	
	/**
	 * ����
	 * @return
	 */
	@RequestMapping("/buy")
	public @ResponseBody String buy(int goodid, HttpSession session){
		Fruits goods = goodService.get(goodid);
		if (goods.getStock() <= 0) { // ��治��
			return "empty";
		}
		Orders order = (Orders) session.getAttribute(INDENT_KEY);
		if (order==null) {
			session.setAttribute(INDENT_KEY, orderService.add(goods));
		}else {
			session.setAttribute(INDENT_KEY, orderService.addOrderItem(order, goods));
		}
		return "ok";
	}
	
	/**
	 * ����
	 */
	@RequestMapping("/lessen")
	public @ResponseBody String lessen(int goodid, HttpSession session){
		Orders order = (Orders) session.getAttribute(INDENT_KEY);
		if (order != null) {
			session.setAttribute(INDENT_KEY, orderService.lessenIndentItem(order, goodService.get(goodid)));
		}
		return "ok";
	}
	
	/**
	 * ɾ��
	 */
	@RequestMapping("/delete")
	public @ResponseBody String delete(int goodid, HttpSession session){
		Orders order = (Orders) session.getAttribute(INDENT_KEY);
		if (order != null) {
			session.setAttribute(INDENT_KEY, orderService.deleteIndentItem(order, goodService.get(goodid)));
		}
		return "ok";
	}
	
	
	/**
	 * �ύ����
	 * @return
	 */
	@RequestMapping("/save")
	public String save(ServletRequest request, HttpSession session, Model model){
		model.addAttribute("typeList", typeService.getList());
		Users user = (Users) session.getAttribute("user");
		if (user == null) {
			request.setAttribute("msg", "���¼���ύ����!");
			return "/index/login.jsp";
		}
		Orders sessionOrder = (Orders) session.getAttribute(INDENT_KEY);
		if (sessionOrder != null) {
			if (sessionOrder != null) {
				for(Items item : sessionOrder.getItemList()){ // �����Ʒ���(��ֹ��治��)
					Fruits good = goodService.get(item.getGoodId());
					if(item.getAmount() > good.getStock()){
						request.setAttribute("msg", "��Ʒ ["+good.getName()+"] ��治��! ��ǰ�������: "+good.getStock());
						return "/index/cart.jsp";
					}
				}
				// ����Ʒ���
				for(Items item : sessionOrder.getItemList()){ // �����Ʒ���(��ֹ��治��)
					Fruits good = goodService.get(item.getGoodId());
					good.setStock(good.getStock() - item.getAmount());
					goodService.update(good);
				}
			}
			sessionOrder.setUserId(user.getId());
			sessionOrder.setUser(userService.get(user.getId()));
			int orderid = orderService.save(sessionOrder);	// ���涩��
			session.removeAttribute(INDENT_KEY);	// ������ﳵ
			return "redirect:topay?orderid="+orderid;
		}
		request.setAttribute("msg", "����ʧ��!");
		return "/index/cart.jsp";
	}
	
	/**
	 * ֧��ҳ��
	 * @return
	 */
	@RequestMapping("/topay")
	public String topay(int orderid, ServletRequest request, Model model) {
		model.addAttribute("typeList", typeService.getList());
		request.setAttribute("order", orderService.get(orderid));
		this.num=orderid;
		return "/index/pay.jsp";
	}
	
	/**
	 * ֧��(ģ��)
	 * @return
	 */
	@RequestMapping("/pay")
	public String pay(Orders order,Model model) {
		model.addAttribute("typeList", typeService.getList());
		 
	
		orderService.pay(order);
		return "redirect:payok?orderid="+order.getId();
	}
	@RequestMapping("/pay2")
	public String pay(Model model) {
		model.addAttribute("typeList", typeService.getList());
		Orders order = orderService.get(this.num);
		if(order.getPaytype()==(byte)0) {
			order.setPaytype((byte)2);
		}
		order.setName(users.getName());
		order.setAddress(users.getAddress());
		order.setPhone(users.getPhone());
		orderService.pay(order);
		return "redirect:payok?orderid="+order.getId();
	}
	
	/**
	 * ֧���ɹ�
	 * @return
	 */
	@RequestMapping("/payok")
	public String payok(int orderid,ServletRequest request, Model model) {
		model.addAttribute("typeList", typeService.getList());
		Orders order = orderService.get(orderid);
		
		int paytype = order.getPaytype();
		if(paytype == Orders.PAYTYPE_WECHAT || paytype == Orders.PAYTYPE_ALIPAY) {
			request.setAttribute("msg", "����["+this.num+"]֧���ɹ�");
		}else {
			request.setAttribute("msg", "����["+this.num+"]��������");
		}
		return "/index/payok.jsp";
	}
	
	/**
	 * �鿴����
	 * @return
	 */
	@RequestMapping("/order")
	public String order(HttpSession session, Model model){
		model.addAttribute("flag", 3);
		model.addAttribute("typeList", typeService.getList());
		Users user = (Users) session.getAttribute("user");
		if (user == null) {
			model.addAttribute("msg", "���¼��鿴����!");
			return "/index/login.jsp";
		}
		List<Orders> orderList = orderService.getListByUserid(user.getId());
		if (orderList!=null && !orderList.isEmpty()) {
			for(Orders order : orderList){
				order.setItemList(orderService.getItemList(order.getId()));
			}
		}
		model.addAttribute("orderList", orderList);
		return "/index/order.jsp";
	}
	
	
	/**
	 * ������Ϣ
	 * @return
	 */
	@RequestMapping("/my")
	public String my(Users user, HttpSession session, Model model){
		model.addAttribute("flag", 4);
		model.addAttribute("typeList", typeService.getList());
		Users userLogin = (Users) session.getAttribute("user");
		if (userLogin == null) {
			model.addAttribute("msg", "���ȵ�¼!");
			return "/index/login.jsp";
		}
		// �����������
		if (Objects.isNull(user) || Objects.isNull(user.getId())) {
			return "/index/my.jsp";
		}
		Users u = userService.get(user.getId());
		// �޸�����
		u.setName(user.getName());
		u.setPhone(user.getPhone());
		u.setAddress(user.getAddress());
		userService.update(u);  // �������ݿ�
		session.setAttribute("user", u); // ����session
		model.addAttribute("msg", "��Ϣ�޸ĳɹ�!");
		// �޸�����
		if(user.getPasswordNew()!=null && !user.getPasswordNew().trim().isEmpty()) {
			if (user.getPassword()!=null && !user.getPassword().trim().isEmpty() 
					&& SafeDivide.encode(user.getPassword()).equals(u.getPassword())) {
				if (user.getPasswordNew()!=null && !user.getPasswordNew().trim().isEmpty()) {
					u.setPassword(SafeDivide.encode(user.getPasswordNew()));
				}
				userService.update(u);  // �������ݿ�
				session.setAttribute("user", u); // ����session
				model.addAttribute("msg", "�����޸ĳɹ�!");
			}else {
				model.addAttribute("msg", "ԭ�������!");
			}
		}
		return "/index/my.jsp";
	}
	
	  @RequestMapping(value="/action") public String pay(ServletRequest request, Model model) { model.addAttribute("typeList",
	  typeService.getList()); 
	  request.setAttribute("order", orderService.get(num));
	  
	  return "/index/pay/index.jsp";
	  
	  
	 
	  }
	 
	
}