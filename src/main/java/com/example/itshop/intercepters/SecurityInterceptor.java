package com.example.itshop.intercepters;

import com.example.itshop.annotations.AppAuth;
import com.example.itshop.enums.ClientType;
import com.example.itshop.utils.SecurityUtil;
import io.jsonwebtoken.lang.Strings;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class SecurityInterceptor implements HandlerInterceptor {
	
	private final SecurityUtil securityUtil;
	
	private final String AUTHORIZATION_HEADER = "Authorization";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod handlerMethod) {
			if (handlerMethod.getMethod().isAnnotationPresent(AppAuth.class)) {
				ClientType[] clientTypes = handlerMethod.getMethod()
					.getDeclaredAnnotation(AppAuth.class).clientTypes();
				return doFilter(clientTypes, request, response);
			} else if (handlerMethod.getMethod().getDeclaringClass().isAnnotationPresent(AppAuth.class)) {
				ClientType[] clientTypes = handlerMethod.getMethod().getDeclaringClass()
					.getDeclaredAnnotation(AppAuth.class).clientTypes();
				return doFilter(clientTypes, request, response);
			}
		}
		
		return true;
	}
	
	private boolean doFilter(@NonNull ClientType[] clientTypes, @NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws IOException {
		try {
			String token = this.resolveToken(request);
			securityUtil.authenticate(token, clientTypes);
			return true;
		} catch (ResponseStatusException e) {
			response.setStatus(e.getStatus().value());
			response.getWriter().println(e.getMessage());
			return false;
		} catch (Exception e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().println(e.getMessage());
			return false;
		}
	}
	
	private String resolveToken(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION_HEADER);
		if (Strings.hasText(token) && token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return null;
	}
	
}
