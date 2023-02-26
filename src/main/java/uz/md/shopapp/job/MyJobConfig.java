package uz.md.shopapp.job;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class MyJobConfig {

  @Bean
  public JobDetailFactoryBean jobDetail() {
    JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
    jobDetailFactory.setJobClass(MyJob.class);
    jobDetailFactory.setDescription(" SMS sender token getter job in every 25 days ");
    jobDetailFactory.setDurability(true);
    return jobDetailFactory;
  }

  @Bean
  public CronTriggerFactoryBean cronTrigger(JobDetail jobDetail) {
    CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
    trigger.setJobDetail(jobDetail);
    trigger.setStartDelay(3000);
    trigger.setName("MyJobTrigger");
    trigger.setMisfireInstructionName("MISFIRE_INSTRUCTION_DO_NOTHING");
    trigger.setCronExpression("0 0 0 */25 * ?"); // Every 25-day of month
    return trigger;
  }
}
