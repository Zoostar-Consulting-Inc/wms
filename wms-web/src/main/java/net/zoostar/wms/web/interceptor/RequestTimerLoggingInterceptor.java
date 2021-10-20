package net.zoostar.wms.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestTimerLoggingInterceptor implements HandlerInterceptor {

	private static final String TIMER = "timer";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("URI: {}", request.getRequestURI());
		if(handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod)handler;
			log.info("handlerMethod: {}", handlerMethod);
		}
		request.setAttribute(TIMER, Long.valueOf(System.currentTimeMillis()));
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		long timer = (long)request.getAttribute(TIMER);
		long time = System.currentTimeMillis() - timer;
		log.info("Request took {} seconds.", time/1000);
	}
}
