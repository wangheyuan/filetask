package com.huawei.mesh.domain.entity;

import com.huawei.mesh.domain.state.DirState;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：why
 * @description: TODO
 * @date ：2022/09/16 22:01
 */
@Data
@Entity
public class OutDir {
    @Id
    @Column(name = "dir_out_id", nullable = false)
    private String dirId;

    @OneToMany(mappedBy = "inputDir")
    private List<OutFile> files;

    @Convert(converter = DirState.Converter.class)
    private DirState state;

    private String dirName;

    @ManyToOne
    @JoinColumn(name = "dir_id")
    private InputDir inputDir;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

}
