package com.change.config;

import com.change.bean.AlipayTranDO;
import com.change.bean.HopPayTranDO;
import com.change.listener.AlipaySkipListener;
import com.change.processor.AlipayItemProcessor;
import com.change.processor.AlipayValidateProcessor;
import com.change.reader.AlipayDBItemReader;
import com.change.reader.AlipayFileItemReader;
import com.change.writer.AlipayDBChangeItemWriter;
import com.change.writer.AlipayDBItemWriter;
import com.change.writer.AlipayFileItemWriter;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 批处理配置中心
 * https://www.jianshu.com/p/260159a0681b
 *
 * @author: feiweiwei
 * @description:
 * @created Date: 13:36 17/11/28.
 * @modify by:
 */

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Autowired
    public JobLauncher jobLauncher;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AlipayFileItemReader alipayFileItemReader;

    @Autowired
    private AlipayDBItemReader alipayDBItemReader;

    @Autowired
    private AlipayItemProcessor alipayItemProcessor;

    @Autowired
    private AlipayFileItemWriter alipayFileItemWriter;

    @Autowired
    private AlipayDBItemWriter alipayDBItemWriter;

    @Autowired
    private AlipayDBChangeItemWriter alipayDBChangeItemWriter;

    @Autowired
    private AlipaySkipListener listener;

    /**
     * 运行方法
     */
    public void run() {
        try {
            String dateParam = new Date().toString();
            JobParameters param = new JobParametersBuilder().addString("date", dateParam).toJobParameters();
            System.out.println(dateParam);
            //执行job
            JobExecution execution = jobLauncher.run(importAliJob(), param);
            System.out.println("Exit Status : " + execution.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public Job importAliJob() {
        return jobBuilderFactory.get("importAliJob")
                .incrementer(new RunIdIncrementer())
                .flow(step5())
//				.next(step2())
                .end()
                .build();
    }

    /**
     * 执行step,包括读数据,处理数据,写数据
     * db-->txt
     *
     * @return 处理后数据输出
     */
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<AlipayTranDO, HopPayTranDO>chunk(10)
                .reader(alipayDBItemReader.getAlipayDBItemReader())
                .processor(alipayItemProcessor)
                .writer(alipayFileItemWriter.getAlipayItemWriter())
                .build();
    }

    /**
     * 执行step,包括读数据,处理数据,写数据
     * db-->db
     *
     * @return 处理后数据输出
     */
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .<AlipayTranDO, AlipayTranDO>chunk(10)
                .reader(alipayDBItemReader.getAlipayDBItemReader())
                .writer(alipayDBItemWriter)
                .faultTolerant()
                .skipLimit(20)
                .skip(Exception.class)
                .listener(listener)
                .retryLimit(3)
                .retry(RuntimeException.class)
                .build();
    }

    /**
     * 数据校验及数据处理
     * 执行step,包括读数据,处理数据,写数据
     * txt-->txt
     *
     * @return 处理后数据输出
     */
    @Bean
    public Step step3() {
        CompositeItemProcessor<AlipayTranDO, HopPayTranDO> compositeItemProcessor = new CompositeItemProcessor<AlipayTranDO, HopPayTranDO>();
        List compositeProcessors = new ArrayList();
        compositeProcessors.add(new AlipayValidateProcessor());
        compositeProcessors.add(new AlipayItemProcessor());
        compositeItemProcessor.setDelegates(compositeProcessors);
        return stepBuilderFactory.get("step3")
                .<AlipayTranDO, HopPayTranDO>chunk(10)
                .reader(alipayFileItemReader.getMultiAliReader())
                .processor(compositeItemProcessor)
                .writer(alipayFileItemWriter.getAlipayItemWriter())
                .build();
    }

    /**
     * 数据校验及数据处理,将数据库中数据处理后倒入另外一张表中
     * 执行step,包括读数据,处理数据,写数据
     * db-->db
     *
     * @return 处理后数据输出
     */
    @Bean
    public Step step5() {
        CompositeItemProcessor<AlipayTranDO, HopPayTranDO> compositeItemProcessor = new CompositeItemProcessor<AlipayTranDO, HopPayTranDO>();
        List compositeProcessors = new ArrayList();
        compositeProcessors.add(new AlipayValidateProcessor());
        compositeProcessors.add(new AlipayItemProcessor());
        compositeItemProcessor.setDelegates(compositeProcessors);
        return stepBuilderFactory.get("step5")
                .<AlipayTranDO, HopPayTranDO>chunk(10)
                .reader(alipayDBItemReader.getAlipayDBItemReader())
                .processor(compositeItemProcessor)
                .writer(alipayDBChangeItemWriter)
                .build();
    }

    /**
     * 并行配置
     *
     * @return 配置文件
     */
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    /**
     * 并行step
     * 执行step,包括读数据,处理数据,写数据
     * txt-->txt
     *
     * @return 处理后数据输出
     */
    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
                .<AlipayTranDO, HopPayTranDO>chunk(10)
                .reader(alipayFileItemReader.getMultiAliReader())
                .processor(alipayItemProcessor)
                .writer(alipayFileItemWriter.getAlipayItemWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(4)
                .build();
    }

}
