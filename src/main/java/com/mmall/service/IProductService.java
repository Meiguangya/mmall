package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

public interface IProductService {

    /**
     * 添加或者更新商品
     * @param product
     * @return
     */
    public ServerResponse productSaveorUpdate(Product product);

    public ServerResponse<String> setSaleStatus(Integer productId, Integer status);
}
