/*===================================================================================
 *                    Copyright(c) 2019 POSCO
 *
 * Project            : test-boot
 * Source File Name   : mzc.test.SampleApp.java
 * Description        :
 * Author             : leedh
 * Version            : 1.0.0
 * File Name related  :
 * Class Name related :
 * Created Date       : 2019. 7. 11.
 * Updated Date       : 2019. 7. 11.
 * Last modifier      : leedh
 * Updated content    : 최초작성
 *
 *==================================================================================*/
package mzc.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * SampleApp.java
 *
 * @author leedh
 * @version 1.0.0
 * @since 2019. 7. 11.
 */
@SpringBootApplication
@EnableAsync
@ComponentScan
@Configuration
@EnableAutoConfiguration
public class AppStarter {
    public static void main( String[] args ) {
    	SpringApplication.run(AppStarter.class, args);
    }
}
