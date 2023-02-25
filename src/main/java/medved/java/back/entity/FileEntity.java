package medved.java.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files")
public class FileEntity {
    @Id
    @Column(name = "filename", nullable = false, unique = true)
    private String fileName;
    @Column(nullable = false)
    private Long size;
    @Lob
    @Column(name = "filecontent", nullable = false)
    private byte[] fileContent;
    @ManyToOne
    private UserEntity user;
}
