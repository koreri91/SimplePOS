package com.app.estockcard.controller;

public interface OnDBResultListener {

    void onSuccess(String success);
    void onError(String error);
}
