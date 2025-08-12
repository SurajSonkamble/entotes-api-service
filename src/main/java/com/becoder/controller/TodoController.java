package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.TodoDto;
import com.becoder.endpoint.TodoEndpoint;
import com.becoder.service.TodoService;
import com.becoder.util.CommonUtils;

@RestController

public class TodoController implements TodoEndpoint {

	@Autowired
	private TodoService todoService;

	@Override
	public ResponseEntity<?> saveTodo(TodoDto todoDto) throws Exception {

		boolean saveTodo = todoService.saveTodo(todoDto);

		if (saveTodo) {

			return CommonUtils.createBuildResponseMessage(" Todo Saved Success ", HttpStatus.CREATED);
		}

		return CommonUtils.createErrorResponseMessage("Todo Not Save", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<?> getTodoById(Integer id) throws Exception {

		TodoDto todoDto = todoService.getTodoById(id);

		return CommonUtils.createBuildResponse(todoDto, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getAllTodoByUser() {

		List<TodoDto> todoByUser = todoService.getTodoByUser();

		if (CollectionUtils.isEmpty(todoByUser)) {

			return ResponseEntity.noContent().build();

		}

		return CommonUtils.createBuildResponse(todoByUser, HttpStatus.OK);
	}

}
