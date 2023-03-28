package com.example.order_online.config;


import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

@Configuration
@PropertySource("classpath:wxpay.properties")
@ConfigurationProperties(prefix = "wxpay") //读取wxpay节点
@Data //使用set方法将wxpay节点中的值填充到当前类的属性中
@Slf4j
public class WxPayConfig {

    // 商户号
    private String mchId;

    // 商户API证书序列号
    private String mchSerialNo;

    // 商户私钥文件
    private String privateKeyPath;

    // APIv3密钥
    private String apiV3Key;

    // APPID
    private String appid;

    // 微信服务器地址
    private String domain;

    // 接收结果通知地址
    private String notifyDomain;

    // APIv2密钥
    private String partnerKey;

    private PrivateKey getPrivateKey(String filename) {
        PrivateKey merchantPrivateKey;
        try {
            merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("私钥文件不存在", e);
        }
        return merchantPrivateKey;
    }

    /**
     * 获取前面验证器
     * @return
     */
    @Bean
    Verifier getVerifier() throws Exception {
        PrivateKey privateKey = getPrivateKey(privateKeyPath);
        //私钥签名对象(对用户发送的请求进行签名)
        PrivateKeySigner signer = new PrivateKeySigner(mchSerialNo, privateKey);
        WechatPay2Credentials credentials = new WechatPay2Credentials(mchId, signer);
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        certificatesManager.putMerchant(mchId,credentials,apiV3Key.getBytes(StandardCharsets.UTF_8));
        return certificatesManager.getVerifier(mchId);

    }

    /**
     * 获取http请求对象
     * @param verifier
     * @return
     */
    @Bean
    CloseableHttpClient getWxPayClient(Verifier verifier) {
        PrivateKey privateKey = getPrivateKey(privateKeyPath);
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                //商户信息
                .withMerchant(mchId,mchSerialNo,privateKey)
                //验证器对象
                .withValidator(new WechatPay2Validator(verifier));

        return builder.build();
    }


    public String getAuthorizationHeader(long timestamp, String nonceStr, String method, String uri,String body) throws Exception {
        String signatureStr=getSignatureStr(timestamp,nonceStr,method,uri,body);
        PrivateKey privateKey = getPrivateKey(privateKeyPath);
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(signatureStr.getBytes(StandardCharsets.UTF_8));
        byte[] signByte = sign.sign();
        String signature = Base64.getEncoder().encodeToString(signByte);
        return "WECHATPAY2-SHA256-RSA2048"+" "
                +"mchid="+"\""+mchId+"\""+","
                +"timestamp="+"\""+timestamp/1000+"\""+","
                +"nonce_str="+"\""+nonceStr+"\""+","
                +"serial_no="+"\""+mchSerialNo+"\""+","
                +"signature="+"\""+signature+"\"";
    }

    private String getSignatureStr(long timestamp, String nonceStr, String method, String uri, String body) {

        return method+"\n"
                +uri+"\n"
                +timestamp/1000+"\n"
                +nonceStr+"\n"
                +body+"\n";
    }
}
