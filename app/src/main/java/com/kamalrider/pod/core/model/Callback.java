package com.kamalrider.pod.core.model;

public interface Callback<T> {

    void onSuccess(T result);

    void onFailure(String errorMessage);

}
