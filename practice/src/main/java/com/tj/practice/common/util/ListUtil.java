package com.tj.practice.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/15 11:22
 */
public class ListUtil {

    public static <T> List<List<T>> splitList(List<T> list, int len) {
        if (list == null || list.isEmpty() || len < 1) {
            return Collections.emptyList();
        }
        List<List<T>> result = new ArrayList<>();
        int size = list.size();
        int count = (size + len - 1) / len;
        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }
}
