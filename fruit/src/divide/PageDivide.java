package divide;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * ��ҳ������
 */
public class PageDivide {
	
	/**
	 * ��ȡ��ҳ����
	 * @param total �ܼ�¼��
	 * @param page ��ǰҳ��
	 * @param size ÿҳ����
	 * @return
	 */
	public static String getPageTool(HttpServletRequest request, long total, int page, int size){
		long pages = total % size ==0 ? total/size : total /size + 1;
		pages = pages==0 ? 1 : pages;
		String url = request.getRequestURL().toString();
		StringBuilder queryString = new StringBuilder();
		Enumeration<String> enumeration = request.getParameterNames();
		try { // ƴװ�������
			while (enumeration.hasMoreElements()) {
				String element = (String) enumeration.nextElement();
				if(!element.contains("page")) { // ����page����
					queryString.append("&").append(element).append("=").append(java.net.URLEncoder.encode(request.getParameter(element),"UTF-8"));
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// ƴװ��ҳ����
		StringBuilder buf = new StringBuilder();
		buf.append("<div style='text-align:center;'>\n");
		if (page <= 1) {
			buf.append("<a class='btn btn-info' disabled >��ҳ</a>\n");
		}else{
			buf.append("<a class='btn btn-info' href='").append(url).append("?page=").append(1).append(queryString).append("'>��ҳ</a>\n");
		}
		if (page <= 1) {
			buf.append("<a class='btn btn-info' disabled >��һҳ</a>\n");
		}else {
			buf.append("<a class='btn btn-info' href='").append(url).append("?page=").append(page>1 ? page-1 : 1).append(queryString).append("'>��һҳ</a>\n");
		}
		buf.append("<h2 style='display:inline;'>[").append(page).append("/").append(pages).append("]</h2>\n");
		buf.append("<h2 style='display:inline;'>[").append(total).append("]</h2>\n");
		if (page >= pages) {
			buf.append("<a class='btn btn-info' disabled >��һҳ</a>\n");
		}else {
			buf.append("<a class='btn btn-info' href='").append(url).append("?page=").append(page<pages ? page+1 : pages).append(queryString).append("'>��һҳ</a>\n");
		}
		if (page >= pages) {
			buf.append("<a class='btn btn-info' disabled >βҳ</a>\n");
		}else {
			buf.append("<a class='btn btn-info' href='").append(url).append("?page=").append(pages).append(queryString).append("'>βҳ</a>\n");
		}
		buf.append("<input type='text' class='form-control' style='display:inline;width:60px;' value=''/>");
		buf.append("<a class='btn btn-info' href='javascript:void(0);' onclick='location.href=\"").append(url).append("?page=").append("\"+(this.previousSibling.value)+\"").append(queryString).append("\"'>GO</a>\n");
		buf.append("</div>\n");
		return buf.toString();
	}

}
