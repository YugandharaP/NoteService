package com.bridgelabz.noteservice.utilservice.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bridgelabz.noteservice.utilservice.securityservice.JwtTokenProvider;
/*
*//**
 *@since 28/07/2018
 * <p>Interceptor it's like a filter which </p>
 *@author yuga
 *//*
@Component
public class NoteInterceptor implements HandlerInterceptor{
	static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

	@Autowired
	IRedisRepository iRedisRepository;
	
	*//**<p><b>To handle the request which coming from view before going to the controller</b></p>*//*
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object object) throws Exception {
		logger.info("before url "+request.getRequestURI());
		String tokenFromHeader = request.getHeader("token");
		JwtTokenProvider tokenProvider = new JwtTokenProvider();
		
		String userId = tokenProvider.parseJWT(tokenFromHeader);
		logger.debug(tokenFromHeader);
		
		
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		
		if(tokenFromRedis==null)
		{
			return false;
		}
		request.setAttribute("userId", userId);
		logger.info("Inceptor work done");
		
		return true;
	}

}*/

