package com.rifqimuhammadaziz.employeetraining.utility;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class TemplateResponse {
    public Map Success(Object data, String message, String status) {
        Map map = new HashMap();
        map.put("data",data);
        map.put("message",message);
        map.put("status",status);
        return map;
    }
    public Map Success(Object objek){
        Map map = new HashMap();
        map.put("message", "Success");
        map.put("status", "200");
        map.put("data", objek);

        return map;
    }
    public Map Error(Object objek){
        Map map = new HashMap();
        map.put("message", objek);
        map.put("status", "404");
        return map;
    }
    public Map Error(String message, String status) {
        Map map = new HashMap();
        map.put("message",message);
        map.put("status",status);
        return map;
    }

    public boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }
}
