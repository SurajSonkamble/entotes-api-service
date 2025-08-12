package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.FavoriteNoteDto;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.endpoint.NotesEndpoint;
import com.becoder.entity.FileDetails;
import com.becoder.service.NotesService;
import com.becoder.util.CommonUtils;

@RestController

public class NotesController implements NotesEndpoint {

	@Autowired
	private NotesService notesService;

	@Override
	public ResponseEntity<?> saveNotes(String notes, MultipartFile file) throws Exception {

		boolean saveNotes = notesService.saveNotes(notes, file);

		if (saveNotes) {

			return CommonUtils.createBuildResponseMessage("Notes saved success", HttpStatus.CREATED);
		}

		return CommonUtils.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<?> getAllNotes() {

		List<NotesDto> allNotes = notesService.getAllNotes();

		if (CollectionUtils.isEmpty(allNotes)) {

			return ResponseEntity.noContent().build();
		}

		return CommonUtils.createBuildResponse(allNotes, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> downloadFile(Integer id) throws Exception {

		FileDetails fileDetails = notesService.getFileDetails(id);

		byte[] data = notesService.downloadFile(fileDetails);

		HttpHeaders headers = new HttpHeaders();

		String contentType = CommonUtils.getContentType(fileDetails.getOriginalFileName());

		headers.setContentType(MediaType.parseMediaType(contentType));

		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllNotesByUser(Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {

		NotesResponse notes = notesService.getAllNotesByUser(pageNo, pageSize);

		return CommonUtils.createBuildResponse(notes, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> favoriteNote(Integer noteId) throws Exception {

		notesService.favoriteNotes(noteId);

		return CommonUtils.createBuildResponseMessage("NOtes added favorite", HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> unfavoriteNotes(Integer favNoteId) throws Exception {

		notesService.favoriteNotes(favNoteId);

		return CommonUtils.createBuildResponseMessage("Remove Favorite", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getFavoriteNote() {

		List<FavoriteNoteDto> userFavoriteNotes = notesService.getUserFavoriteNotes();

		if (CollectionUtils.isEmpty(userFavoriteNotes)) {

			return ResponseEntity.noContent().build();
		}

		return CommonUtils.createBuildResponse(userFavoriteNotes, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> copyNotes(Integer id) throws Exception {

		boolean copyNotes = notesService.copyNotes(id);

		if (copyNotes) {

			return CommonUtils.createBuildResponseMessage("Copied Success", HttpStatus.CREATED);
		}

		return CommonUtils.createErrorResponseMessage("copy Failed ! Try Again", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> restoreDeleted(Integer id) throws Exception {

		notesService.restoreDeletedNotes(id);

		return CommonUtils.createBuildResponseMessage("Restore Success", HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> emptyRecycleBin(Integer userId) {

		// userId = 2;

		List<NotesDto> notes = notesService.getUserRecycleBinNotes();

		if (CollectionUtils.isEmpty(notes)) {

			return CommonUtils.createBuildResponseMessage("Notes not avaible in Recycle BIn", HttpStatus.OK);
		}

		return CommonUtils.createBuildResponse(notes, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> hardDeleteNote(Integer id) throws Exception {

		notesService.hardDeleteNotes(id);

		return CommonUtils.createBuildResponseMessage("Deleteed Success", HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> searchNotes(String key, Integer pageNo, Integer pageSize) {
		NotesResponse notes = notesService.getAllNoteByUserSearch(pageNo, pageSize, key);
		return CommonUtils.createBuildResponse(notes, HttpStatus.OK);
	}

}
