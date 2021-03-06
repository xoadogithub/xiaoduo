package com.xoado.tools.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONException;
import com.xoado.client.http.XoadoHttpRemote;
import com.xoado.common.XoadoResult;
import com.xoado.tools.service.IImageRecognition;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
* @author  作者
* @version 创建时间：2018年7月3日 上午9:50:52
* 类说明
*/
@Service
public class ImageRecognition implements IImageRecognition{
	/*
	 * 营业执照
	 */
	@Value("${THEBUSINESSLICENSEURL}")
	private String THEBUSINESSLICENSEURL;
	/*
	 * 通用文字识别
	 */
	@Value("${TEXTURL}")
	private String TEXTURL;
	/*
	 * 身份证识别
	 */
	@Value("${IDCARD}")
	private String IDCARD;
	/*
	 * 人脸识别
	 */
	@Value("${FACERECOGNITION}")
	private String FACERECOGNITION;
	/*
	 * 银行卡识别
	 */
	@Value("${BANKCARD}")
	private String BANKCARD;
	/*
	 * 阿里code
	 */
	@Value("${APPCODE}")
	private String APPCODE;
	/*
	 * 存储路径
	 */
	@Value("${XOADO_SCANNING}")
	private String XOADO_SCANNING;

	@Override
	public XoadoResult image_recognition(CommonsMultipartFile file, String base64Body) {
		
		/**
		 * 1:使用通用文字识别TEXTURL，如果识别出来有营业执照进行营业执照识别THEBUSINESSLICENSEURL
		 */
//		生成map
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + APPCODE);
		headers.put("Content-Type", "application/json; charset=UTF-8");
		if(base64Body==null){
	try {
				
		        InputStream in = null;
		        String imagepath = XOADO_SCANNING+new Date().getTime()+file.getOriginalFilename();
		        File file2 = new File(XOADO_SCANNING);
		        if(!file2.exists()){
					 file2.mkdirs();
					}
		        try {
		        	 File newpositiveImgUrlname = new File(imagepath);
		        	 file.transferTo(newpositiveImgUrlname);
		        	 in = new FileInputStream(newpositiveImgUrlname);
//		        	 进行base64编码
		        	 byte[] bytes=new byte[(int)newpositiveImgUrlname.length()]; in.read(bytes);
		        	 base64Body = Base64.getEncoder().encodeToString(bytes); 
		        	
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					if (in != null) {
						try {
							 in.close();
						} catch (Exception e) {
							 e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				
			}
		}
		JSONObject json = new JSONObject();
        json.put("image", base64Body);
        String imaText = json.toString();
        String result = XoadoHttpRemote.xoadoDopost(TEXTURL, imaText, headers);
        
        JSONObject jsonObject =JSONObject.fromObject(result);
        System.out.println("jsonObject:"+jsonObject);
        HashMap<Object,Object> map = new HashMap<>();
        JSONArray jsonArray = jsonObject.getJSONArray("ret");
        if(jsonArray!=null){
        	for (int i = 0; i < jsonArray.size(); i++) {
        		String word = jsonArray.getJSONObject(i).getString("word");
        		for (int j = 1; j < jsonArray.size()-1; j++) {
					map.put(word, word);
				}
    		}
        }	

		Object MYYZH = map.get("营业执照");

		Object MSFZ = map.get("居民身份证");

		Object MYHK = map.get("银行卡");

		if(MSFZ!=null){
			 String result1 = XoadoHttpRemote.xoadoDopost(IDCARD, imaText, headers);
			 System.out.println(result1);
        	 JSONObject jsonObject2 =JSONObject.fromObject(result1);
        	 System.out.println(jsonObject2);
//        	 Object object = jsonObject2.get("reg_num");
        	 return XoadoResult.build(200, "身份证信息",jsonObject2);
        }else
		if(MYYZH!=null){
//        	执行扫描营业执照
        	 String result1 = XoadoHttpRemote.xoadoDopost(THEBUSINESSLICENSEURL, imaText, headers);
        	 JSONObject jsonObject2 =JSONObject.fromObject(result1);
//        	 Object object = jsonObject2.get("reg_num");
        	 return XoadoResult.build(200, "营业执照信息",jsonObject2);
		}else if(MYHK!=null){
//        	执行银行卡扫描
				System.out.println("执行银行卡扫描");
        	 String result1 = XoadoHttpRemote.xoadoDopost(BANKCARD, imaText, headers);
        	 JSONObject jsonObject2 =JSONObject.fromObject(result1);
//        	 Object object = jsonObject2.get("reg_num");
        	 return XoadoResult.build(200, "银行卡信息",jsonObject2);
        }

		return XoadoResult.build(200, "返回值的是通用信息",jsonObject);
	}
	
	public static JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
