package medved.java.back.repository;

import medved.java.back.entity.FileEntity;
import medved.java.back.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {
    void deleteByUserAndFileName(UserEntity user, String fileName);

    FileEntity findByUserAndFileName(UserEntity user, String file);

    List<FileEntity> findAllByUser(UserEntity user);

    @Modifying
    @Query("update FileEntity f set f.fileName=:newname where f.fileName=:filename and f.user=:user")
    void editFileName(@Param("user") UserEntity user,
                      @Param("filename") String fileName,
                      @Param("newname") String newName);
}
