package com.tj.practice.common;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Antony
 * @since 9/14/2018
 */

public class BaseController {

	@Autowired
	public HttpServletRequest request;
    

    /**
     * 获取Session
     * @return
     */
    public HttpSession getSession() {
        return request.getSession();
    }

    
    
   

}
