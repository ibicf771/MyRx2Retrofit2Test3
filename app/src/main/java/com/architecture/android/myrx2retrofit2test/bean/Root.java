package com.architecture.android.myrx2retrofit2test.bean;

import android.util.Log;

/**
 * Created by yangsimin on 2018/7/18.
 */

public class Root {
    private String code;

    private String message;

    private String success;

    private ResultValue resultValue;

    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setSuccess(String success){
        this.success = success;
    }
    public String getSuccess(){
        return this.success;
    }
    public void setResultValue(ResultValue resultValue){
        this.resultValue = resultValue;
    }
    public ResultValue getResultValue(){
        return this.resultValue;
    }

    public class ResultValue {
        private String key;

        private long timestamp;

        public void setKey(String key){
            this.key = key;
        }
        public String getKey(){
            return this.key;
        }
        public void setTimestamp(int timestamp){
            this.timestamp = timestamp;
        }
        public long getTimestamp(){
            return this.timestamp;
        }

    }

    public String getAllMsg(){
        return "code " + code + " message " + message
                + " success " + success + " key " + resultValue.key
                + " timestamp " + resultValue.timestamp;
    }
    public void show() {
        Log.d("root_log", " 你好");
        Log.d("Root_log", "code " + code + " message " + message
                + " success " + success + " key " + resultValue.key
                + " timestamp " + resultValue.timestamp);
    }

}
