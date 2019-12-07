package cn.mlm.mall.kill;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author linSir
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/2 14:29
 */
@SpringBootApplication
@EnableDubbo
@EnableScheduling
public class MallKillProvideSpringBootApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder createSpringApplicationBuilder() {

        return new SpringApplicationBuilder(MallKillProvideSpringBootApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MallKillProvideSpringBootApplication.class, args);
    }
}
