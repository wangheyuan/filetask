package com.huawei.mesh.domain.entity;

import com.huawei.mesh.domain.state.FileState;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author ：why
 * @description: TODO
 * @date ：2022/09/17 7:42
 */
@Data
@Entity
public class InputFile {
    @Id
    @Column(name = "input_id", nullable = false)
    private String inputId;

    @ManyToOne
    @JoinColumn(name = "input_dir_id")
    private InputDir inputDir;

    private String fileName;

    private String path;

    @Convert(converter = FileState.Converter.class)
    private FileState state;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;

}
