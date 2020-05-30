package com.bridgelabz.fundoonotes.configuration;

import java.util.HashMap;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class FundooListener implements HttpSessionListener {

	private HashMap<String, String> hashmap = new HashMap<>();

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		System.out.println("-- HttpSessionListener#sessionCreated invoked --");
		HttpSession session = se.getSession();
		hashmap.put(session.getId(), session.getId()+System.currentTimeMillis());
		System.out.println("session id: " + session.getId());
		session.setMaxInactiveInterval(1800);//in seconds
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		hashmap.remove(se.getSession().getId());
		System.out.println("-- HttpSessionListener#sessionDestroyed invoked --");
	}
	
}
