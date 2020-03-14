package org.sea.spring.boot.batch.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sea.spring.boot.batch.SpringBootBathApplication;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringBootBathApplication.class) 
@Slf4j
public class TestBatchService {

 
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job importJob;

    @Test
    public void testBatch1() throws Exception {
    	StopWatch watch = new StopWatch("testAdd1");
    	watch.start("保存");
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                
                .toJobParameters();
        jobLauncher.run(importJob, jobParameters);
        watch.stop();
        log.info(watch.prettyPrint());
    }
}
