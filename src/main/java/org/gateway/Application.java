package org.gateway;

import java.io.File;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * 
 * @author admin
 * @since 2019-06-11 15:49
 *
 */

@ImportResource({ "classpath:*.xml" })
@ServletComponentScan
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // SpringBoot1.4已上版本只能找classpath中的log4j配置文件，这里做个兼容处理
        String log4jFile = System.getProperty("user.dir")+File.separator+"log4j.properties";
        File file = new File(log4jFile);
        if (file.exists()) {
            PropertyConfigurator.configure(log4jFile);
        }
        // run application
        SpringApplication.run(Application.class, args);
    }

}
