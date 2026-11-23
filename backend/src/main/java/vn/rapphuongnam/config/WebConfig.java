package vn.rapphuongnam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Cho phep frontend (Live Server cong 5500 hoac mo file truc tiep) goi API. Khach xac thuc bang token nen khong can cookie
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    //Trang quan ly phai dang nhap (UC1.2); trang dang nhap va file css thi khong
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DangNhapInterceptor())
                .addPathPatterns("/admin", "/admin/**")
                .excludePathPatterns("/admin/dang-nhap", "/admin/dang-xuat", "/admin/*.css", "/admin/*.js");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:../frontend/uploads/", "file:frontend/uploads/");
    }
}
