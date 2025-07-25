package com.becoder.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.dto.NotesDto;
import com.becoder.entity.Notes;

public interface NotesRepository extends JpaRepository<Notes, Integer> {

	Page<Notes> findByCreatedBy(Integer userId, Pageable pageable);

	List<Notes> findByCreatedByAndIsDeletedTrue(int userId);

}
