package vn.rapphuongnam.khach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

//UC11 tac vu nen: moi 30 giay tra cac ghe giu qua han ve trang thai trong
@Configuration
@EnableScheduling
public class GiaiPhongGheTask {
    private static final Logger log = LoggerFactory.getLogger(GiaiPhongGheTask.class);
    private final DatVeKhachService service;

    public GiaiPhongGheTask(DatVeKhachService service) {
        this.service = service;
    }

    @Scheduled(fixedDelay = 30_000, initialDelay = 10_000)
    public void chay() {
        try {
            int n = service.giaiPhongQuaHan();
            if (n > 0) log.info("UC11: chuyen {} don qua han sang Het han", n);
        } catch (Exception e) {
            log.warn("UC11 loi: {}", e.getMessage());
        }
    }
}
