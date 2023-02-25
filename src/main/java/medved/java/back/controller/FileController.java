package medved.java.back.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medved.java.back.dto.FileDto;
import medved.java.back.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@AllArgsConstructor
@Slf4j
public class FileController {

    private FileService fileService;

    @GetMapping("list")
    public List<FileDto> getAllFiles(@RequestParam("limit") Integer limit) {
        log.info("-> Get all files controller");
        return fileService.getAllFiles(limit);
    }

    @PostMapping("file")
    public ResponseEntity<?> uploadFile(
            @RequestParam("filename") String filename,
            MultipartFile file) {
        fileService.uploadFile(filename, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("file")
    public ResponseEntity<?> deleteFile(@RequestParam("filename") String filename) {
        fileService.deleteFile(filename);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename) {
        byte[] file = fileService.downloadFile(filename);
        return new ResponseEntity<>(new ByteArrayResource(file), HttpStatus.OK);
    }

    @PutMapping(value = "/file")
    public ResponseEntity<?> editFileName(@RequestParam("filename") String filename, @RequestBody FileDto fileDto) {
        fileService.editFileName(filename, fileDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
