package com.pis.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pis.gmall.bean.OmsCartItem;
import com.pis.gmall.bean.PmsSkuInfo;
import com.pis.gmall.service.CartService;
import com.pis.gmall.service.SkuService;
import com.pis.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {

    @Reference
    SkuService skuService;
    @Reference
    CartService cartService;

    public String checkCart(HttpServletRequest request, String skuId,String isChecked, String memberId,ModelMap modelMap){

        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setIsChecked(isChecked);
        cartService.checkCart(omsCartItem);
        List<OmsCartItem> omsCartItemList = cartService.getCartList(memberId);
        modelMap.put("cartList",omsCartItemList);

        BigDecimal totalPrice = getTotalPrice(omsCartItemList);
        modelMap.put("totalPrice",totalPrice);

        return "cartListInner";
    }

    public String cartList(HttpServletRequest request, ModelMap modelMap){
        List<OmsCartItem> omsCartItemList = new ArrayList<>();
        String memberId = (String) request.getAttribute("memberId");
        String nickName = (String) request.getAttribute("nickName");


        if(StringUtils.isNotBlank(memberId)){
            //已经登录
            omsCartItemList = cartService.getCartList(memberId);
        }else {
            //未登录就从cookie中获取omsCartItemList
            //查询cookie
            String cookie = CookieUtil.getCookieValue(request,"cartListCookie",true);
            if(StringUtils.isNotBlank(cookie)){
                //cookie不为空
                omsCartItemList = JSON.parseArray(cookie,OmsCartItem.class);
            }else{
                //cookie为空
                //跳转 快去添加商品页面
            }
        }
        for (OmsCartItem omsCartItem : omsCartItemList) {
            omsCartItem.setPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
        }
        modelMap.put("cartList",omsCartItemList);
        //计算商品总额
        BigDecimal totalAmount = getTotalPrice(omsCartItemList);
        modelMap.put("totalAmount",totalAmount);
        return "cartList";
    }

    private BigDecimal getTotalPrice(List<OmsCartItem> omsCartItemList) {
        BigDecimal total = new BigDecimal("0");
        for (OmsCartItem omsCartItem : omsCartItemList) {
            if (omsCartItem.getIsChecked().equals("1"))
            total = total.add(omsCartItem.getTotalPrice());
        }

        return total;
    }

    @RequestMapping("addToCart")
    public String addToCart(String skuId, int quantity, HttpServletRequest request, HttpServletResponse response){
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        //根据skuId调用skuService商品详情信息
        PmsSkuInfo skuInfo = skuService.getSkuById(skuId);
        //将商品详情信息封装成购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getProductId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("11111111111");
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(new BigDecimal(quantity));

        //判断用户是否登录

        //根据用户登录决定是否走cookie的分支还是db
        String memberId = (String) request.getAttribute("memberId");
        String nickName = "";
        if(StringUtils.isNotBlank(memberId)) {
            //用户没有登录

            String cookie = CookieUtil.getCookieValue(request,"cartListCookie",true);
            if(StringUtils.isNotBlank(cookie)){
                //cookie为空
                omsCartItems.add(omsCartItem);
            }else {
                //cookie不为空
                boolean exist = if_cart_exist(omsCartItem,omsCartItems);
                if(exist){
                    //购物车里有该商品，则数量+1
                    for (OmsCartItem cartItem : omsCartItems) {
                        if(cartItem.getProductSkuId()== omsCartItem.getProductSkuId()){
                            cartItem.setQuantity(cartItem.getQuantity().add(omsCartItem.getQuantity()));
                        }
                    }
                    //否则，就在原有的购物车加入该商品，
                    omsCartItems.add(omsCartItem);
                }
            }
            //并更新cookie
            CookieUtil.setCookie(request,response,"cartListCookie", JSON.toJSONString(omsCartItems),60*60*72,true);

        }else {
            //用户已经登录
            OmsCartItem omsCartItemFromDb = cartService.ifUserCartExistThisCartItem(memberId,skuId);
            if(omsCartItemFromDb == null){
                //购物车无此商品\
                omsCartItem.setMemberId(memberId);
                omsCartItem.setMemberNickname("test小明");
                omsCartItem.setQuantity(new BigDecimal(quantity));
                cartService.addToCart(omsCartItem);
            }else {
                //购物车有此商品
                omsCartItemFromDb.setQuantity(omsCartItemFromDb.getQuantity().add(omsCartItem.getQuantity()));
                cartService.updateCart(omsCartItemFromDb);
            }
            //更新缓存
            cartService.synCartCache(memberId);
        }
        return "/success.html";
    }

    private boolean if_cart_exist(OmsCartItem omsCartItem, List<OmsCartItem> omsCartItems) {
        boolean b = false;
        for (OmsCartItem cartItem : omsCartItems) {
            if(omsCartItem.getProductSkuId() == cartItem.getProductSkuId()) {
                b = true;
            }
        }
        return b;
    }
}
