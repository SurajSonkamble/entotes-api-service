package com.becoder.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.FavoriteNoteDto;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FileDetails;

public interface NotesService {

	public boolean saveNotes(String notes, MultipartFile file) throws Exception;

	public List<NotesDto> getAllNotes();

	public byte[] downloadFile(FileDetails fileDetails) throws Exception;

	public FileDetails getFileDetails(Integer id) throws Exception;

	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize);

	public void favoriteNotes(Integer notesId) throws Exception;

	public void unFavoriteNotes(Integer favoriteNoteId) throws Exception;

	public List<FavoriteNoteDto> getUserFavoriteNotes();
	
	public boolean copyNotes(Integer id) throws Exception;

}
