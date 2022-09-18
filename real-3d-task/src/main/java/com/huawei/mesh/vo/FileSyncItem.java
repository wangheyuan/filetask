package com.huawei.mesh.vo;

import lombok.Data;

/**
 * @author ：why
 * @description: TODO
 * @date ：2022/09/17 8:52
 */
@Data
public class FileSyncItem {
    private String dataName;
    private String fileName;
    private String state;

    public FileSyncItem() {
    }

    public FileSyncItem(String dataName, String fileName, String state) {
        this.setDataName(dataName);
        this.setFileName(fileName);
        this.setState(state);
    }
}
