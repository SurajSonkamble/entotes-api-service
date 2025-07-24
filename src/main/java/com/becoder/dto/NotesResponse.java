package com.becoder.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotesResponse {

	private List<NotesDto> notes;

	private Integer pageNO;

	private Integer pageSize;

	private Long totalElements;

	private Integer totalPages;

	private Boolean isFirst;

	private boolean isLast;

}
