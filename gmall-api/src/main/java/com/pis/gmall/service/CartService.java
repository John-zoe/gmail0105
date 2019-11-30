package com.pis.gmall.service;

import com.pis.gmall.bean.OmsCartItem;

import java.io.Serializable;
import java.util.List;

public interface CartService extends Serializable {

    OmsCartItem ifUserCartExistThisCartItem(String memberId, String skuId);

    void addToCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemFromDb);

    void synCartCache(String memberId);

    List<OmsCartItem> getCartList(String memberId);

    void checkCart(OmsCartItem omsCartItem);
}
