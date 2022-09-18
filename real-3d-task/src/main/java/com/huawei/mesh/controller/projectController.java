package com.huawei.mesh.controller;

import com.huawei.mesh.dto.ProjectSyncDTO;
import com.huawei.mesh.service.IProjectService;
import com.huawei.mesh.service.impl.ProjectService;
import com.huawei.mesh.vo.ProjectSyncVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：why
 * @description: TODO
 * @date ：2022/09/18 1:43
 */
@RequestMapping("project")
@RestController
public class projectController {
    @Autowired
    private IProjectService projectService;

    @PostMapping("sync")
    public ProjectSyncVo syncProject(ProjectSyncDTO projectSyncDTO) {
        return projectService.syncProjectFile(projectSyncDTO);
    }
}
