package com.storeTemplate.cart.service;

import com.storeTemplate.cart.vo.Cart;
import com.storeTemplate.cart.vo.CartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName CartService
 * @Description TODO
 * @Author 13011
 * @Date 2021/1/17 18:21
 * @Version 1.0
 **/
public interface CartService {
    /**
     * 将商品添加到购物车
     */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 获取购物车中某个购物项
     */
    CartItem getCartItem(Long skuId);

    /**
     * 获取整个购物车
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车
     */
    void clearCart(String cartKey);

    /**
     * 勾选购物项
     */
    void checkItem(Long skuId, Integer check);

    /**
     * 改变购物车中物品的数量
     */
    void changeItemCount(Long skuId, Integer num);

    /**
     * 删除购物项
     */
    void deleteItem(Long skuId);

    /**
     * 结账
     */
    BigDecimal toTrade() throws ExecutionException, InterruptedException;

    List<CartItem> getUserCartItems();
}
