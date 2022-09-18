package com.huawei.mesh.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ：why
 * @description: TODO
 * @date ：2022/09/17 8:35
 */
@Data
public class ProjectSyncVo {
    private String projectId;
    private String projectName;
    private String state;
    private List<FileSyncItem> items;
}
