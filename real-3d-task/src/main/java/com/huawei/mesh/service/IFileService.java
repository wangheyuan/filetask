package com.huawei.mesh.service;

import com.huawei.mesh.dto.FileDTO;
import com.huawei.mesh.vo.FileDownItem;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IFileService {
    boolean uploadFileDTO(FileDTO fileDTO);

    /**
     * 根据fileId下载文件
     *
     * @param fileId
     * @param response
     */
    void downFile(String fileId, HttpServletResponse response);

    /**
     * 通过工程id 获取需要调用的文件id，让其下载
     *
     * @param projectId
     * @return
     */
    List<FileDownItem> fileIdList(String projectId);
}
