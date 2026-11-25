package vn.rapphuongnam.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Trang goc: mo trang Tong quan (nhan vien se duoc chuyen sang Tra cuu ve)
@Controller
public class AdminController {
    @GetMapping({"/", "/admin"})
    public String trangChu() {
        return "redirect:/admin/tong-quan";
    }
}
