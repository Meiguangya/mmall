package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    public ServerResponse productSaveorUpdate(Product product) {
        if (product == null) {
            return ServerResponse.createByErrorMsg("参数不正确");
        }
        if (StringUtils.isBlank(product.getSubImages())) {
            String[] subImgArr = product.getSubImages().split(",");
            product.setMainImage(subImgArr[0]);
        }
        if (product.getId() != null) {
            int result = productMapper.updateByPrimaryKey(product);
            if (result > 0) {
                return ServerResponse.createBySuccessMsg("更新商品成功");
            }
            return ServerResponse.createByErrorMsg("更新商品失败");
        } else {
            int result = productMapper.insert(product);
            if (result > 0) {
                return ServerResponse.createBySuccessMsg("添加商品成功");
            }
            return ServerResponse.createByErrorMsg("添加商品失败");
        }
    }

    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int result = productMapper.updateByPrimaryKeySelective(product);
        if(result>0){
            return ServerResponse.createBySuccessData("更新成功");
        }
        return ServerResponse.createByErrorMsg("更新失败");
    }
}
