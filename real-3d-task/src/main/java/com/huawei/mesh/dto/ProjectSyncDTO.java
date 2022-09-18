package com.huawei.mesh.dto;

import lombok.Data;

import java.util.List;

/**
 * @author ：why
 * @description: TODO
 * @date ：2022/09/16 21:22
 */
@Data
public class ProjectSyncDTO {
    private String projectId;
    private String projectName;
    private List<ProjectDirDTO> dirs;
}
