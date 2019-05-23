/*
 * TwitterConnectController.java
 * Twitterへのアクセス、認証を管理する。
 */

package com.example.demo.controller;

import javax.inject.Inject;

import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/connect")
public class TwitterConnectController extends ConnectController {
	
	@Inject
	public TwitterConnectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
		super(connectionFactoryLocator, connectionRepository);
	}
	
	@Override
	protected String connectedView(String provideId) {
		return "redirect:/";
	}
}
