package com.bridgelabz.fundoonotes.utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FundooInterceptor implements HandlerInterceptor {

	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
		String token = request.getHeader("token");
		if(userService.isSessionActive(token)){
			return true;
		}else {
			ObjectMapper mapper = new ObjectMapper();
			Response responsemsg = new Response(400,"User not logged in, Kindly login");
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write(mapper.writeValueAsString(responsemsg));
			return false;
		}
	}
	
	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,
							ModelAndView modelAndView) throws Exception{

	}

	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,
								Exception exception) throws Exception{
	}
}