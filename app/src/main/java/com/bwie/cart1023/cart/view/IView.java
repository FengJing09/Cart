package com.bwie.cart1023.cart.view;

import com.bwie.cart1023.bean.MessageBean;
import com.bwie.cart1023.bean.Product;
import com.bwie.cart1023.bean.Shopper;

import java.util.List;

/**
 * Created by eric on 2018/10/23.
 */

public interface IView {
    void success(MessageBean<List<Shopper<List<Product>>>> data);

    void failed(Exception e);
}
