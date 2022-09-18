package com.huawei.mesh.domain.entity;

import com.huawei.mesh.domain.state.DirState;
import com.huawei.mesh.domain.state.FileState;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：why
 * @description: TODO
 * @date ：2022/09/16 21:49
 */
@Data
@Entity
public class InputDir {
    @Id
    @Column(name = "dir_id", nullable = false)
    private String dirId;

    private String dirName;

    @OneToMany(mappedBy = "inputDir")
    private List<OutDir> dirs;

    @Convert(converter = DirState.Converter.class)
    private DirState state;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "inputDir", cascade = CascadeType.ALL)
    private List<InputFile> files;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
