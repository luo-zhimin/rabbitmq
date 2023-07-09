package com.image.rabbitmq.util;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/9 18:32
 */
public class SleepUtils {

    public static void sleep(int second) {
        try {
            Thread.sleep(1000L * second);
        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
            Thread.currentThread().interrupt();
        }
    }
}
