package com.becoder.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.TodoDto;
import com.becoder.service.TodoService;
import com.becoder.util.CommonUtils;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

	@Autowired
	private TodoService todoService;

	@PostMapping("/")
	public ResponseEntity<?> saveTodo(@RequestBody TodoDto todoDto) throws Exception {

		boolean saveTodo = todoService.saveTodo(todoDto);

		if (saveTodo) {

			return CommonUtils.createBuildResponseMessage(" Todo Saved Success ", HttpStatus.CREATED);
		}

		return CommonUtils.createErrorResponseMessage("Todo Not Save", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getTodoById(@PathVariable Integer id) throws Exception {

		TodoDto todoDto = todoService.getTodoById(id);

		return CommonUtils.createBuildResponse(todoDto, HttpStatus.OK);

	}

	
	@GetMapping("/list")
	public ResponseEntity<?> getAllTodoByUser() {

		List<TodoDto> todoByUser = todoService.getTodoByUser();

		if (CollectionUtils.isEmpty(todoByUser)) {

			return ResponseEntity.noContent().build();

		}

		return CommonUtils.createBuildResponse(todoByUser, HttpStatus.OK);
	}

}
