package medved.java.back.service;

import lombok.extern.slf4j.Slf4j;
import medved.java.back.dto.FileDto;
import medved.java.back.entity.FileEntity;
import medved.java.back.entity.UserEntity;
import medved.java.back.exception.ActionDataException;
import medved.java.back.exception.InputDataException;
import medved.java.back.exception.UnauthorizedException;
import medved.java.back.repository.FileRepository;
import medved.java.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileService {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @Autowired
    public FileService(UserRepository userRepository, FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    private UserEntity getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        log.info("-> Current user name by security {}", userDetails.getUsername());
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> {
                    log.error("-> Unauthorized error");
                    throw new UnauthorizedException("Unauthorized error");
                }
        );
    }

    public List<FileDto> getAllFiles(Integer limit) {
        if (limit < 1) {
            throw new InputDataException("Error input data");
        }
        UserEntity user = getCurrentUser();
        try {
            log.info("-> Success get all files for user {}", user.getUsername());
            return fileRepository.findAllByUser(user)
                    .stream()
                    .map(userEntity -> new FileDto(userEntity.getFileName(), userEntity.getSize()))
                    .limit(limit)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ActionDataException("Error getting file list");
        }
    }

    public void uploadFile(String filename, MultipartFile file) {
        if (filename == null || file == null) {
            throw new InputDataException("Error input data");
        }
        UserEntity user = getCurrentUser();
        try {
            fileRepository.save(new FileEntity(filename, file.getSize(), file.getBytes(), user));
            log.info("-> Success upload file {}", filename);
        } catch (IOException e) {
            log.error("-> Error uploading file");
            throw new ActionDataException("Error uploading file");
        }
    }

    @Transactional
    public void deleteFile(String filename) {
        if (filename == null) {
            throw new InputDataException("Error input data");
        }
        UserEntity user = getCurrentUser();
        try {
            fileRepository.deleteByUserAndFileName(user, filename);
        } catch (Exception e) {
            log.error("-> Error delete file");
            throw new ActionDataException("Error delete file");
        }
        log.info("-> Success delete file {}", filename);
    }

    @Transactional
    public void editFileName(String filename, FileDto fileDto) {
        if (filename == null || fileDto == null) {
            throw new InputDataException("Error input data");
        }
        UserEntity user = getCurrentUser();
        try {
            fileRepository.editFileName(user, filename, fileDto.getFilename());
        } catch (Exception e) {
            log.error("-> Error editing file");
            throw new ActionDataException("Error editing file");
        }
        log.info("-> Success update {} to {}", filename, fileDto.getFilename());
    }

    public byte[] downloadFile(String filename) {
        if (filename == null) {
            throw new InputDataException("Error input data");
        }
        UserEntity user = getCurrentUser();
        FileEntity file = fileRepository.findByUserAndFileName(user, filename);
        if (file == null) {
            log.error("-> Error upload file");
            throw new ActionDataException("Error upload file");
        }
        log.info("-> Success download file {}", filename);
        return file.getFileContent();
    }
}
