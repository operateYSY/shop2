package controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * ��̨��¼��֤������
 */
public class MasterHinder extends HandlerInterceptorAdapter{

	/**
	 * ����¼״̬
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		if(uri.contains("css/") || uri.contains("js/") || uri.contains("img/") 
				|| uri.contains("login") || uri.contains("logout")) {
			return true; // ������·��
		}
		Object username = request.getSession().getAttribute("username");
		if (Objects.nonNull(username) && !username.toString().trim().isEmpty()) {
			return true; // ��¼��֤ͨ��
		}
		response.sendRedirect("login.jsp");
		return false; // �������һ������
	}

}
