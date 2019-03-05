package com.coffeehouse.restapi;

import com.google.gson.annotations.SerializedName;

public class ResponseData<T> {

    @SerializedName("content")
    private T content;

    private String errorCode;

    private String message;

    public T getContent() {
        return content;
    }

    public void setContent(T value) {
        this.content = value;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }
}
