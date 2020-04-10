package divide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;

/**
 * �ϴ�������
 * spring mvn֧��
 */
public class UploadingFile {
	

	/**
	 * ͼƬ�ϴ�
	 * @return �������·��
	 * @param photo ͼƬ�ļ�
	 * @param photoFileName �ļ���
	 * @param savePath �ļ�����·��(�����web��Ŀ¼)
	 * @return
	 * @throws Exception 
	 */
	public static String fileUpload(MultipartFile file) throws Exception{
		// �ж��Ƿ����ϴ��ļ�
		if (Objects.isNull(file) || file.isEmpty() || Objects.isNull(file.getOriginalFilename())) {
			return null;
		}
		String savePath = "picture"; // �����ļ������Ŀ¼
		String fileName = file.getOriginalFilename();
		// �ļ��洢·��
		String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/")+savePath;			
		// ��ȡ��ǰ�ļ�����
		String type = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
		// ��ȡ��ǰϵͳʱ���ַ��� + ��λ�����  + ��׺��
		String newFileName = new SimpleDateFormat("yyMMddssSSS").format(new Date()) + new Random().nextInt(899)+100 + "." + type;
		// ��ָ��·�������������ļ�
		File savefile = new File(path,newFileName);
		// �������ļ����ļ��в������򴴽�
		if(!savefile.getParentFile().exists()){
			savefile.getParentFile().mkdirs();
		}
		System.out.println("�ϴ��ļ�����·��: "+savefile.getPath());
		file.transferTo(savefile);
		return savePath+"/"+newFileName;
	}

}
