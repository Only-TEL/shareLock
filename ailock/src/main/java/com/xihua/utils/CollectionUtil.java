package com.xihua.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {

    public static List<List> groupList(List list, int toIndex) {
        List<List> listGroup = new ArrayList();
        int listSize = list.size();

        for(int i = 0; i < list.size(); i += toIndex) {
            if (i + toIndex > listSize) {
                toIndex = listSize - i;
            }

            List newList = list.subList(i, i + toIndex);
            listGroup.add(newList);
        }

        return listGroup;
    }
}
