package com.zcc;

import java.io.FileWriter;

/**
 * @description:
 * @author:zcc
 * @data:2019/3/2 0002
 */
public class AlipayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016092800614003";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQCJ1FiELDwY3zAhnozb54K5LPICrlX25c5GASGPrytxWIDQTAoSxNQjQiImWDunpyrru/U9qVj7x8Tm/VF3LCDvY8xj8abNBS6Xf1pT+V4JJeZlSzodgKSHS80mBNRNEJlIpSlaWjRC+7NCJBH+G63YB0ComBpwH81ZkonPmkHz1Ub5Imu1x+BbW0v9jwR2QuioznyF4DmYol+kxY4NrPJcjnDwzGqb3a/bDfrbfwZqfrSHFLX53D/xYKM/x6Z9Kukj0fU8EwwcdpQfw4gtOHpqAPw38qozVEBahrvIBeVmgyqxKASCRrsYHD2vQfmFCpZRfcbtzN2B7xLd4jELI02nAgMBAAECggEBAIATTSmM/U9TXiNvdI8Hw0aViFGLdKBYDWSpumkNKn5J8nEcp6OnOUWGN7uP8+cnhM/HOe1Rhzl3+fV3YZMu82/gOdaJgCk1boUit2oe6A3yJPTtgZRWrQx4GKsCg9EU4otJxnKKLdQslfeZEwsg51s96gyAQtj8BFL+w4ahXjXYEBEHkcIn4jqFfas3EI7pNHwaHHo5aFz2xEO7/IpGhm2u8VcA8hAvGGOgWs/Go6RKkhZ4QTP1wXzYT+ivsk0YWA7ZyGHFtz9QeKfSLc42dfhw0JDWH3RkueKCBGVHy2FeYKvcoPBr869u5Waw1lsZrrdmcp/OodnDbamffSu7QMkCgYEA4WIiOTib2LpIRaDAknLDyr1jQbA2tK8+7NOAJbOsxZ8Aj+OcCQE0jUNPxAX04OOPzZUg9k6N3QniA5m2W5uoLDSPPw0RpgrrRb0b3kghJ8UY/xjbNLsxqyQrjfXwC8GW7pFmCBdpcNNts0j7Hz9omdTk57EAqikA8VDV5LvCf0MCgYEAnI120mGfYJCVnqz+JlKAZKQb+9l1ZkPo9e6xDv0VDS66dpfwTvf7U8ojSSHY4CZaTOarA4c+NFmlfNnDCoCc9b8d0sh7QUPiaj3vvMJloeetqrbFKpdUcQnmAHQJHDixDiW63JH0dMY4UjknSGRT7/JgDhHTmKZ2M9L76s4EN80CgYEAtNBGxoM8vF2OF5YEiDjcDK1tkMhuyvwAznxLRXPk+tI54DXo+2xlHaNPveJNd5H4zySBwjh5JUiFcwABRXMWMwijlyDD9OKzp0o0rlpNhxtfl1/4lFBNY5BsBMK2pqfTj2dEZN9rbuGgE4JMPOBg6XDbh2kAMIMex3pjbtKXEgMCgYEAilwYdM0kr5J5OZjIsZPet5O2y7ogIPbJ8Hk5cPbJ4TG4SFH2Z8SREjenQd4xL8GVszt2TxL7LgdA0/wdfa23ixWSxRS8oizd8Wm19MyWSCZa/XK1BPRkA9dsuF/VIWUVTlyYLpKsS3xYkIrEph1NddZvgF/O863C8AB69sWHQs0CgYEAk3eNV2iOI1i1QQT8TMyQm+4aSFRv/s3coTl6wRQj8FeBSmKyIbmUQShvqTZJSwkcxpCOW1+EXDohWXcX5SexQSAOUm9l1rTg/WnbngDfSRLsYWaFE75kpT8bd+Q28Wp+NBCCKpwQm7SBJWrLt3lHTE7MP2flfP1i5KLzwEF3yIo=";

    // 支付宝公钥
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApvDzJ004hx7Uhn1ByjaIjtwejenF5KAkkHi6d8QbwVGjdo52nfedxmU6qb9J4NxoWkiS9cZCIWyDM038mdriAcKxAftZFDok2qo6kMhX7XkiG0ROknHGWgdnO1prJwcl9GXr98kULDLOylv6fyQdc9Wlt7ePJ29Fi18cOdU+cbl6KHgw/9Vr1YEDqLmYUkBiZ1O6CA8Su19u3rrZ5b9XIYOq/Biko7f/o3cwjQsbPaegjhVnj0JLHNhAsb/YcqOQsMfzy3Oc9bPMcwCqMhH33vIb8tLXAqZ55anSrns2t/wKztI0/A4bZFD2ej3ZsMRQXlyJE03gc/mltlbFOeTyNwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:9090/pay/api/alipayNotify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:9090/pay/api/alipayBack";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";


}
