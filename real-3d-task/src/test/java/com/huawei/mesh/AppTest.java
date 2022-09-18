package com.huawei.mesh;

import com.alibaba.fastjson.JSONObject;
import com.huawei.mesh.domain.dao.ProjectDao;
import com.huawei.mesh.domain.entity.InputDir;
import com.huawei.mesh.domain.entity.InputFile;
import com.huawei.mesh.domain.entity.Project;
import com.huawei.mesh.domain.state.DirState;
import com.huawei.mesh.domain.state.FileState;
import com.huawei.mesh.dto.ProjectDirDTO;
import com.huawei.mesh.dto.ProjectSyncDTO;
import com.huawei.mesh.service.impl.ProjectService;
import com.huawei.mesh.vo.ProjectSyncVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest 
{

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectDao projectDao;

    /**
     * Rigorous Test :-)
     */
    @Test
    public void addProject()
    {
        ProjectSyncDTO projectDTO = new ProjectSyncDTO();
        projectDTO.setProjectName("test11");
        List<ProjectDirDTO> dirs = new ArrayList<>();
        projectDTO.setDirs(dirs);
        for (int i=1; i<3; i++) {
           ProjectDirDTO dir = new ProjectDirDTO();
           dir.setDataName("chunk" + i);
           List<String> fileList = new ArrayList<>();
           for (int j=1; j< 3; j++) {
               fileList.add("test_chunk" +i + "_file" + j + ".txt");
           }
           dir.setFileNames(fileList);
           dirs.add(dir);
        }
        System.out.println(JSONObject.toJSONString(projectDTO));
        ProjectSyncVo projectSyncVo = projectService.syncProjectFile(projectDTO);
        System.out.println(JSONObject.toJSONString(projectSyncVo));
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void updateProject()
    {
        ProjectSyncDTO projectDTO = new ProjectSyncDTO();
        projectDTO.setProjectId("4");
        projectDTO.setProjectName("test2");
        List<ProjectDirDTO> dirs = new ArrayList<>();
        projectDTO.setDirs(dirs);
        for (int i=2; i<4; i++) {
            ProjectDirDTO dir = new ProjectDirDTO();
            dir.setDataName("chunk" + i);
            List<String> fileList = new ArrayList<>();
            for (int j=1; j< 3; j++) {
                fileList.add("test_chunk"  + "_file" + j + ".txt");
            }
            dir.setFileNames(fileList);
            dirs.add(dir);
        }
        ProjectSyncVo projectSyncVo = projectService.syncProjectFile(projectDTO);
        System.out.println(JSONObject.toJSONString(projectSyncVo));
    }

    @Test
    public void addProjectByData() {
        Project project = new Project();
        project.setProjectId("4");
        project.setProjectName("test2");
        project.setState(DirState.READ);
        List<InputDir> inputDirs = new ArrayList<>();
        for(int i=1; i<3 ; i++) {
            InputDir inputDir = new InputDir();
            inputDirs.add(inputDir);
            inputDir.setDirId(UUID.randomUUID().toString());
            inputDir.setProject(project);
            inputDir.setState(DirState.READ);
            inputDir.setDirName("chunk" + i);
            List<InputFile> inputFileList = new ArrayList<>();
            inputDir.setFiles(inputFileList);
            for(int j=1; j< 3; j++) {
                InputFile inputFile = new InputFile();
                inputFile.setInputId(UUID.randomUUID().toString());
                inputFile.setFileName("test_chunk" + "_file" + j + ".txt");
                inputFile.setState(FileState.READ);
                inputFile.setInputDir(inputDir);
                inputFileList.add(inputFile);
            }
        }
        project.setDirs(inputDirs);
        Project result = projectDao.save(project);
        System.out.println(JSONObject.toJSONString(result));

    }
}
