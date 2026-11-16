package vn.rapphuongnam.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Trang goc: tam thoi chuyen thang toi quan ly phim
@Controller
public class AdminController {
    @GetMapping({"/", "/admin"})
    public String trangChu() {
        return "redirect:/admin/phim";
    }
}
