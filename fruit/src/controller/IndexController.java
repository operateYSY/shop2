package controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import entity.Tops;
import service.FruitService;
import service.TopService;
import service.TypeService;
import divide.PageDivide;

/**
 * 前台相关接口
 */
@Controller
@RequestMapping("/index")
public class IndexController{
	
	private static final int rows = 16; // 默认每页数量

	@Autowired
	private TopService topService;
	@Autowired
	private FruitService goodService;
	@Autowired
	private TypeService typeService;
	

	/**
	 * 首页
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request){
		request.setAttribute("flag", 1);
		request.setAttribute("typeList", typeService.getList());
		request.setAttribute("top1List", topService.getList(Tops.TYPE_SCROLL, 1, 1));
		request.setAttribute("top2List", topService.getList(Tops.TYPE_LARGE, 1, 6));
		request.setAttribute("top3List", topService.getList(Tops.TYPE_SMALL, 1, 8));
		return "/index/index.jsp";
	}
	
	/**
	 * 推荐列表
	 * @return
	 */
	@RequestMapping("/top")
	public String tops(int typeid, @RequestParam(required=false, defaultValue="1")int page, HttpServletRequest request) {
		request.setAttribute("flag", typeid==2 ? 7 : 8);
		request.setAttribute("typeList", typeService.getList());
		request.setAttribute("goodList", goodService.getList(typeid, page, rows));
		request.setAttribute("pageTool", PageDivide.getPageTool(request, goodService.getTotal(typeid), page, rows));
		return "/index/fruits.jsp";
	}
	
	/**
	 * 商品列表
	 * @return
	 */
	@RequestMapping("/goods")
	public String goods(int typeid, @RequestParam(required=false, defaultValue="1")int page, HttpServletRequest request){
		request.setAttribute("flag", 2);
		if (typeid > 0) {
			request.setAttribute("type", typeService.get(typeid));
		}
		request.setAttribute("typeList", typeService.getList());
		request.setAttribute("goodList", goodService.getListByType(typeid, page, rows));
		request.setAttribute("pageTool", PageDivide.getPageTool(request, goodService.getTotalByType(typeid), page, rows));
		return "/index/fruits.jsp";
	}
	
	/**
	 * 商品详情
	 * @return
	 */
	@RequestMapping("/looking")
	public String detail(int goodid, HttpServletRequest request){
		request.setAttribute("good", goodService.get(goodid));
		request.setAttribute("typeList", typeService.getList());
		return "/index/looking.jsp";
	}
	
	/**
	 * 搜索
	 * @return
	 */
	@RequestMapping("/search")
	public String search(String name, @RequestParam(required=false, defaultValue="1")int page, HttpServletRequest request) {
		if (Objects.nonNull(name) && !name.trim().isEmpty()) {
			request.setAttribute("goodList", goodService.getListByName(name, page, rows));
			request.setAttribute("pageTool", PageDivide.getPageTool(request, goodService.getTotalByName(name), page, rows));
		}
		request.setAttribute("typeList", typeService.getList());
		return "/index/fruits.jsp";
	}

}