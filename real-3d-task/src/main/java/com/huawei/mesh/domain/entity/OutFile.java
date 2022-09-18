package com.huawei.mesh.domain.entity;

import com.huawei.mesh.domain.state.FileState;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author ：why
 * @description: TODO
 * @date ：2022/09/17 7:42
 */
@Entity
@Data
public class OutFile {
    @Id
    @Column(name = "out_id", nullable = false)
    private String OutId;

    @ManyToOne
    @JoinColumn(name = "out_dir_id")
    private InputDir inputDir;

    private String fileName;

    private String path;

    @Convert(converter = FileState.Converter.class)
    private FileState state;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;
}
