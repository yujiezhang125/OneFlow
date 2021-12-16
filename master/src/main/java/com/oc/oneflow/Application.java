package com.oc.oneflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.oc.oneflow.common.utils.ConfigUtil;
import com.oc.oneflow.model.ConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.List;


@SpringBootApplication
public class Application {
    @Autowired
    private JdbcTemplate hiveJdbcTemplate;
    @Autowired
    private ConfigUtil configUtil;

    private static final Logger appLogger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void run() throws FileNotFoundException {
        ConfigVO configVO = configUtil.getConfigVO();
        List<ConfigVO.DataSource> dataSources = configVO.getDataSources();
        System.out.println(dataSources.get(0).getName());
        // System.out.println(configVO.getProjectId());
        // System.out.println(configVO.getProjectName());
        configVO.getTasks().forEach( taskVO -> {
            String taskId = taskVO.getTaskId();
            String taskName = taskVO.getTaskName();
            String cron = taskVO.getCron();
            taskVO.getSteps().forEach(stepVO -> {
                System.out.println(taskId);
                String type = stepVO.getType();
                if (type.equals("hive")) {
                    System.out.println("This is hive");
                    appLogger.info("select * from employee");
                    hiveJdbcTemplate.execute("select * from emplyee");
                    appLogger.info("done");
                }
            });
        });
    }
}
