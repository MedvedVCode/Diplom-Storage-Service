package medved.java.back.service;

import medved.java.back.entity.UserEntity;
import medved.java.back.repository.FileRepository;
import medved.java.back.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@ExtendWith(MockitoExtension.class)
@WithMockUser
public class FileServiceTest {
    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private FileService fileService;

    @Test
    public void downloadFileTest() {
        assertNotNull(this.fileService);
        this.fileService.downloadFile("downloadFileName");
        verify(this.fileRepository, times(1)).findByUserAndFileName(ArgumentMatchers.any(UserEntity.class), anyString());
    }
}
