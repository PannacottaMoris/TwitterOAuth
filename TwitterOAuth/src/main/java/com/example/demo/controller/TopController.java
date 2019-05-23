/*
 * TopController.java
 * Topページにアクセスした際のアクセスコントロールやページ遷移を管理する。
 */

package com.example.demo.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.GoodsEntity;
import com.example.demo.entity.SearchQuery;
import com.example.demo.schedule.ScheduledService;

@Controller
@RequestMapping("/")
public class TopController {
	private Twitter twitter;
	private ConnectionRepository connectionRepository;
	private final String loginPage = "redirect:/connect/twitter";

	@Autowired
	private ScheduledService scheduleService;

	@Inject
	public TopController(Twitter twitter, ConnectionRepository connectionRepository) {
		this.twitter = twitter;
		this.connectionRepository = connectionRepository;
	}
	
	//Twitterへのログイン状況を確認する。
	public boolean isNeedLogin() {
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			return true;
		}
		return false;
	}
	
	//Twitter認証状況を確認し、見認証ならば接続ページへ遷移する。
	//認証済みであればTOPページへ遷移する。
	@RequestMapping(method=RequestMethod.GET)
	public String login(Model model){
		if (isNeedLogin()) {
			return loginPage;
		}
		return "redirect:/top";
	}

	//Twitterお気に入りページ
	@RequestMapping(value="home", method=RequestMethod.GET)
	public String home(Model model){
		if (isNeedLogin()) {
			return loginPage;
		}
		model.addAttribute(twitter.userOperations().getUserProfile());
		List<Tweet> favorites = twitter.timelineOperations().getFavorites();
		model.addAttribute("favorites", favorites);
		return "home";
	}

	//TOPページへ遷移
	@RequestMapping(value = "top", method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute SearchQuery query) {
		List<GoodsEntity> arrives = scheduleService.getArrivals();
		List<GoodsEntity> discontinued = scheduleService.getDiscontinued();
		ModelAndView mav = new ModelAndView();
		if (isNeedLogin()) {
			mav.setViewName(loginPage);
			return mav;
		}
		mav.addObject("arrives", arrives);
		mav.addObject("discontinued", discontinued);
		mav.setViewName("index");
		mav.addObject("query", query);
		return mav;
	}
}
