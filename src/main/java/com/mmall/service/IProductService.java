package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

public interface IProductService {

    /**
     * 添加或者更新商品
     *
     * @param product
     * @return
     */
    public ServerResponse productSaveorUpdate(Product product);

    /**
     * 设置产品销售状态
     * @param productId
     * @param status
     * @return
     */
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    /**
     * 产品详细信息
     * @param productId
     * @return
     */
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    /**
     * 分页查询列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    /**
     * 分页搜索
     *
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> searchProductByNameOrId(String productName, Integer productId, int pageNum, int pageSize);

    /**
     * 查看商品详情
     * @param productId
     * @return
     */
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    /**
     * 根据关键字或者分类进行排序搜索
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    public ServerResponse<PageInfo> getProductByKeyWordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
