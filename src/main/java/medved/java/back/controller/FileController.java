package medved.java.back.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medved.java.back.dto.FileDto;
import medved.java.back.repository.TokenRepository;
import medved.java.back.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@AllArgsConstructor
@Slf4j
public class FileController {

    private FileService fileService;

    @GetMapping("list")
    public List<FileDto> getAllFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") Integer limit){
        log.info("-> Get all files controller");
        return fileService.getAllFiles(authToken, limit);
    }

    @PostMapping("file")
    public ResponseEntity<?> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            MultipartFile file){
        try {
            fileService.uploadFile(authToken,filename,file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(HttpStatus.OK);

    }
}
