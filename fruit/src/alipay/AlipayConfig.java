package alipay;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2019-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016101300672847";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCUbxSFH49FSl7f98zc9tPffxEu8hneBe4N0eSYXuTdN8/ZCntmKeWh75hyNhZtS6GmzZ/hSeKcw1n/CHnfVjLRdGh+ogvJsmQ0bloR23Qb4fqswRZ2DyqqPuCgeRPbsEhxkzIOOuVcBBdR9sLhUtb7bKzjLfcck14M3Odc8K1vLpg0E4f4uaupZ2piRKowOM0d4ndE6mIqwBXS8XTIyWynov6ZiyCri6aQsqTnvcav+AW8Gi4hillEH280IpPEM4B/CGF8KZKl9BINKRFVVO2ZH2U4HoVIwMsjM/ivDqEUGA+2oEkuIwMejhH/YKvvZtEI47qT2xMR5yLEPF0StjljAgMBAAECggEAS59QGAadKpMaRQbtg4HpLcuCCKXlkdz5jGbsDMkD1sSxiwUxGzqrOFZmF4IA6QwIPoAyPUg0QOOx/SHnXWaAG4AwIJwUX10YejHih4iD3hTCbrMs61cMyJGiRX+KI3nrXpm8fc15vuiCrQqH/N4EkzGp31AyvodB+lGtOgeQhLyxKM8Mv+F7dLduSCySupRkp0w/htRhg1ul23d05d1m7FkwqIzPCqtW7pulJF7otNjCnKpVlnH4m546zzb0i2gebXmVfkWh35FOJ/NToW68ORX+9MnhKG6Tt54jf1zBkvW17qGkDJfzRyjcdbDkN5sGI+dRpkPxUabOFQy6brxF4QKBgQDryF5c3Ho9niQstBUYwTMaEsbwWzeU1WHRYbU6B1+//xjQichKOVhwRCsZeJQKytFmaqaUrYWgmzJcHyyWB6xcxZhnelW4rj+K3XS9X9cs39Qy8+cpFdR3MoouDESGVK8TQp+jLkHrwpQoye328Mx5y4aPwCTqZj+9D/DzVcZ60wKBgQChKVTHoQbIM3ZvUC3vjB5LQUL9zMFnzH4CBUF4mL5iHLRfWSnJyqNi4F0pjgAGGaEWQZDHYaLJrycwSGbkeGJqOjNArUHlx/2o73UPvPNU+Gx6tTGSY2Wu4ROymGvl7ELR1hOwQ5x8dUTsDJBbVJ6LWqwO+arUdv6hBsecORQNMQKBgGOIuF0XLftpEPhWJTnIBVySZKz+tN39nkeXqbuPdVpFxCH0qDq9LppmDJO6FTThHbjQePX2spx0J7Q/7MCBCMA5Ifc864cE+AoVJrzzApENBDVmRUVQ09pl2XNK9nqGsUm+LEnksytlfsohRHWkfY1E3887W9ofw/R6EEuJADrFAoGAOVu5dddkz4opY9K4lqJbAdO3YRcn2qaYTmXKeH58e/TVaX4VdmFtpzpNTgXhEkq63230dlHUm02AeBm8rSH/fOk75pADfSGhsSeQlUpdnGdSTJYCj6Raw1QOK6k99bYRzhcQKNLXdiBSqGSMWEUUrGpYBHhtiXLUi0EbeAmEjnECgYEA2GXIFkqCOBimo5llvGkdykb2mnAk64zMexn2fYnPKqJO5bCVuoy2lHwTtr4tH82gHPXr16zLCaqPr5qlP2/waNy0ju2gD9mPw6WyKux/86aR91MAgl8w7U5O5g+of3uRJ8p7xikWrVuZen2FlVXaNJrzK7Zo6zI2vOIwiExHihc=";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlG8UhR+PRUpe3/fM3PbT338RLvIZ3gXuDdHkmF7k3TfP2Qp7Zinloe+YcjYWbUuhps2f4UninMNZ/wh531Yy0XRofqILybJkNG5aEdt0G+H6rMEWdg8qqj7goHkT27BIcZMyDjrlXAQXUfbC4VLW+2ys4y33HJNeDNznXPCtby6YNBOH+LmrqWdqYkSqMDjNHeJ3ROpiKsAV0vF0yMlsp6L+mYsgq4umkLKk573Gr/gFvBouIYpZRB9vNCKTxDOAfwhhfCmSpfQSDSkRVVTtmR9lOB6FSMDLIzP4rw6hFBgPtqBJLiMDHo4R/2Cr72bRCOO6k9sTEecixDxdErY5YwIDAQAB";
    		

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://localhost:8080/shop/pay/notify";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://localhost:8080/shop/index/pay2";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

