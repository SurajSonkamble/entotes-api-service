package com.becoder.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserDto {

	private Integer id;

	private String firstName;

	private String lastName;

	private String email;

	private String mobNo;

	private String password;

	private List<RoleDto> roles;

	@AllArgsConstructor
	@NoArgsConstructor
	@Setter
	@Getter
	@Builder
	public static class RoleDto {

		private Integer id;

		private String name;

	}

}
