package com.train.sytstem.center.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * SubListTest
 *
 * @author taokai3
 * @date 2018/6/25
 */
public class SubListTest {

    @Test
    public void subList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < 14; i++) {
            list.add(i);
        }

        for (int i = 0; i < 2; i++) {
            list.add(i);
        }

        int size = list.size();
        System.out.println("All size:" + size);

        int index = 0;
        while (index < size) {
            int temp = index;
            index = index + 5;
            if(index > size){
                index = size;
            }
            System.out.println("End index:"+index);
            List<Integer> subList = list.subList(temp, index);
            int subSize = subList.size();
            System.out.println("Sub size:" + subSize);
            System.out.println("subList" + subList.toString());
        }
    }
}
