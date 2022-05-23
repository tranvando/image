package com.example.demo;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ImageController {

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/getImage")
    public ResponseEntity<ApiResponse> showImage() throws Exception {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dzajltfix",
                "api_key", "626825339116911",
                "api_secret", "FPXhBQw5jrSpPsa_Dl-wybZ0bnw"));
        ApiResponse response =cloudinary.api().resources(ObjectUtils.asMap("type", "upload", "prefix", "uploads"));
        return ResponseEntity.ok(response);
    }
}
