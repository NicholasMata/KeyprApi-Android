package com.matadesigns.keyprapi.models;

import java.io.Serializable;

public class DataResponse<T extends Serializable> implements Serializable {
    public T data;
}
