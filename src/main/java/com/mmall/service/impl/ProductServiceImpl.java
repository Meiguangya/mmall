package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService categoryService;

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
        if (result > 0) {
            return ServerResponse.createBySuccessData("更新成功");
        }
        return ServerResponse.createByErrorMsg("更新失败");
    }

    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByError();
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMsg("产品不存在或已经被删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccessData(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setSubImages(product.getSubtitle());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setStatus(product.getStatus());

        //imageHost
        productDetailVo.setImageHost(PropertiesUtil.getProperty(Const.FTP_PREFIX, "http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category.getParentId() == null) {
            productDetailVo.setParentCategoryId(0);
        }
        productDetailVo.setParentCategoryId(category.getParentId());

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        //1.start
        PageHelper.startPage(pageNum, pageSize);
        //2.sql
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            productListVoList.add(assembleProductListVo(product));
        }
        //pageHelper
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccessData(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setImageHost(PropertiesUtil.getProperty(Const.FTP_PREFIX, "http://img.happymmall.com/"));

        return productListVo;
    }

    public ServerResponse<PageInfo> searchProductByNameOrId(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameOrId(productName, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            productListVoList.add(assembleProductListVo(product));
        }
        //pageHelper
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccessData(pageResult);
    }

    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMsg("产品不存在");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMsg("产品不在线");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccessData(productDetailVo);
    }

    public ServerResponse<PageInfo> getProductByKeyWordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //没有该分类,没有关键字,返回空集合
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccessData(pageInfo);
            }
            categoryIdList = categoryService.getCategoryAndChildrenById(categoryId).getData();
        }
        if (!StringUtils.isBlank(keyword)) {
            keyword = new StringBuilder("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum, pageSize);
        //排序处理
        if (!StringUtils.isBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderArr = orderBy.split("_");
                PageHelper.orderBy(orderArr[0] + " " + orderArr[1]);
            }
        }
        List<Product> productList = productMapper.selectBykeyworAndCategory(
                StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            productListVoList.add(assembleProductListVo(product));
        }
        //pageHelper
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccessData(pageResult);
    }

}
