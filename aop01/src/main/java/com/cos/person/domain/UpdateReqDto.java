package com.cos.person.domain;

import javax.validation.constraints.NotBlank;

import lombok.Data;

// 페이지나 요청마다 필요한 정보가 다르기 때문에 따로 DTO를 설계해주는게 좋다. (유연성 증가)
@Data
public class UpdateReqDto {
	@NotBlank(message = "password를 입력하지 않았습니다.")
	private String password;
	private String phone;
}
