package com.example.demo;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class ImageController {

    @GetMapping("/")
    public String home(@RequestParam(required = false) String type, Model model){
        if(StringUtils.isBlank(type))
            model.addAttribute("type","image");
        else
            model.addAttribute("type",type);
        return "index2";
    }

    List<HashMap> listImages=new ArrayList<HashMap>();

    public void loadImage(String type,String next_cursor) throws Exception {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dzajltfix",
                "api_key", "626825339116911",
                "api_secret", "FPXhBQw5jrSpPsa_Dl-wybZ0bnw"));
        //ApiResponse response =cloudinary.api().resources(ObjectUtils.asMap("resource_type","image","type","upload", "prefix", "uploads","max_results","500","next_cursor",next_cursor));
        //ApiResponse response = cloudinary.search().expression("(resource_type:image OR resource_type:video) AND type:upload AND folder:uploads").maxResults(500).nextCursor(next_cursor).execute();
        ApiResponse response = cloudinary.search().expression("resource_type:"+type+" AND type:upload AND folder:uploads").maxResults(500).nextCursor(next_cursor).execute();
        listImages.addAll((List<HashMap>) response.get("resources"));
        boolean isKeyPresent = response.containsKey("next_cursor");
        if(isKeyPresent) {
            String next = response.get("next_cursor").toString();
            loadImage(type,next);
        }
    }
    @GetMapping("/getImage")
    public ResponseEntity<JSONObject> showImage(@RequestParam String type) throws Exception {
        listImages.clear();
        JSONObject result=new JSONObject();
        loadImage(type,"");
        //listImages.
        result.put("resources",listImages);
        return ResponseEntity.ok(result);
    }
}
