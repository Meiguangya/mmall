package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 后台分类管理
 */
@Controller
@RequestMapping(value = "/manage/category")
public class CategoryManagerController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    /**
     * 添加分类
     * @param session
     * @param categoryName
     * @param parentedId
     * @return
     */
    @RequestMapping(value = "add_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") Integer parentedId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        ServerResponse response = userService.isAdmin(user);
        if(response.isSuccess()){
            return categoryService.addCategory(categoryName,parentedId);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员,没有权限");
        }
    }

    /**
     * 修改分类名称
     * @param session
     * @param categoryName
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "set_category_name.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,String categoryName,Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        ServerResponse response = userService.isAdmin(user);
        if(response.isSuccess()){
            return categoryService.updateCategory(categoryName,categoryId);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员,没有权限");
        }
    }

    @RequestMapping(value = "get_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenParallerCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        ServerResponse response = userService.isAdmin(user);
        if(response.isSuccess()){
            return categoryService.getChildrenParallerCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员,没有权限");
        }
    }

    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        ServerResponse response = userService.isAdmin(user);
        if(response.isSuccess()){
            return categoryService.getCategoryAndChildrenById(categoryId);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员,没有权限");
        }
    }


}
