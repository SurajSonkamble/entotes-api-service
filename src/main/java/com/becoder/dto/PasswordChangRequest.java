package com.becoder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangRequest {
	
	private String oldPassword;
	
	private String newPassword;

}
