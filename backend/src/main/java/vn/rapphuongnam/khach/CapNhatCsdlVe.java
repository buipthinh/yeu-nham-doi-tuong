package vn.rapphuongnam.khach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

//Chay 1 lan khi khoi dong. Bang Ve co rang buoc UNIQUE(gheSuatChieuId): ghe cua don da huy/het han van con dong Ve,
//nen khong ban lai duoc cho khach khac. Bo rang buoc nay; viec moi ghe chi thuoc 1 don con hieu luc
//da do GheSuatChieu.trangThai + donHangId dam bao.
@Component
public class CapNhatCsdlVe implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(CapNhatCsdlVe.class);
    private final JdbcTemplate jdbc;

    public CapNhatCsdlVe(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            Integer co = jdbc.queryForObject("SELECT COUNT(*) FROM sys.objects WHERE name = 'UQ_Ve_gheSuatChieu' AND type = 'UQ'", Integer.class);
            if (co != null && co > 0) {
                jdbc.execute("ALTER TABLE dbo.Ve DROP CONSTRAINT UQ_Ve_gheSuatChieu");
                jdbc.execute("CREATE INDEX IX_Ve_gheSuatChieu ON dbo.Ve(gheSuatChieuId)");
                log.info("Da bo rang buoc UQ_Ve_gheSuatChieu de ghe cua don huy/het han ban lai duoc");
            }
        } catch (Exception e) {
            log.warn("Khong cap nhat duoc bang Ve: {}", e.getMessage());
        }
    }
}
