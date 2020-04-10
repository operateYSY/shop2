package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import entity.Admins;
import entity.Fruits;
import entity.Orders;
import entity.Tops;
import entity.Types;
import entity.Users;
import service.AdminService;
import service.FruitService;
import service.OrderService;
import service.TopService;
import service.TypeService;
import service.UserService;
import divide.PageDivide;
import divide.SafeDivide;
import divide.UploadingFile;

/**
 * ��̨��ؽӿ�
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final int rows = 10;

	@Autowired
	private AdminService adminService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private FruitService goodService;
	@Autowired
	private TopService topService;
	@Autowired
	private TypeService typeService;

	/**
	 * ����Ա��¼
	 * @return
	 */
	@RequestMapping("/login")
	public String login(Admins admin, HttpServletRequest request, HttpSession session) {
		if (adminService.checkUser(admin.getUsername(), admin.getPassword())) {
			session.setAttribute("username", admin.getUsername());
			return "redirect:index";
		}
		request.setAttribute("msg", "�û������������!");
		return "/admin/login.jsp";
	}

	/**
	 * �˳�
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("admin");
		return "/admin/login.jsp";
	}
	
	/**
	 * ��̨��ҳ
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		request.setAttribute("msg", "��ϲ��! ��¼�ɹ���");
		return "/admin/index.jsp";
	}

	/**
	 * �����б�
	 * 
	 * @return
	 */
	@RequestMapping("/orderList")
	public String orderList(@RequestParam(required=false, defaultValue="0")byte status, HttpServletRequest request,
			@RequestParam(required=false, defaultValue="1") int page) {
		request.setAttribute("flag", 1);
		request.setAttribute("status", status);
		request.setAttribute("orderList", orderService.getList(status, page, rows));
		request.setAttribute("pageTool", PageDivide.getPageTool(request, orderService.getTotal(status), page, rows));
		return "/admin/order-list.jsp";
	}
	
	/**
	 * �����б�
	 * 
	 * @return
	 */
	@RequestMapping("/orderSearch")
	public String orderSearch(@RequestParam(required=false, defaultValue="0")int id, HttpServletRequest request) {
		if(id > 0) {
			request.setAttribute("flag", 1);
			request.setAttribute("orderList", orderService.getListById(id));
			return "/admin/order-list.jsp";
		}else {
			return "redirect:/admin/orderList";
		}
	}

	/**
	 * ��������
	 * 
	 * @return
	 */
	@RequestMapping("/orderDispose")
	public String orderDispose(int id, byte status,
			@RequestParam(required=false, defaultValue="1") int page) {
		orderService.dispose(id);
		return "redirect:orderList?flag=1&status="+status+"&page="+page;
	}
	
	/**
	 * �������
	 * 
	 * @return
	 */
	@RequestMapping("/orderFinish")
	public String orderFinish(int id, byte status,
			@RequestParam(required=false, defaultValue="1") int page) {
		orderService.finish(id);
		return "redirect:orderList?flag=1&status="+status+"&page="+page;
	}

	/**
	 * ����ɾ��
	 * 
	 * @return
	 */
	@RequestMapping("/orderDelete")
	public String orderDelete(int id, byte status,
			@RequestParam(required=false, defaultValue="1") int page) {
		orderService.delete(id);
		return "redirect:orderList?flag=1&status="+status+"&page="+page;
	}

	/**
	 * �˿͹���
	 * 
	 * @return
	 */
	@RequestMapping("/userList")
	public String userList(HttpServletRequest request,
			@RequestParam(required=false, defaultValue="1") int page) {
		request.setAttribute("flag", 2);
		request.setAttribute("userList", userService.getList(page, rows));
		request.setAttribute("pageTool", PageDivide.getPageTool(request, userService.getTotal(), page, rows));
		return "/admin/customer-list.jsp";
	}
	
	/**
	 * ͨ�����������б�
	 * 
	 * @return
	 */
	@RequestMapping("/userSearch")
	public String userSearch(String name, HttpServletRequest request) {
		request.setAttribute("flag", 2);
		request.setAttribute("userList", userService.getListByName(name));
		return "/admin/customer-list.jsp";
	}

	/**
	 * �˿����
	 * 
	 * @return
	 */
	@RequestMapping("/userAdd")
	public String userAdd(HttpServletRequest request) {
		request.setAttribute("flag", 2);
		return "/admin/customer-add.jsp";
	}

	/**
	 * �˿����
	 * 
	 * @return
	 */
	@RequestMapping("/userSave")
	public String userSave(Users user, HttpServletRequest request, 
			@RequestParam(required=false, defaultValue="1") int page) {
		if (userService.isExist(user.getUsername())) {
			request.setAttribute("msg", "�û����Ѵ���!");
			return "/admin/customer-add.jsp";
		}
		userService.add(user);
		return "redirect:userList?flag=2&page="+page;
	}

	/**
	 * �˿���������ҳ��
	 * 
	 * @return
	 */
	@RequestMapping("/userRe")
	public String userRe(int id, HttpServletRequest request) {
		request.setAttribute("flag", 2);
		request.setAttribute("user", userService.get(id));
		return "/admin/customer-change.jsp";
	}

	/**
	 * �˿���������
	 * 
	 * @return
	 */
	@RequestMapping("/userReset")
	public String userReset(Users user, 
			@RequestParam(required=false, defaultValue="1") int page) {
		String password = SafeDivide.encode(user.getPassword());
		user = userService.get(user.getId());
		user.setPassword(password);
		userService.update(user);
		return "redirect:userList?flag=2&page="+page;
	}

	/**
	 * �˿͸���
	 * 
	 * @return
	 */
	@RequestMapping("/userEdit")
	public String userEdit(int id, HttpServletRequest request) {
		request.setAttribute("flag", 2);
		request.setAttribute("user", userService.get(id));
		return "/admin/admin-edit.jsp";
	}

	/**
	 * �˿͸���
	 * 
	 * @return
	 */
	@RequestMapping("/userUpdate")
	public String userUpdate(Users user, 
			@RequestParam(required=false, defaultValue="1") int page) {
		userService.update(user);
		return "redirect:userList?flag=2&page="+page;
	}

	/**
	 * �˿�ɾ��
	 * 
	 * @return
	 */
	@RequestMapping("/userDelete")
	public String userDelete(Users user, 
			@RequestParam(required=false, defaultValue="1") int page) {
		userService.delete(user);
		return "redirect:userList?flag=2&page="+page;
	}

	/**
	 * ��Ʒ�б�
	 * 
	 * @return
	 */
	@RequestMapping("/goodList")
	public String goodList(@RequestParam(required=false, defaultValue="0")byte status, HttpServletRequest request, 
			@RequestParam(required=false, defaultValue="1") int page) {
		request.setAttribute("flag", 3);
		request.setAttribute("page", page);
		request.setAttribute("status", status);
		request.setAttribute("goodList", goodService.getList(status, page, rows));
		request.setAttribute("pageTool", PageDivide.getPageTool(request, goodService.getTotal(status), page, rows));
		return "/admin/fruit-list.jsp";
	}
	
	/**
	 * ͨ�����ƻ�ȡ�б�
	 * 
	 * @return
	 */
	@RequestMapping("/goodSearch")
	public String goodSearch(String name, HttpServletRequest request, 
			@RequestParam(required=false, defaultValue="1") int page) {
		request.setAttribute("flag", 3);
		request.setAttribute("goodList", goodService.getListByName(name, page, rows));
		request.setAttribute("pageTool", PageDivide.getPageTool(request, goodService.getTotalByName(name), page, rows));
		return "/admin/fruit-list.jsp";
	}

	/**
	 * ��Ʒ���
	 * 
	 * @return
	 */
	@RequestMapping("/goodAdd")
	public String goodAdd(HttpServletRequest request) {
		request.setAttribute("flag", 3);
		request.setAttribute("typeList", typeService.getList());
		return "/admin/fruit-add.jsp";
	}

	/**
	 * ��Ʒ���
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/goodSave")
	public String goodSave(String name, int price, String intro, int stock, int typeId, 
			MultipartFile cover, MultipartFile image1, MultipartFile image2, 
			@RequestParam(required=false, defaultValue="1") int page) throws Exception {
		Fruits good = new Fruits();
		good.setName(name);
		good.setPrice(price);
		good.setIntro(intro);
		good.setStock(stock);
		good.setTypeId(typeId);
		good.setCover(UploadingFile.fileUpload(cover));
		good.setImage1(UploadingFile.fileUpload(image1));
		good.setImage2(UploadingFile.fileUpload(image2));
		goodService.add(good);
		return "redirect:goodList?flag=3&page="+page;
	}

	/**
	 * ��Ʒ����
	 * 
	 * @return
	 */
	@RequestMapping("/goodEdit")
	public String goodEdit(int id, HttpServletRequest request) {
		request.setAttribute("flag", 3);
		request.setAttribute("typeList", typeService.getList());
		request.setAttribute("good", goodService.get(id));
		return "/admin/fruit-edit.jsp";
	}

	/**
	 * ��Ʒ����
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/goodUpdate")
	public String goodUpdate(int id, String name, int price, String intro, int stock, int typeId,  
			MultipartFile cover, MultipartFile image1, MultipartFile image2,
			@RequestParam(required=false, defaultValue="1") int page) throws Exception {
		Fruits good = goodService.get(id);
		good.setName(name);
		good.setPrice(price);
		good.setIntro(intro);
		good.setStock(stock);
		good.setTypeId(typeId);
		if (Objects.nonNull(cover) && !cover.isEmpty()) {
			good.setCover(UploadingFile.fileUpload(cover));
		}
		if (Objects.nonNull(image1) && !image1.isEmpty()) {
			good.setImage1(UploadingFile.fileUpload(image1));
		}
		if (Objects.nonNull(image2) && !image2.isEmpty()) {
			good.setImage2(UploadingFile.fileUpload(image2));
		}
		goodService.update(good);
		return "redirect:goodList?flag=3&page="+page;
	}

	/**
	 * ��Ʒɾ��
	 * 
	 * @return
	 */
	@RequestMapping("/goodDelete")
	public String goodDelete(int id, 
			@RequestParam(required=false, defaultValue="1") int page) {
		goodService.delete(id);
		return "redirect:goodList?flag=3&page="+page;
	}
	
	/**
	 * ����Ƽ�
	 * @return
	 */
	@RequestMapping("/topSave")
	public @ResponseBody String topSave(Tops tops, 
			@RequestParam(required=false, defaultValue="0")byte status,
			@RequestParam(required=false, defaultValue="1") int page) {
		int id = topService.add(tops);
		return id > 0 ? "ok" : null;
	}
	
	/**
	 * ɾ���Ƽ�
	 * @return
	 */
	@RequestMapping("/topDelete")
	public @ResponseBody String topDelete(Tops tops, 
			@RequestParam(required=false, defaultValue="0")byte status,
			@RequestParam(required=false, defaultValue="1") int page) {
		boolean flag = topService.delete(tops);
		return flag ? "ok" : null;
	}

	/**
	 * ��Ŀ�б�
	 * 
	 * @return
	 */
	@RequestMapping("/typeList")
	public String typeList(HttpServletRequest request) {
		request.setAttribute("flag", 4);
		request.setAttribute("typeList", typeService.getList());
		return "/admin/type-list.jsp";
	}
	
	/**
	 * ͨ�����ֻ�ȡ�б�
	 * 
	 * @return
	 */
	@RequestMapping("/typeSearch")
	public String typeSearch(String name,HttpServletRequest request) {
		request.setAttribute("flag", 4);
		request.setAttribute("typeList", typeService.getList(name));
		return "/admin/type-list.jsp";
	}

	/**
	 * ��Ŀ���
	 * 
	 * @return
	 */
	@RequestMapping("/typeSave")
	public String typeSave(Types type, 
			@RequestParam(required=false, defaultValue="1") int page) {
		typeService.add(type);
		return "redirect:typeList?flag=4&page="+page;
	}

	/**
	 * ��Ŀ����
	 * 
	 * @return
	 */
	@RequestMapping("/typeEdit")
	public String typeUp(int id, HttpServletRequest request) {
		request.setAttribute("flag", 4);
		request.setAttribute("type", typeService.get(id));
		return "/admin/type-edit.jsp";
	}

	/**
	 * ��Ŀ����
	 * 
	 * @return
	 */
	@RequestMapping("/typeUpdate")
	public String typeUpdate(Types type, 
			@RequestParam(required=false, defaultValue="1") int page) {
		typeService.update(type);
		return "redirect:typeList?flag=4&page="+page;
	}

	/**
	 * ��Ŀɾ��
	 * 
	 * @return
	 */
	@RequestMapping("/typeDelete")
	public String typeDelete(Types type, 
			@RequestParam(required=false, defaultValue="1") int page) {
		typeService.delete(type);
		return "redirect:typeList?flag=4&page="+page;
	}

	/**
	 * ����Ա�б�
	 * 
	 * @return
	 */
