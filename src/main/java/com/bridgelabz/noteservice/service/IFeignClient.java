package com.bridgelabz.noteservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Component
@FeignClient(name = "userService", url="http://localhost:8765")//serviceId of userService 
public interface IFeignClient {
	@GetMapping("user/getuserbyemailid/{emailId} ")
	public ResponseEntity<?>getUserByEmailId(@PathVariable ("emailId")String emailId);

}
