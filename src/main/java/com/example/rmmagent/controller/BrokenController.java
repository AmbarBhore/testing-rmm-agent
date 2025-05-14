package com.example.rmmagent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrokenController {

	    @GetMapping("/broken")
	        public String causeError() {
			        return ResponseEntity.ok("This won't compile"); 
		}
}	    