/*	@RequestMapping("/adminList")
	public String adminList(HttpServletRequest request, 
			@RequestParam(required=false, defaultValue="1") int page) {
		request.setAttribute("flag", 5);
		request.setAttribute("adminList", adminService.getList(page, rows));
		request.setAttribute("pageTool", PageDivide.getPageTool(request, adminService.getTotal(), page, rows));
		return "/admin/admin_list.jsp";
	}
*/
	/**
	 * ����Ա�޸��Լ�����
	 * 
	 * @return
	 */
	@RequestMapping("/adminRe")
	public String adminRe(HttpServletRequest request, HttpSession session) {
		request.setAttribute("flag", 5);
		request.setAttribute("admin", adminService.getByUsername(String.valueOf(session.getAttribute("username"))));
		return "/admin/admin-change.jsp";
	}

	/**
	 * ����Ա�޸��Լ�����
	 * 
	 * @return
	 */
	@RequestMapping("/adminReset")
	public String adminReset(Admins admin, HttpServletRequest request) {
		request.setAttribute("flag", 5);
		if (adminService.get(admin.getId()).getPassword().equals(SafeDivide.encode(admin.getPassword()))) {
			admin.setPassword(SafeDivide.encode(admin.getPasswordNew()));
			adminService.update(admin);
			request.setAttribute("admin", admin);
			request.setAttribute("msg", "�޸ĳɹ�!");
		}else {
			request.setAttribute("msg", "ԭ�������!");
		}
		return "/admin/admin-change.jsp";
	}

	/**
	 * ����Ա���
	 * 
	 * @return
	 */
