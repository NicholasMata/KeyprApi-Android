package com.matadesigns.keyprapi;

import java.io.Serializable;

public interface Handler<T extends Serializable> {
    void onSuccess(T value);
    void onError(Exception err);
}
