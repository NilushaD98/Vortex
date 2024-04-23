package Vortex.adminservice.controller;

import Vortex.adminservice.feign.PostServiceProxy;
import Vortex.adminservice.service.PostService;
import Vortex.adminservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vortexadminservice/api/v1/post/")
@RequiredArgsConstructor
public class AdminController {



}
