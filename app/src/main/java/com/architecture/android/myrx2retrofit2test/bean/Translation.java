package com.architecture.android.myrx2retrofit2test.bean;

import android.util.Log;

/**
 * Created by yangsimin on 2018/4/24.
 */

public class Translation {
    private int status;

    private content content;
    private static class content {
        private String from;
        private String to;
        private String vendor;
        private String out;
        private int errNo;
    }

    //定义 输出返回数据 的方法
    public void show() {
        System.out.println(status);

        System.out.println(content.from);
        System.out.println(content.to);
        System.out.println(content.vendor);
        System.out.println(content.out);
        System.out.println(content.errNo);
        Log.d("Translation", "from " + content.from + " to " + content.to);
        Log.d("Translation", "out " + content.out);
    }
}

