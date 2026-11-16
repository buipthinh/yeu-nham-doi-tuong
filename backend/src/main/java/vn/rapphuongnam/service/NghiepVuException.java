package vn.rapphuongnam.service;

//Loi vi pham quy tac nghiep vu, noi dung hien thang cho nguoi dung
public class NghiepVuException extends RuntimeException {
    public NghiepVuException(String message) {
        super(message);
    }
}
