package com.huawei.mesh.service;

import com.huawei.mesh.dto.ProjectSyncDTO;
import com.huawei.mesh.vo.ProjectSyncVo;

public interface IProjectService {

    /**
     * 获取文件的差异状态
     * @param projectSyncDTO
     * @return
     */
    ProjectSyncVo syncProjectFile(ProjectSyncDTO projectSyncDTO);
}
