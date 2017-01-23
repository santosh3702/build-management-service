package com.build.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.build.model.BuildServiceRequest;
import com.build.model.BuildServiceResponse;

@RestController
@CrossOrigin
public class BuildServiceController {
	
	@Autowired
	private BuildServiceHelper buildServiceHelper;

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	@RequestMapping("/test")
	public BuildServiceResponse test() {
		System.out.println("test");
		BuildServiceResponse buildServiceResponse = new BuildServiceResponse();
		buildServiceResponse.setStatus("Pass");
		return buildServiceResponse;
	}
	
	@RequestMapping("/build")
	public BuildServiceResponse build(@RequestBody BuildServiceRequest request) {
		System.out.println("doBuild  "+request);
		BuildServiceResponse buildServiceResponse = buildServiceHelper.cloneAndBuild(request);
		return buildServiceResponse;
	}
	
	
}
