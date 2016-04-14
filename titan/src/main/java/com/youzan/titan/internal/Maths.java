package com.youzan.titan.internal;

import android.support.annotation.NonNull;

/**
 * Created by liangfei on 4/14/16.
 */
public class Maths {
    public static int max(@NonNull int[] array) {
        int max = Integer.MIN_VALUE;
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }
}
