package com.example.demo.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.dto.Student;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
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

    @GetMapping("/getImage1")
    public ResponseEntity<List<Student>> readXml() {
        List<Student> listStudents = new ArrayList<>();
        Student student = null;

        try {
            // đọc file input1.xml
            File inputFile = new File("input.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // in phần tử gốc ra màn hình
//            System.out.println("Phần tử gốc:"
//                    + doc.getDocumentElement().getNodeName());

            // đọc tất cả các phần tử có tên thẻ là "student"
            NodeList nodeListStudent = doc.getElementsByTagName("student");
//            NodeList a = doc.getChildNodes();
            System.out.println(nodeListStudent);

            // duyệt các phần tử demo.student
            for (int i = 0; i < nodeListStudent.getLength(); i++) {
                // tạo đối tượng demo.student
                student = new Student();
                // đọc các thuộc tính của demo.student
                Node nNode = nodeListStudent.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    student.setId(eElement.getAttribute("id"));
                    student.setFirstName(eElement.getElementsByTagName("firstname").item(0).getTextContent());
                    student.setLastName(eElement.getElementsByTagName("lastname").item(0).getTextContent());
                    student.setMarks(eElement.getElementsByTagName("marks").item(0).getTextContent());
                }
                // add đối tượng demo.student vào listStudents
                listStudents.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(listStudents);
    }



}
