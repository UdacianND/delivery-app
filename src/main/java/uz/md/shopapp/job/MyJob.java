package uz.md.shopapp.job;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.md.shopapp.client.SmsSender;
import uz.md.shopapp.client.requests.LoginRequest;

@Component
@RequiredArgsConstructor
public class MyJob implements Job {

  @Value("${app.sms.sender-email}")
  private String senderEmail;

  @Value("${app.sms.sender-password}")
  private String senderPassword;

  private final SmsSender smsSender;

  @Override
  public void execute(JobExecutionContext context) {
    System.out.println("context.getJobDetail() = " + context.getJobDetail());
    smsSender.login(LoginRequest
            .builder()
            .email(senderEmail)
            .password(senderPassword)
            .build());
  }
}
