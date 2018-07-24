package com.game.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 按键缓冲类
 */
public class KeyBufferUtil {

    //创建集合用于存储键盘的按键
    private static List<Integer> keys = new ArrayList<Integer>();

    /**
     * 按键按下
     * @param key
     */
    public static void KeyDown(int key) {
        //判断缓冲区中不存在指定的值
        if (!keys.contains((Integer) key)) {
            //存入
            keys.add((Integer) key);
        }
    }

    /**
     * 按键抬起
     * @param key
     */
    public static void KeyUp(int key) {
        //判断缓冲区中存在指定的值
        if (keys.contains((Integer) key)) {
            //移除
            keys.remove((Integer) key);
        }
    }

    /**
     * 判断是否存在某个键
     * @param key
     * @return
     */
    public static boolean contains(int key) {
        return keys.contains((Integer) key);
    }
}
