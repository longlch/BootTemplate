package com.guru.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guru.model.ExceptionJSONInfo;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(DestiNearbyException.class)
	public String handleDestiException(HttpServletRequest request, Exception ex){
		logger.info("Destinearly exception:: URL="+request.getRequestURL());
		return "desti_nearby_exception";
	}
	
	@ExceptionHandler(OriginNearlyException.class)
	public String handleOriException(HttpServletRequest request, Exception ex){
		logger.info("Destinearly exception:: URL="+request.getRequestURL());
		return "ori_nearby_exception";
	}
	
	@ExceptionHandler(DirectionException.class)
	public String handleDirectionException(HttpServletRequest request, Exception ex){
		logger.info("Direction exception:: URL="+request.getRequestURL());
		return "direction_exception";
	}
}
