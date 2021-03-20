package com.bielinskim;

import java.util.List;

public class Console {

    public static void displayList(List list) {
        for(int i=0; i<list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

}
