package cc.mrbird.febs;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@MapperScan("cc.mrbird.febs.*.mapper")
public class FebsApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(FebsApplication.class,args);
    }
    @Override
    protected  SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(FebsApplication.class);
    }
}
