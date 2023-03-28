package com.example.order_online.handler;

import cn.hutool.core.util.RandomUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.text.TextProducer;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author 16537
 * @Classname KaptchaTextCreateHandler
 * @Description
 * @Version 1.0.0
 * @Date 2023/1/15 11:00
 */
@Component
public class KaptchaTextCreateHandler implements TextProducer {
    @Override
    public String getText() {
        StringBuilder text = new StringBuilder();
        int i = RandomUtil.randomInt(0, 2);
        int num1 = RandomUtil.randomInt(0, 10);
        int num2 = RandomUtil.randomInt(0, 10);
        switch (i){
            case 0:
                text.append(num1).append("+").append(num2).append("=").append(num1+num2);
                break;
            case 1:
                if (num1>num2){
                    text.append(num1).append("-").append(num2).append("=").append(num1-num2);
                }else{
                    text.append(num2).append("-").append(num1).append("=").append(num2-num1);
                }
                break;
            default:break;
        }
        return text.toString();
    }
}
