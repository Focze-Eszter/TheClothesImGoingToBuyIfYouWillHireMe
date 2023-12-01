package com.EszterFocze.TCIGTB.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("") //handler method for the http get request to the homepage
	public String viewHomePage() {
		return "index"; //admin control panel
	}
}
