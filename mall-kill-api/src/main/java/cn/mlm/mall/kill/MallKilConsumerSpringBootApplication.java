package cn.mlm.mall.kill;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/2 15:22
 */
@SpringBootApplication
@EnableDubbo
public class MallKilConsumerSpringBootApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder createSpringApplicationBuilder() {
        return new SpringApplicationBuilder(MallKilConsumerSpringBootApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MallKilConsumerSpringBootApplication.class, args);
    }

}
