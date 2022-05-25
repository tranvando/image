package com.example.demo;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class ImageController {

    @GetMapping("/")
    public String home(){
        return "index";
    }

    List<HashMap> listImages=new ArrayList<HashMap>();

    public void loadImage(String next_cursor) throws Exception {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dzajltfix",
                "api_key", "626825339116911",
                "api_secret", "FPXhBQw5jrSpPsa_Dl-wybZ0bnw"));
        //ApiResponse response =cloudinary.api().resources(ObjectUtils.asMap("resource_type","image","type","upload", "prefix", "uploads","max_results","500","next_cursor",next_cursor));
        ApiResponse response = cloudinary.search().expression("(resource_type:image OR resource_type:video) AND type:upload AND folder:uploads").maxResults(500).nextCursor(next_cursor).execute();
        listImages.addAll((List<HashMap>) response.get("resources"));
        boolean isKeyPresent = response.containsKey("next_cursor");
        if(isKeyPresent) {
            String next = response.get("next_cursor").toString();
            loadImage(next);
        }
    }
    @GetMapping("/getImage")
    public ResponseEntity<JSONObject> showImage() throws Exception {
        listImages.clear();
        JSONObject result=new JSONObject();
        loadImage("");
        //listImages.
        result.put("resources",listImages);
        return ResponseEntity.ok(result);
    }
}
