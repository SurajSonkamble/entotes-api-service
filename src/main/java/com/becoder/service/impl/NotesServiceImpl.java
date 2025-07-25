package com.becoder.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesDto.CategoryDto;
import com.becoder.dto.NotesDto.FilesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FileDetails;
import com.becoder.entity.Notes;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.CategoryRepository;
import com.becoder.repository.FileRepository;
import com.becoder.repository.NotesRepository;
import com.becoder.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private NotesRepository notesRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private FileRepository fileRepo;

	@Autowired
	private CategoryRepository categoryRepo;

	@Value("${file.upload.path}")
	private String uplaodPath;

	@Override
	public boolean saveNotes(String notes, MultipartFile file) throws Exception {

		ObjectMapper ob = new ObjectMapper();

		NotesDto notesDto = ob.readValue(notes, NotesDto.class);

		if (!ObjectUtils.isEmpty(notesDto.getId())) {

			updateNotes(notesDto, file);
		}

		checkCategoryExist(notesDto.getCategory());

		Notes notesMap = mapper.map(notesDto, Notes.class);

		FileDetails fileDtls = saveFileDtls(file);

		if (!ObjectUtils.isEmpty(fileDtls)) {

			notesMap.setFileDetails(fileDtls);
		} else {

			if (ObjectUtils.isEmpty(notesDto.getId())) {

				notesMap.setFileDetails(null);
			}
		}

		Notes saveNotes = notesRepo.save(notesMap);

		if (!ObjectUtils.isEmpty(saveNotes)) {

			return true;
		}

		return false;
	}

	private void updateNotes(NotesDto notesDto, MultipartFile file) throws Exception {

		Notes existNotes = notesRepo.findById(notesDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Notes id"));

		if (ObjectUtils.isEmpty(file)) {

			notesDto.setFileDetails(mapper.map(existNotes.getFileDetails(), FilesDto.class));
		}

	}

	private FileDetails saveFileDtls(MultipartFile file) throws IOException {

		if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {

			String originalFileName = file.getOriginalFilename();

			String extension = FilenameUtils.getExtension(originalFileName);

			List<String> extensionAllow = Arrays.asList("pdf", "xlsx", "jpg", "png");

			if (!extensionAllow.contains(extension)) {

				throw new IllegalArgumentException("Invalid file foramt ! upload only .pdf, .xlsx,.jpg");
			}

			String rndString = UUID.randomUUID().toString();

			String uploadFileName = rndString + "." + extension;

			File saveFile = new File(uplaodPath);

			if (!saveFile.exists()) {

				saveFile.mkdir();
			}

			String storePath = uplaodPath.concat(uploadFileName);

			// upload path

			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));

			if (upload != 0) {

				FileDetails fileDtls = new FileDetails();

				fileDtls.setOriginalFileName(originalFileName);
				fileDtls.setDisplayFileName(getDisplayName(originalFileName));
				fileDtls.setUploadFileName(uploadFileName);
				fileDtls.setFileSize(file.getSize());
				fileDtls.setPath(storePath);

				FileDetails saveFileDtls = fileRepo.save(fileDtls);

				return saveFileDtls;

			}

		}

		return null;
	}

	private String getDisplayName(String originalFileName) {

		String extension = FilenameUtils.getExtension(originalFileName);
		String fileName = FilenameUtils.removeExtension(originalFileName);

		if (fileName.length() > 8) {

			fileName = fileName.substring(0, 7);
		}

		fileName = fileName + "." + extension;

		return fileName;
	}

	private void checkCategoryExist(CategoryDto category) throws Exception {

		categoryRepo.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("Category is invalid"));

	}

	@Override
	public List<NotesDto> getAllNotes() {

		return notesRepo.findAll().stream().map(note -> mapper.map(note, NotesDto.class)).toList();

	}

	@Override
	public byte[] downloadFile(FileDetails fileDetails) throws Exception {

		// InputStream io = new FileInputStream(fileDtls.getPath());

		InputStream io = new FileInputStream(fileDetails.getPath());

		return StreamUtils.copyToByteArray(io);
	}

	@Override
	public FileDetails getFileDetails(Integer id) throws Exception {

		FileDetails fileDtls = fileRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("File is not avilable"));

		return fileDtls;
	}

	@Override
	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize) {

		Pageable pageable = PageRequest.of(pageNo, pageSize);

		Page<Notes> pageNotes = notesRepo.findByCreatedBy(userId, pageable);

		List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n, NotesDto.class)).toList();

		NotesResponse notes = NotesResponse.builder().notes(notesDto).pageNO(pageNotes.getNumber())
				.pageSize(pageNotes.getSize()).totalElements(pageNotes.getTotalElements())
				.totalPages(pageNotes.getTotalPages()).isFirst(pageNotes.isFirst()).isLast(pageNotes.isLast()).build();

		return notes;
	}

	@Override
	public void hardDeleteNotes(Integer id) throws Exception {

		Notes notes = notesRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not Found"));

		notes.setIsDeleted(true);

		notes.setDeletedOn(LocalDateTime.now());

		notesRepo.save(notes);

	}

	@Override
	public void restoreDeletedNotes(Integer id) throws Exception {

		Notes notes = notesRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id Not Found"));

		notes.setIsDeleted(false);
		notes.setDeletedOn(null);

		notesRepo.save(notes);

	}

	@Override
	public List<NotesDto> getUserRecycleBinNotes(int userId) {

		List<Notes> recycleNotes = notesRepo.findByCreatedByAndIsDeletedTrue(userId);

		List<NotesDto> notesDtoList = recycleNotes.stream().map(note -> mapper.map(note, NotesDto.class)).toList();

		return notesDtoList;

	}

}
