package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

	private String name;
	private String phone;
	private String address;
	private String mapAddress;	//전체 주소
	private String message;		//배송메시지
	private String postNum;		//우편번호
	private String DNum;		//운송장번호
}