/*	@RequestMapping("/adminSave")
	public String adminSave(Admins admin, HttpServletRequest request, 
			@RequestParam(required=false, defaultValue="1") int page) {
		if (adminService.isExist(admin.getUsername())) {
			request.setAttribute("msg", "�û����Ѵ���!");
			return "/admin/admin_add.jsp";
		}
		adminService.add(admin);
		return "redirect:adminList?flag=5&page="+page;
	}*/

	/**
	 * ����Ա�޸�
	 * 
	 * @return
	 */
	@RequestMapping("/adminEdit")
	public String adminEdit(int id, HttpServletRequest request) {
		request.setAttribute("flag", 5);
		request.setAttribute("admin", adminService.get(id));
		return "/admin/admin-change.jsp";
	}

	/**
	 * ����Ա����
	 * 
	 * @return
	 */
	@RequestMapping("/adminUpdate")
	public String adminUpdate(Admins admin, 
			@RequestParam(required=false, defaultValue="1") int page) {
		admin.setPassword(SafeDivide.encode(admin.getPassword()));
		adminService.update(admin);
		return "redirect:adminList?flag=5&page="+page;
	}

	/**
	 * ����Աɾ��
	 * 
	 * @return
	 */
	@RequestMapping("/adminDelete")
	public String adminDelete(Admins admin, 
			@RequestParam(required=false, defaultValue="1") int page) {
		adminService.delete(admin);
		return "redirect:adminList?flag=5&page="+page;
	}
	
	@RequestMapping("/view")
	public String view(HttpServletRequest request,Model model) throws ParseException{
		List<Orders>list= orderService.findAll1();
		System.out.print(list.get(0));
		request.setAttribute("flag", 6);
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);         //��ȡ��
		int month = cal.get(Calendar.MONTH) + 1;   //��ȡ��
		int day = cal.get(Calendar.DATE);
		String days = "" ;
		String mouths ="";
		
		if(month<10) {
			mouths="0"+month;
		}else {
			mouths=""+month;
		}
		if(day<10)
			days="0"+day;
		else {
			days=""+day;
		}
		String now=year+"-"+mouths+"-"+days+" "+"00:00:00";
		
		if(day-1<0) {
			day=day+30;
			month--;
			if(month<=0) {
				month=12;
				year--;
			}
		}	
		if(month<10) {
			mouths="0"+month;
		}else {
			mouths=""+month;
		}
		if(day-1<10) {
			day--;
			days="0"+day;
		}
		else {
			day--;
			days=""+day;
		}
		String day1=year+"-"+mouths+"-"+days+" "+"00:00:00";
		if(day-1<0) {
			day=day+30;
			month--;
			if(month<=0) {
				month=12;
				year--;
			}
		}	
		if(month<10) {
			mouths="0"+month;
		}else {
			mouths=""+month;
		}
		if(day-1<10) {
			day--;
			days="0"+day;
		}else {
			day--;
			days=""+day;
		}
		String day2=year+"-"+mouths+"-"+days+" "+"00:00:00";
		if(day-1<0) {
			day=day+30;
			month--;
			if(month<=0) {
				month=12;
				year--;
			}
		}	
		if(month<10) {
			mouths="0"+month;
		}else {
			mouths=""+month;
		}
		if(day-1<10) {
			day--;
			days="0"+day;
		}else {
			day--;
			days=""+day;
		}
		String day3=year+"-"+mouths+"-"+days+" "+"00:00:00";
		if(day-1<0) {
			day=day+30;
			month--;
			if(month<=0) {
				month=12;
				year--;
			}
		}	
		if(month<10) {
			mouths="0"+month;
		}else {
			mouths=""+month;
		}
		if(day-1<10) {
			day--;
			days="0"+day;
		}else {
			day--;
			days=""+day;
		}
		String day4=year+"-"+mouths+"-"+days+" "+"00:00:00";
		if(day-1<0) {
			day=day+30;
			month--;
			if(month<=0) {
				month=12;
				year--;
			}
		}	
		if(month<10) {
			mouths="0"+month;
		}else {
			mouths=""+month;
		}
		if(day-1<10) {
			day--;
			days="0"+day;
		}else {
			day--;
			days=""+day;
		}
		String day5=year+"-"+mouths+"-"+days+" "+"00:00:00";
		if(day-1<0) {
			day=day+30;
			month--;
			if(month<=0) {
				month=12;
				year--;
			}
		}	
		if(month<10) {
			mouths="0"+month;
		}else {
			mouths=""+month;
		}
		if(day-1<10) {
			day--;
			days="0"+day;
		}else {
			day--;
			days=""+day;
		}
		String day6=year+"-"+mouths+"-"+days+" "+"00:00:00";
		if(day-1<0) {
			day=day+30;
			month--;
			if(month<=0) {
				month=12;
				year--;
			}
		}	
		if(month<10) {
			mouths="0"+month;
		}else {
			mouths=""+month;
		}
		if(day-1<10) {
			day--;
			days="0"+day;
		}else {
			day--;
			days=""+day;
		}
		String day7=year+"-"+mouths+"-"+days+" "+"00:00:00";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nows = dateFormat.parse(now);
		Date dateTime1 = dateFormat.parse(day1);
		Date dateTime2 = dateFormat.parse(day2);
		Date dateTime3 = dateFormat.parse(day3);
		Date dateTime4 = dateFormat.parse(day4);
		Date dateTime5 = dateFormat.parse(day5);
		Date dateTime6 = dateFormat.parse(day6);
		Date dateTime7 = dateFormat.parse(day7);
		int mount1=0;
		int mount2=0;
		int mount3=0;
		int mount4=0;
		int mount5=0;
		int mount6=0;
		int mount7=0;
		
		for(int i=0;i<list.size();i++) {
			if(list.get(i).getSystime().compareTo(dateTime7)>0&&list.get(i).getSystime().compareTo(dateTime6)<0) {
				mount1+=list.get(i).getTotal();
			}
			if(list.get(i).getSystime().compareTo(dateTime6)>0&&list.get(i).getSystime().compareTo(dateTime5)<0) {
				mount2+=list.get(i).getTotal();
			}
			if(list.get(i).getSystime().compareTo(dateTime5)>0&&list.get(i).getSystime().compareTo(dateTime4)<0) {
				mount3+=list.get(i).getTotal();
			}
			if(list.get(i).getSystime().compareTo(dateTime4)>0&&list.get(i).getSystime().compareTo(dateTime3)<0) {
				mount4+=list.get(i).getTotal();
			}
			if(list.get(i).getSystime().compareTo(dateTime3)>0&&list.get(i).getSystime().compareTo(dateTime2)<0) {
				mount5+=list.get(i).getTotal();
			}
			if(list.get(i).getSystime().compareTo(dateTime2)>0&&list.get(i).getSystime().compareTo(dateTime1)<0) {
				mount6+=list.get(i).getTotal();
			}
			if(list.get(i).getSystime().compareTo(dateTime1)>0&&list.get(i).getSystime().compareTo(nows)<0) {
				mount7+=list.get(i).getTotal();
			}
			
			
			
		}
		int[] datas=new int[7];
		datas[0]=mount1;
		datas[1]=mount2;
		datas[2]=mount3;
		datas[3]=mount4;
		datas[4]=mount5;
		datas[5]=mount6;
		datas[6]=mount7;
		
		model.addAttribute("data",datas);
		return "/admin/view.jsp";
		
		
	}
}
