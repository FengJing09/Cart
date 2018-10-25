package com.bwie.cart1023.cart.model;

import com.bwie.cart1023.inter.INetCallBack;
import com.bwie.cart1023.utils.HttpUtils;

import java.lang.reflect.Type;

/**
 * Created by eric on 2018/10/23.
 */

public class CartModel {
    public void getData(String url, INetCallBack callBack, Type type) {
        HttpUtils.getInstance().get(url, callBack, type);
    }
}
