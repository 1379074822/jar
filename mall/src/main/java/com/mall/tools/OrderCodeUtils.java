package com.mall.tools;

import java.util.Date;
import java.util.Random;


/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 生成订单编号工具类
 */
public class OrderCodeUtils {

    public static String orderCode(Integer id){
        String result="";
        Random random = new Random();
        String newDate = DateUtils.format(new Date(),"yyyyMMddHHmm");
        for(int i=0;i<3;i++){
            result += random.nextInt(10);
        }
        return result+newDate+id;
    }
    public static void main(String[] args) {
        String id = "1";
        System.out.println(orderCode(1));
    }
}
