package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    public ServerResponse<Category> addCategory(String categoryName, Integer parentedId);

    public ServerResponse<Category> updateCategory(String categoryName,Integer categoryId);

    /**
     * 获取子节点 平级
     * @param parentId
     * @return
     */
    public ServerResponse getChildrenParallerCategory(Integer parentId);

    /**
     * 递归查询分类的子节点
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> getCategoryAndChildrenById(Integer categoryId);
}
