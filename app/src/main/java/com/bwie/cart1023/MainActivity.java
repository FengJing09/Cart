package com.bwie.cart1023;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.cart1023.adapter.ProductAdapter;
import com.bwie.cart1023.adapter.ShopperAdapter;
import com.bwie.cart1023.bean.MessageBean;
import com.bwie.cart1023.bean.Product;
import com.bwie.cart1023.bean.Shopper;
import com.bwie.cart1023.cart.presenter.CartPresenter;
import com.bwie.cart1023.cart.view.IView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IView {
    private static final String TAG = "MainActivity";
    //    MessageBean<List<ShopperAdapter<List<Product>>>>
    private TextView txtEditFinish;
    private CheckBox cbTotal;
    private TextView txtPrice;
    private Button btnCalu;
    private RecyclerView rvShoper;

    private CartPresenter presenter;
    private ShopperAdapter adapter;

    private List<Shopper<List<Product>>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        setListener();

    }

    private void setListener() {
//        cbTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // 遍历一级列表，和下方的全选状态一致
//                for (Shopper<List<Product>> listShopper : list) {
//                    listShopper.setChecked(isChecked);
//                    // 遍历二级列表，和下方的全选状态一致
//                    List<Product> products = listShopper.getList();
//                    for (Product product : products) {
//                        product.setChecked(isChecked);
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });

        cbTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = cbTotal.isChecked();
                // 遍历一级列表，和下方的全选状态一致
                for (Shopper<List<Product>> listShopper : list) {
                    listShopper.setChecked(isChecked);
                    // 遍历二级列表，和下方的全选状态一致
                    List<Product> products = listShopper.getList();
                    for (Product product : products) {
                        product.setChecked(isChecked);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {
        list = new ArrayList<>();
        // 商家的列表
        adapter = new ShopperAdapter(this, list);
        // 添加一级条目（商家）状态发生变化时
        adapter.setOnShopperClickListener(new ShopperAdapter.OnShopperClickListener() {
            @Override
            public void onShopperClick(int position, boolean isCheck) {
                // 为了效率考虑，当点击状态变成未选中时，全选按钮肯定就不是全选了，就不用再循环一次
                if (!isCheck) {
                    cbTotal.setChecked(false);
                } else {
                    // 如果是商家变成选中状态时，需要循环遍历所有的商家是否被选中
                    // 循环遍历之前先设置一个true标志位，只要有一个是未选中就改变这个标志位为false
                    boolean isAllShopperChecked = true;
                    for (Shopper<List<Product>> listShopper : list) {
                        // 只要有一个商家没有被选中，全选复选框就变成未选中状态，并且结束循环
                        if (!listShopper.isChecked()) {
                            isAllShopperChecked = false;
                            break;
                        }
                    }
                    cbTotal.setChecked(isAllShopperChecked);
                }

                // 一级条目发生变化时，计算一下总价
                calculatePrice();
            }
        });


        adapter.setOnAddDecreaseProductListener(new ProductAdapter.OnAddDecreaseProductListener() {
            @Override
            public void onChange(int position, int num) {
                calculatePrice();
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvShoper.setLayoutManager(layoutManager);
        rvShoper.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvShoper.setAdapter(adapter);

        presenter = new CartPresenter();
        presenter.attach(this);
        presenter.getData();
    }

    // 计算商品总价
    private void calculatePrice() {
        // 遍历商家
        float totalPrice = 0;
        for (Shopper<List<Product>> listShopper : list) {
            // 遍历商家的商品
            List<Product> list = listShopper.getList();
            for (Product product : list) {
                // 如果商品被选中
                if (product.isChecked()) {
                    totalPrice += product.getNum() * product.getPrice();
                }
            }
        }

        txtPrice.setText("总价：" + totalPrice);

    }

    private void initView() {
        txtEditFinish = findViewById(R.id.txt_edit_or_finish);
        cbTotal = findViewById(R.id.cb_total_select);
        txtPrice = findViewById(R.id.txt_total_price);
        btnCalu = findViewById(R.id.btn_calu);
        rvShoper = findViewById(R.id.rv_shopper);
    }

    @Override
    public void success(MessageBean<List<Shopper<List<Product>>>> data) {
        if (data != null) {
            // 获取商家列表
            List<Shopper<List<Product>>> shoppers = data.getData();
            if (shoppers != null) {
                list.clear();
                list.addAll(shoppers);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void failed(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
    }
}
