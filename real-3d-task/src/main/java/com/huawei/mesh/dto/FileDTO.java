package com.huawei.mesh.dto;

import lombok.Data;

import java.io.File;

/**
 * @author ：why
 * @description: TODO
 * @date ：2022/09/18 1:58
 */
@Data
public class FileDTO {
    private String dataName;
    private File file;
    private String opType;
}
