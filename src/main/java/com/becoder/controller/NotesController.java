package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.FavoriteNoteDto;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FileDetails;
import com.becoder.service.NotesService;
import com.becoder.util.CommonUtils;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

	@Autowired
	private NotesService notesService;

	@PostMapping("/")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception {

		boolean saveNotes = notesService.saveNotes(notes, file);

		if (saveNotes) {

			return CommonUtils.createBuildResponseMessage("Notes saved success", HttpStatus.CREATED);
		}

		return CommonUtils.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping("/")
	public ResponseEntity<?> getAllNotes() {

		List<NotesDto> allNotes = notesService.getAllNotes();

		if (CollectionUtils.isEmpty(allNotes)) {

			return ResponseEntity.noContent().build();
		}

		return CommonUtils.createBuildResponse(allNotes, HttpStatus.OK);

	}

	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {

		FileDetails fileDetails = notesService.getFileDetails(id);

		byte[] data = notesService.downloadFile(fileDetails);

		HttpHeaders headers = new HttpHeaders();

		String contentType = CommonUtils.getContentType(fileDetails.getOriginalFileName());

		headers.setContentType(MediaType.parseMediaType(contentType));

		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@GetMapping("/user-notes")
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize) {

		Integer userId = 1;

		NotesResponse notes = notesService.getAllNotesByUser(userId, pageNo, pageSize);

		return CommonUtils.createBuildResponse(notes, HttpStatus.OK);

	}

<<<<<<< HEAD
	@GetMapping("/fav/{noteId}")
	public ResponseEntity<?> favoriteNote(@PathVariable Integer noteId) throws Exception {

		notesService.favoriteNotes(noteId);

		return CommonUtils.createBuildResponseMessage("NOtes added favorite", HttpStatus.CREATED);
	}

	@DeleteMapping("/un-fav/{favNoteId}")
	public ResponseEntity<?> unfavoriteNotes(@PathVariable Integer favNoteId) throws Exception {

		notesService.favoriteNotes(favNoteId);

		return CommonUtils.createBuildResponseMessage("Remove Favorite", HttpStatus.OK);
	}

	@GetMapping("/fav-note")
	public ResponseEntity<?> getFavoriteNote() {

		List<FavoriteNoteDto> userFavoriteNotes = notesService.getUserFavoriteNotes();

		if (CollectionUtils.isEmpty(userFavoriteNotes)) {

			return ResponseEntity.noContent().build();
		}

		return CommonUtils.createBuildResponse(userFavoriteNotes, HttpStatus.OK);

	}

	@GetMapping("/copy/{id}")
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception {

		boolean copyNotes = notesService.copyNotes(id);

		if (copyNotes) {

			return CommonUtils.createBuildResponseMessage("Copied Success", HttpStatus.CREATED);
		}

		return CommonUtils.createErrorResponseMessage("copy Failed ! Try Again", HttpStatus.INTERNAL_SERVER_ERROR);

=======
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception {

		notesService.hardDeleteNotes(id);

		return CommonUtils.createBuildResponseMessage("Delete Success", HttpStatus.OK);

	}

	@GetMapping("/restore/{id}")
	public ResponseEntity<?> restoreDeleted(@PathVariable Integer id) throws Exception {

		notesService.restoreDeletedNotes(id);

		return CommonUtils.createBuildResponseMessage("Restore Success", HttpStatus.OK);

	}

	@GetMapping("/recycle-bin")
	public ResponseEntity<?> emptyRecycleBin(Integer userId) {

		userId = 2;

		List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);

		if (CollectionUtils.isEmpty(notes)) {

			return CommonUtils.createBuildResponseMessage("Notes not avaible in Recycle BIn", HttpStatus.OK);
		}

		return CommonUtils.createBuildResponse(notes, HttpStatus.OK);
	}

	public ResponseEntity<?> hardDeleteNote(@PathVariable Integer id) throws Exception {

		notesService.hardDeleteNotes(id);

		return CommonUtils.createBuildResponseMessage("Deleteed Success", HttpStatus.OK);
>>>>>>> 6ea6b7bc696d66611edcbc013fed823b965bfecd
	}

}
