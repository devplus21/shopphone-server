package com.example.itshop.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class HttpUtil {
	public boolean isStatusCodeSuccess(int statusCode) {
		return (statusCode >= 200 && statusCode <= 299);
	}
}
