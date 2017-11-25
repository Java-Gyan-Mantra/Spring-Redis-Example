package com.spring.redis.api.exception.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ServiceExceptionHandler {
	@ExceptionHandler(ServiceException.class)
	public ModelAndView handelException(ServiceException ex) {
		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject("errorMessage", ex.getMessage());
		return mav;
	}
}
