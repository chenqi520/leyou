package com.leyou.common.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author cq
 * @create 2018-12-03 17:47
 * @copy BigData
 */

/**
 * 微信抢红包玩法
 */

public class Pay {

    public static void main(String[] args) {
        int count=100;//红包总金额
        int portion=33;//份数
        int maxlength=portion;
        List<Integer> proportions=new ArrayList<>();
        //生成一组随机领取数据存入List集合
        for (int i = 0; i < maxlength; i++) {
            if(portion == 1){
                proportions.add(count);
            }else{
                //调用获取随机数的方法
                int proportion=getProportion(count/portion);
                proportions.add(proportion);
                //份数递减
                portion--;
                //总金额递减已领金额
                count-=proportion;
            }
        }
        //打乱数据（因为越往后生成数据值越大）
        Collections.shuffle(proportions);
        int  proportionSum=0;
        for (int double1 : proportions) {
            System.out.println(double1);
            proportionSum+=double1;
        }
        System.out.println(proportionSum);
    }
    //生成随机数方法
    public static int getProportion(int max){
        Random random=new Random();
        int proportion=random.nextInt(max);
        //当生成数值为0时，重新生成
        if(proportion==0){
            proportion=getProportion(max);
        }
        return proportion;
    }


}
