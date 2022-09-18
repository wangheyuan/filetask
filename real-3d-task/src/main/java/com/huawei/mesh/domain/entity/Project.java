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
 * @date ：2022/09/16 21:27
 */
@Data
@Entity
public class Project {
    @Id
    @Column(name = "project_id", nullable = false, length = 128)
    private String projectId;

    private String starMapProjectId;

    private String projectName;

    @Convert(converter = DirState.Converter.class)
    private DirState state;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project", cascade = CascadeType.ALL)
    private List<InputDir> dirs;

    @UpdateTimestamp
    private LocalDateTime updateDate;

}
