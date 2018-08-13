package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    private org.slf4j.Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public ServerResponse<Category> addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMsg("添加分类参数错误");
        }

        Category category = new Category();

        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int result = categoryMapper.insert(category);
        if (result > 0) {
            return ServerResponse.createBySuccessMsg("添加分类成功");
        }
        return ServerResponse.createByError();

    }

    public ServerResponse<Category> updateCategory(String categoryName, Integer categoryId) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMsg("更新分类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int result = categoryMapper.updateByPrimaryKeySelective(category);
        if (result > 0) {
            return ServerResponse.createBySuccessMsg("更新成功");
        } else {
            return ServerResponse.createByErrorMsg("更新失败");
        }
    }

    public ServerResponse getChildrenParallerCategory(Integer parentId) {
        List<Category> list = categoryMapper.qryChildParallerCategory(parentId);
        if (CollectionUtils.isEmpty(list)) {
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccessData(list);
    }

    public ServerResponse<List<Integer>> getCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        getChildCategory(categorySet,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        for(Category category : categorySet){
            categoryIdList.add(category.getId());
        }
        return ServerResponse.createBySuccessData(categoryIdList);
    }

    private Set<Category> getChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.qryChildParallerCategory(categoryId);
        for(Category categoryItem : categoryList){
            getChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }

}
