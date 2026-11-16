package vn.rapphuongnam.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vn.rapphuongnam.dto.PhimDto;
import vn.rapphuongnam.entity.TrangThaiPhim;
import vn.rapphuongnam.service.PhimService;

import java.util.List;

//REST API cho trang khach hang (frontend JS), khong tra phim dang An
@RestController
@RequestMapping("/api/phim")
public class PhimApiController {
    private final PhimService phimService;

    public PhimApiController(PhimService phimService) {
        this.phimService = phimService;
    }

    //GET /api/phim?trangThai=dangChieu  hoac  sapChieu
    @GetMapping
    public List<PhimDto> danhSach(@RequestParam(defaultValue = "dangChieu") TrangThaiPhim trangThai) {
        if (trangThai == TrangThaiPhim.an) return List.of();
        return phimService.phimTheoTrangThai(trangThai).stream().map(PhimDto::tu).toList();
    }

    //GET /api/phim/5
    @GetMapping("/{id}")
    public PhimDto chiTiet(@PathVariable Integer id) {
        var p = phimService.layTheoId(id);
        if (p.getTrangThai() == TrangThaiPhim.an) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return PhimDto.tu(p);
    }
}
