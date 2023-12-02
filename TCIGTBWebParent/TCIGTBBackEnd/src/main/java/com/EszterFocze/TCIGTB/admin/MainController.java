package com.EszterFocze.TCIGTB.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

	@GetMapping("") //handler method for the http get request to the homepage
	public String viewHomePage() {
		return "home"; //admin control panel
	}

	@GetMapping("/")
	public ModelAndView displayHomeScreen(Model model) {
		final ModelAndView modelAndView = new ModelAndView("home");
		return modelAndView;
	}
	
	@GetMapping("/home")
	public ModelAndView displayHomePage(Model model) {
		final ModelAndView modelAndView = new ModelAndView("home");
		return modelAndView;
	}
}
