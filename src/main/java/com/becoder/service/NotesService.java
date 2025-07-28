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

<<<<<<< HEAD
	public void favoriteNotes(Integer notesId) throws Exception;

	public void unFavoriteNotes(Integer favoriteNoteId) throws Exception;

	public List<FavoriteNoteDto> getUserFavoriteNotes();
	
	public boolean copyNotes(Integer id) throws Exception;
=======
	public void hardDeleteNotes(Integer id) throws Exception;

	public void restoreDeletedNotes(Integer id) throws Exception;

	// public void emptyRecycleBin(int userId);

	public List<NotesDto> getUserRecycleBinNotes(int userId);
>>>>>>> 6ea6b7bc696d66611edcbc013fed823b965bfecd

}
