package com.becoder.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

	private Integer id;
	
	private String name;
	
	private String description;

	private Boolean isActive;

	private Integer createdBy;

	private Date createdOn;

	


}
