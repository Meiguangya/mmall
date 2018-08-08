package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Random;

@Controller
@RequestMapping(value = "/manage/product")
public class ProductManagerController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IProductService productService;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        if (userService.isAdmin(user).isSuccess()) {
            return productService.productSaveorUpdate(product);
        } else {
            return ServerResponse.createByErrorMsg("用户不是管理员");
        }
    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        if (userService.isAdmin(user).isSuccess()) {
            return productService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMsg("用户不是管理员");
        }
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        if (userService.isAdmin(user).isSuccess()) {
            return productService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMsg("用户不是管理员");
        }
    }

    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        if (userService.isAdmin(user).isSuccess()) {
            return productService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMsg("用户不是管理员");
        }
    }

    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getList(HttpSession session, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        if (userService.isAdmin(user).isSuccess()) {
            return productService.searchProductByNameOrId(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMsg("用户不是管理员");
        }
    }


}
