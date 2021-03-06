package com.wuda.bbs.utils.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NetTool {
    public static Map<String, String> encodeUrlFormWithGBK(Map<String, String> form) {
        Map<String, String> encodedForm = new HashMap<>();
        for (String key: form.keySet()) {
            try {
                String encodedKey = URLEncoder.encode(key, "GBK");
                String encodedValue = URLEncoder.encode(form.get(key), "GBK");
                encodedForm.put(encodedKey, encodedValue);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return encodedForm;
    }

    public static Map<String, String> extractUrlParam(String url) {
        Map<String, String> paramaters = new HashMap<>();

        String params = url.split("\\?")[1];
        String[] param_arr = params.split("&");
        for (String param: param_arr) {
            String[] pair = param.split("=");
            if (pair.length == 2) {
                paramaters.put(pair[0], pair[1]);
            }
        }

        return paramaters;
    }
}
