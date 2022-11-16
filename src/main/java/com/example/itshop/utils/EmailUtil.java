package com.example.itshop.utils;

import com.example.itshop.dto.common.AuthEmailDto;
import com.example.itshop.properties.EmailProperty;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Log4j2
public class EmailUtil {
	
	private final HttpUtil httpUtil;
	private final EmailProperty emailProperty;
	private SendGrid sendGrid;
	
	@PostConstruct
	public SendGrid prepareSendGrid() {
		if (Objects.isNull(sendGrid)) {
			sendGrid = new SendGrid(emailProperty.getApiKey());
		}
		return sendGrid;
	}
	
	@Async
	public void sendVerificationEmail(AuthEmailDto dto) {
		Email from = new Email("6051071126+ITShop@st.utc2.edu.vn");
		Email to = new Email(dto.getToEmail());
		String subject = "Verify Your Account";
		Mail mail = new Mail();
		
		Personalization personalization = new Personalization();
		personalization.addDynamicTemplateData("verificationLink", dto.getLink());
		personalization.addTo(to);
		personalization.addDynamicTemplateData(
			emailProperty.getVerificationLinkKey(),
			dto.getLink()
		);
		
		mail.setFrom(from);
		mail.setSubject(subject);
		mail.setTemplateId(emailProperty.getVerificationTemplateId());
		mail.addPersonalization(personalization);
		
		Request request = new Request();
		request.setMethod(Method.POST);
		request.setEndpoint("mail/send");
		
		try {
			request.setBody(mail.build());
			Response response = sendGrid.api(request);
			printLog(response);
			if (!httpUtil.isStatusCodeSuccess(response.getStatusCode())) {
				throw new Exception(response.getBody());
			}
		} catch (IOException e) {
			log.error(e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	private void printLog(Response response) {
		log.info("Send email with response status: {}, body: {}", response.getStatusCode(),
			response.getBody());
	}
	
}
