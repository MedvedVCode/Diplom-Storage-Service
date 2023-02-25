package medved.java.back.service;

import lombok.extern.slf4j.Slf4j;
import medved.java.back.dto.FileDto;
import medved.java.back.entity.FileEntity;
import medved.java.back.entity.UserEntity;
import medved.java.back.repository.FileRepository;
import medved.java.back.repository.TokenRepository;
import medved.java.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileService {
    private UserRepository userRepository;
    private FileRepository fileRepository;
    @Value("${jwt.token.bearer-size}")
    private int bearerSize;

    @Autowired
    public FileService(UserRepository userRepository, FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    private UserEntity getUser(String authToken) {
        log.info("-> Trying to get user from security");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        log.info("-> Username by token {}", userDetails.getUsername());
        return userRepository.findByUsername(userDetails.getUsername()).get();
    }

    public List<FileDto> getAllFiles(String authToken, Integer limit) {
        UserEntity user = getUser(authToken);

        log.info("-> Get all files service works...");
        return fileRepository.findAllByUser(user)
                .stream()
                .map(userEntity -> new FileDto(userEntity.getFileName(), userEntity.getSize()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public boolean uploadFile(String authToken, String filename, MultipartFile file) throws IOException {
        UserEntity user = getUser(authToken);
        fileRepository.save(new FileEntity(filename, file.getSize(), file.getBytes(), user));
        return true;
    }
}
