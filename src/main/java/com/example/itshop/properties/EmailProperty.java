package com.example.itshop.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class EmailProperty {
	@Value("${mail.api-key}")
	private String apiKey;
	
	@Value("${mail.from}")
	private String fromEmail;
	
	@Value("${mail.verification.template-id}")
	private String verificationTemplateId;
	
	@Value("${mail.reset-password.template-id}")
	private String resetPasswordTemplateId;
	
	private final String verificationLinkKey = "verificationLink";
	
	private final String resetPasswordLinkKey = "resetPasswordLink";
}
