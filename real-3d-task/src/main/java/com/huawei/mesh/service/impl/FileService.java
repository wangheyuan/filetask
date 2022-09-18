package com.huawei.mesh.service.impl;

import com.huawei.mesh.dto.FileDTO;
import com.huawei.mesh.service.IFileService;
import com.huawei.mesh.vo.FileDownItem;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ：why
 * @description: TODO
 * @date ：2022/09/18 2:12
 */
public class FileService implements IFileService {
    @Override
    public boolean uploadFileDTO(FileDTO fileDTO) {
        return false;
    }

    @Override
    public void downFile(String fileId, HttpServletResponse response) {

    }

    @Override
    public List<FileDownItem> fileIdList(String projectId) {
        return null;
    }
}
