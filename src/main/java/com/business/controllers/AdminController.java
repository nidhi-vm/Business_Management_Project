package com.business.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.business.basiclogics.Logic;
import com.business.entities.Admin;
import com.business.entities.Orders;
import com.business.entities.Product;
import com.business.entities.User;
import com.business.loginCredentials.AdminLogin;
import com.business.loginCredentials.UserLogin;
import com.business.services.AdminServices;
import com.business.services.OrderServices;
import com.business.services.ProductServices;
import com.business.services.UserServices;

import jakarta.validation.Valid;

@Controller
public class AdminController {
    private static final String REDIRECT_ADMIN_SERVICES = "redirect:/admin/services";
    private static final String LOGIN_VIEW = "Login";
    private static final String PRODUCT_UNAVAILABLE_MESSAGE = "SORRY...!  Product Unavailable";
    private static final String ADMIN_PAGE_VIEW = "Admin_Page";
    private static final String ADD_ADMIN_VIEW = "Add_Admin";
    private static final String UPDATE_ADMIN_VIEW = "Update_Admin";
    private static final String ADD_PRODUCT_VIEW = "Add_Product";
    private static final String UPDATE_PRODUCT_VIEW = "Update_Product";
    private static final String ADD_USER_VIEW = "Add_User";
    private static final String UPDATE_USER_VIEW = "Update_User";
    private static final String ORDER_SUCCESS_VIEW = "Order_success";
    private static final String BUY_PRODUCT_VIEW = "BuyProduct";
    
    @Autowired
    private UserServices services;
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private ProductServices productServices;    
    @Autowired
    private OrderServices orderServices;

    private String email;
    private User currentUser;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    // Validating login 
    @GetMapping("/adminLogin")
    public String getAllData(@ModelAttribute("adminLogin") AdminLogin login, Model model) {
        String email = login.getEmail();
        String password = login.getPassword();
        if (adminServices.validateAdminCredentials(email, password)) {
            return REDIRECT_ADMIN_SERVICES;
        } else {
            model.addAttribute("error", "Invalid email or password");
            return LOGIN_VIEW;
        }
    }

    @GetMapping("/userlogin")
    public String userLogin(@ModelAttribute("userLogin") UserLogin login, Model model) {
        email = login.getUserEmail();
        String password = login.getUserPassword();
        if (services.validateLoginCredentials(email, password)) {
            currentUser = this.services.getUserByEmail(email);
            List<Orders> orders = this.orderServices.getOrdersForUser(currentUser);
            model.addAttribute("orders", orders);
            model.addAttribute("name", currentUser.getUname());
            return BUY_PRODUCT_VIEW;
        } else {
            model.addAttribute("error2", "Invalid email or password");
            return LOGIN_VIEW;
        }
    }

    // Searching Product By Name
    @PostMapping("/product/search")
    public String searchHandler(@RequestParam("productName") String name, Model model) {
        Product product = this.productServices.getProductByName(name);
        if (product == null) {
            model.addAttribute("message", PRODUCT_UNAVAILABLE_MESSAGE);
            model.addAttribute("product", product);
            List<Orders> orders = this.orderServices.getOrdersForUser(currentUser);
            model.addAttribute("orders", orders);
            return BUY_PRODUCT_VIEW;
        }
        List<Orders> orders = this.orderServices.getOrdersForUser(currentUser);
        model.addAttribute("orders", orders);
        model.addAttribute("product", product);
        return BUY_PRODUCT_VIEW;
    }

    // Providing services 
    @GetMapping("/admin/services")
    public String returnBack(Model model) {
        List<User> users = this.services.getAllUser();
        List<Admin> admins = this.adminServices.getAll(); 
        List<Product> products = this.productServices.getAllProducts();
        List<Orders> orders = this.orderServices.getOrders();
        model.addAttribute("users", users);
        model.addAttribute("admins", admins);
        model.addAttribute("products", products);
        model.addAttribute("orders", orders);

        return ADMIN_PAGE_VIEW;
    }

    // Invoking addAdmin Page
    @GetMapping("/addAdmin")
    public String addAdminPage() {
        return ADD_ADMIN_VIEW;
    }

    // Handling AddAdmin
    @PostMapping("addingAdmin")
    public String addAdmin(@ModelAttribute Admin admin) {
        this.adminServices.addAdmin(admin);
        return REDIRECT_ADMIN_SERVICES;
    }

    // Invoking updateAdmin Page
    @GetMapping("/updateAdmin/{adminId}")
    public String update(@PathVariable("adminId") int id, Model model) {
        Admin admin = this.adminServices.getAdmin(id);
        model.addAttribute("admin", admin);
        return UPDATE_ADMIN_VIEW;
    }

    // Handling Update Page
    @GetMapping("/updatingAdmin/{id}")
    public String updateAdmin(@ModelAttribute Admin admin, @PathVariable("id") int id) {
        this.adminServices.update(admin, id);
        return REDIRECT_ADMIN_SERVICES;
    }

    // Handling delete operation
    @GetMapping("/deleteAdmin/{id}")
    public String deleteAdmin(@PathVariable("id") int id) {
        this.adminServices.delete(id);
        return REDIRECT_ADMIN_SERVICES;
    }

    // Invoking AddProduct Page
    @GetMapping("/addProduct")
    public String addProduct() {
        return ADD_PRODUCT_VIEW;
    }

    // Invoking Update Product Page
    @GetMapping("/updateProduct/{productId}")
    public String updateProduct(@PathVariable("productId") int id, Model model) {
        Product product = this.productServices.getProduct(id);
        logger.info("Product details: {}", product);
        model.addAttribute("product", product);
        return UPDATE_PRODUCT_VIEW;
    }

    // Invoking AddUser Page
    @GetMapping("/addUser")
    public String addUser() {
        return ADD_USER_VIEW;
    }

    // Invoking UpdateUser Page
    @GetMapping("/updateUser/{userId}")
    public String updateUserPage(@PathVariable("userId") int id, Model model) {
        User user = this.services.getUser(id);
        model.addAttribute("user", user);
        return UPDATE_USER_VIEW;
    }

    // Placing Order
    @PostMapping("/product/order")
    public String orderHandler(@ModelAttribute() Orders order, Model model) {
        double totalAmount = Logic.countTotal(order.getoPrice(), order.getoQuantity());
        order.setTotalAmmout(totalAmount);
        order.setUser(currentUser);
        Date d = new Date();
        order.setOrderDate(d);
        this.orderServices.saveOrder(order);
        model.addAttribute("amount", totalAmount);
        return ORDER_SUCCESS_VIEW;
    }

    @GetMapping("/product/back")
    public String back(Model model) {
        List<Orders> orders = this.orderServices.getOrdersForUser(currentUser);
        model.addAttribute("orders", orders);
        return BUY_PRODUCT_VIEW;
    }
}