package cn.mlm.mall.kill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.*;

/**
 * 定时任务多线程处理的通用化配置
 *
 * @author linSir
 */
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    /**
     * Springboot定时任务默认是由是单个线程串行调度所有任务，
     * 当定时任务很多的时候，为了提高任务执行效率，避免任务之间互相影响，
     * 可以采用并行方式执行定时任务。定义一个ScheduleConfig配置类并实现SchedulingConfigurer接口，
     * 重写configureTasks方法，配置任务调度线程池。
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 配置线程池大小，根据任务数量定制
        taskScheduler.setPoolSize(10);
        // 线程名称前缀
        taskScheduler.setThreadNamePrefix("spring-task-scheduler-thread-");
        // 线程池关闭前最大等待时间，确保最后一定关闭
        taskScheduler.setAwaitTerminationSeconds(60);
        // 线程池关闭时等待所有任务完成
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 任务丢弃策略
        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return taskScheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskScheduler());
    }
}
