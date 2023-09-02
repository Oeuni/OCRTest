package com.example.demo.controller;


import com.example.demo.dto.AddressDTO;
import com.example.demo.infra.naver.ocr.NaverOcrApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class CheckController {
	
	String add = null;
     @GetMapping("/naverOcr")
    public ResponseEntity ocr() throws  IOException{
    	 
        String fileName = "OCR_Sample_img_쿠팡_6.jpg";
        File file = ResourceUtils.getFile("classpath:img/"+fileName); //classpath:static/image/test/

        List<String> result = NaverOcrApi.callApi("POST", file.getPath(), "YklnV3VzYWZtSmlvRVRWenBHeU1rbnVOVFloaWFOSU4=", "jpg");
        //네이버 시크릿 키
        String re = "1/1, 쿠팡은, 로켓배송, 1577-7011, coupang, 차, P6, 인천2, 785979, To, 202, B03, Fr, 주집, 하일시, 주문번호, 5,0001,2589,5582, 운송장번호, 10,2272,8815,7752, 개인정보, 보호를, 위하여, 인수하신, 화물의, 운송장을, 폐기하여, 주시가, 바랍니다., To., 김성, *, From., 212번, 문, 앞-경비실, 호출, 배달표, 연수구, 컨벤시아대로42번길, 35, 일, 인수자, coupang, 서명, 701동, 1402호, (송도2동, 16-8번, ), 지,, 송도더샵그린애비뉴7단지), 60B, 01/09, 일, D3효, 오전7752";
        
        AddressDTO addressDTO = new AddressDTO();
        String [] results = new String[result.size()];  
        for (int i = 0; i < results.length; i++) {
			results[i] = result.get(i);
		}
//        String[] results = re.split(",");
        for (int i = 0; i < results.length; i++) {
        	///////////////////////////////운송장번호 추출
           if (results[i].trim().contains("운송장번호")) {
        	   for (int j = 0; j < 4; j++) {
				if(results[i+j].contains("10")) {
					System.out.println("운송장번호 : " + results[i+j].trim());
		        	   addressDTO.setDNum(results[i+j].trim());
				}
			}
           }
           ///////////////////////////////////배송메시지, 이름 추출
           if(results[i].trim().contains("From.")) {
        	   if(results[i-1].equals("*")) {
        		   addressDTO.setName(results[i-2].trim() + results[i-1].trim());
        		   System.out.println("이름 : " + results[i-2].trim() + results[i-1].trim());
        	   }
        	   else {
        		   addressDTO.setName(results[i-1].trim());
        		   System.out.println("이름 : " + results[i-1].trim());
        	   }
        	   
        	   for (int j = 0; j < 4; j++) {
        		   if(results[i+j].trim().contains("문")) {
        			   if(results[i+j+1].trim().contains("배달표")) {
        				   System.out.println("배송메시지 : " + results[i+j].trim());
        	        	   addressDTO.setMessage(results[i+j].trim());
        			   }
        			   else if(results[i+j+2].trim().contains("배달표")) {
        				   System.out.println("배송메시지 : " + results[i+j].trim() + results[i+j+1].trim());
        	        	   addressDTO.setMessage(results[i+j].trim() + results[i+j+1].trim());
        			   }
        			   else {
        				   System.out.println("배송메시지 : " + results[i+j].trim() + results[i+j+1] + results[i+j+2]);
        	        	   addressDTO.setMessage(results[i+j].trim() + results[i+j+1] + results[i+j+2]);
        			   }
        		   }
        	   }
           }
           
           if(add == null) {
        	   //////////////////////////////////Map 주소 추출
        	   if(results[i].trim().contains("배달표")) {
        		   if(results[i+3].contains("인수")) {
        			   addressDTO.setMapAddress(results[i+1].trim() + results[i+2]);
        			   System.out.println("Map 주소 : " + results[i+1].trim() + results[i+2]);
        			   add = results[i+1].trim() + results[i+2];
        			   System.out.println(add);
        			   for (int j = 12; j > 0; j--) {
						if(results[i+j].contains("호")) {
							System.out.println("주소 : " + add + " " + results[i+j-1] + " " + results[i+j]);
	            			addressDTO.setAddress(add + " " + results[i+j-1] + " " + results[i+j]);
						}
					}
        		   }
        		   else {
        			   addressDTO.setMapAddress(results[i+1].trim() + results[i+2] + results[i+3]);
        			   System.out.println("Map 주소 : " + results[i+1].trim() + results[i+2] + results[i+3]);
        			   add = results[i+1].trim() + results[i+2] + results[i+3];
        			   System.out.println(add);
        			   for (int j = 12; j > 0; j--) {
   						if(results[i+j].contains("호")) {
   							System.out.println("주소 : " + add + " " + results[i+j-1] + " " + results[i+j]);
   	            			addressDTO.setAddress(add + " " + results[i+j-1] + " " + results[i+j]);
   						}
   					}
        		   }
        	   }
           }
           
      }
        
        
        
       return new ResponseEntity(addressDTO, HttpStatus.OK);
        //return new ResponseEntity(result, HttpStatus.OK);
    }
}