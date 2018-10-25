package com.bwie.cart1023.cart.presenter;

import com.bwie.cart1023.bean.MessageBean;
import com.bwie.cart1023.bean.Product;
import com.bwie.cart1023.bean.Shopper;
import com.bwie.cart1023.cart.model.CartModel;
import com.bwie.cart1023.cart.view.IView;
import com.bwie.cart1023.inter.INetCallBack;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by eric on 2018/10/23.
 */

public class CartPresenter {
    private IView iv;
    private CartModel model;

    public void attach(IView iv) {
        this.iv = iv;
        model = new CartModel();
    }

    public void getData() {
        String url = "http://www.zhaoapi.cn/product/getCarts?uid=1538";
        Type type = new TypeToken<MessageBean<List<Shopper<List<Product>>>>>() {
        }.getType();

        model.getData(url, new INetCallBack() {
            @Override
            public void success(Object obj) {
                MessageBean<List<Shopper<List<Product>>>> data = (MessageBean<List<Shopper<List<Product>>>>) obj;
                iv.success(data);
            }

            @Override
            public void failed(Exception e) {
                iv.failed(e);
            }
        }, type);

    }

    public void detach() {
        if (iv != null) {
            iv = null;
        }
    }
}
