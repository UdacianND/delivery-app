package uz.md.shopapp.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.md.shopapp.client.SmsSender;
import uz.md.shopapp.client.requests.LoginRequest;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class MyJob {

  @Value("${app.sms.sender-email}")
  private String senderEmail;

  @Value("${app.sms.sender-password}")
  private String senderPassword;

  private final SmsSender smsSender;

  @Scheduled(fixedDelay = 1000000000)
  public void execute() {
    log.info("login to sms sender service ");
    smsSender.login(LoginRequest
            .builder()
            .email(senderEmail)
            .password(senderPassword)
            .build());
  }
}
