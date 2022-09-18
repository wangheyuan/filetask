package com.huawei.mesh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huawei.mesh.domain.dao.ProjectDao;
import com.huawei.mesh.domain.entity.InputDir;
import com.huawei.mesh.domain.entity.InputFile;
import com.huawei.mesh.domain.entity.Project;
import com.huawei.mesh.domain.state.DirState;
import com.huawei.mesh.domain.state.FileState;
import com.huawei.mesh.dto.ProjectDirDTO;
import com.huawei.mesh.dto.ProjectSyncDTO;
import com.huawei.mesh.service.IProjectService;
import com.huawei.mesh.vo.FileSyncItem;
import com.huawei.mesh.vo.OpType;
import com.huawei.mesh.vo.ProjectSyncVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：why
 * @description: TODO
 * @date ：2022/09/17 8:34
 */
@Slf4j
@Service
public class ProjectService implements IProjectService {
    @Autowired
    private ProjectDao projectDao;

    @Override
    public ProjectSyncVo syncProjectFile(ProjectSyncDTO projectSyncDTO) {
        if (StringUtils.isEmpty(projectSyncDTO.getProjectId())) {
            return projectToVo(addAllFile(projectSyncDTO), true);
        }
        Optional<Project> projectOpl = projectDao.findById(projectSyncDTO.getProjectId());
        if (!projectOpl.isPresent()) {
            return new ProjectSyncVo();
        }
        Project project = projectOpl.get();
        ProjectSyncVo projectOldVo = projectToVo(project, false);
        ProjectSyncVo projectNewVo = projectDtoToVo(projectSyncDTO);
        ProjectSyncVo resultVo = compare(projectOldVo, projectNewVo);
        // 更新当前需要更新的文件的状态
//        syncFileState(resultVo, project);
        return resultVo;
    }

    private Project addAllFile(ProjectSyncDTO projectSyncDTO) {
        Project project = new Project();
        project.setProjectId(UUID.randomUUID().toString());
        project.setProjectName(projectSyncDTO.getProjectName());
        List<ProjectDirDTO> inputDirs = projectSyncDTO.getDirs();
        if (!CollectionUtils.isEmpty(inputDirs)) {
            List<InputDir> addDirs =inputDirs.stream().map(d->{
                InputDir dir = new InputDir();
                String dirId = UUID.randomUUID().toString();
                dir.setDirId(dirId);
                dir.setProject(project);
                dir.setState(DirState.PRE_READY);
                dir.setDirName(d.getDataName());
                if (!CollectionUtils.isEmpty(d.getFileNames())) {
                    List<InputFile> addFiles = d.getFileNames().stream().map(f->{
                        InputFile file = new InputFile();
                        file.setInputId(UUID.randomUUID().toString());
                        file.setFileName(f);
                        file.setState(FileState.PRE_READY);
                        file.setInputDir(dir);
                        return file;
                    }).collect(Collectors.toList());
                    dir.setFiles(addFiles);
                }
                return dir;
            }).collect(Collectors.toList());
            project.setDirs(addDirs);
        }
        log.info("-----------project--------------");
        log.info(JSONObject.toJSONString(project));
        Project result = projectDao.save(project);
        log.info("-----------projectVo--------------");
        log.info(JSONObject.toJSONString(result));
        return result;
    }

    /**
     * 获取 新增的文件列表对象 或者 获取当前已经存在的文件列表对象
     *
     * @param project
     * @param isAll true 表示 所有的新增， false 表示已经同步好的信息
     * @return
     */
    private ProjectSyncVo projectToVo(Project project, boolean isAll) {
        ProjectSyncVo projectSyncVo = new ProjectSyncVo();
        projectSyncVo.setProjectId(project.getProjectId());

        List<FileSyncItem> items = new ArrayList<>();
        List<InputDir> inputDirs = project.getDirs();
        if (!CollectionUtils.isEmpty(inputDirs)) {
            List<FileSyncItem> dItems = inputDirs.stream().filter(d-> {
                if (isAll || d.getState() == DirState.READ) {
                    return true;
                }
                return false;
            }).map(d->{
                FileSyncItem dItem = new FileSyncItem();
                dItem.setDataName(d.getDirName());
                dItem.setState(OpType.ADD.name());
                return dItem;
            }).collect(Collectors.toList());
            items.addAll(dItems);
            inputDirs.stream().forEach(d ->{
                if (CollectionUtils.isEmpty(d.getFiles())) {
                    return;
                }
                List<FileSyncItem> fileSyncItems = d.getFiles().stream().filter(f -> {
                    if (isAll || f.getState() == FileState.READ) {
                        return true;
                    }
                    return false;
                }).map(f -> {
                    FileSyncItem fItem = new FileSyncItem();
                    fItem.setFileName(f.getFileName());
                    fItem.setState(OpType.ADD.name());
                    fItem.setDataName(d.getDirName());
                    return fItem;
                }).collect(Collectors.toList());
                items.addAll(fileSyncItems);
            });
        }
        projectSyncVo.setItems(items);
        if (!CollectionUtils.isEmpty(items)) {
            projectSyncVo.setState(DirState.PRE_READY.name());
        } else {
            projectSyncVo.setState(DirState.READ.name());
        }
        return projectSyncVo;
    }

    private ProjectSyncVo projectDtoToVo(ProjectSyncDTO projectSyncDTO) {
        ProjectSyncVo projectSyncVo = new ProjectSyncVo();
        projectSyncVo.setProjectId(projectSyncDTO.getProjectId());
        projectSyncVo.setProjectName(projectSyncDTO.getProjectName());
        List<FileSyncItem> items = new ArrayList<>();
        List<ProjectDirDTO> inputDirs = projectSyncDTO.getDirs();

        if (!CollectionUtils.isEmpty(inputDirs)) {
            List<FileSyncItem> dItems = inputDirs.stream().map(d -> {
                FileSyncItem dItem = new FileSyncItem();
                dItem.setDataName(d.getDataName());
                dItem.setFileName("");
                return dItem;
            }).collect(Collectors.toList());
            items.addAll(dItems);

            inputDirs.stream().forEach(d -> {
                if (CollectionUtils.isEmpty(d.getFileNames())) {
                    return;
                }
                List<FileSyncItem> fItems = d.getFileNames().stream().map(f -> {
                    FileSyncItem fItem = new FileSyncItem();
                    fItem.setDataName(d.getDataName());
                    fItem.setFileName(f);
                    return fItem;
                }).collect(Collectors.toList());
                items.addAll(fItems);
            });
        }
        projectSyncVo.setItems(items);
        return projectSyncVo;
    }

    /**
     *
     * @param old 原来的结构
     * @param now 现在需要的机构
     * @return
     */
    private ProjectSyncVo compare(ProjectSyncVo old, ProjectSyncVo now) {
        log.info("-----------old----------------");
        System.out.println(JSONObject.toJSONString(old));
        log.info("-----------now----------------");
        System.out.println(JSONObject.toJSONString(now));
        ProjectSyncVo projectSyncVo = new ProjectSyncVo();
        projectSyncVo.setProjectId(now.getProjectId());
        projectSyncVo.setProjectName(now.getProjectName());
        List<FileSyncItem> syncItems = new ArrayList<>();
        projectSyncVo.setItems(syncItems);
        Set<String> oldDirs = old.getItems().stream().filter(item->
                 StringUtils.isEmpty(item.getFileName()))
                .map(item-> item.getDataName()).collect(Collectors.toSet());
        Set<String> nowDirs = now.getItems().stream().filter(item->
                        StringUtils.isEmpty(item.getFileName()))
                .map(item-> item.getDataName()).collect(Collectors.toSet());

        Map<String, List<FileSyncItem>> oldFileMap = old.getItems().stream().filter(item ->
                !StringUtils.isEmpty(item.getFileName()) && !StringUtils.isEmpty(item.getDataName()))
                .collect(Collectors.groupingBy(FileSyncItem::getDataName));

        Map<String, List<FileSyncItem>> nowFileMap = now.getItems().stream().filter(item ->
                        !StringUtils.isEmpty(item.getFileName()) && !StringUtils.isEmpty(item.getDataName()))
                .collect(Collectors.groupingBy(FileSyncItem::getDataName));

        Set<String> removeDir = new HashSet<>();
        removeDir.addAll(oldDirs);
        removeDir.removeAll(nowDirs);
        // 处理需要移除的目录
        List<FileSyncItem> removeDItems = removeDir.stream().map(d-> {
            FileSyncItem dItem = new FileSyncItem();
            dItem.setDataName(d);
            dItem.setState(OpType.DELETE.name());
            return dItem;
        }).collect(Collectors.toList());
        syncItems.addAll(removeDItems);

        Set<String> addDir = new HashSet<>();
        addDir.addAll(nowDirs);
        addDir.removeAll(oldDirs);
        // 处理需要新增的目录
        List<FileSyncItem> addDItems = addDir.stream().map(d-> {
            FileSyncItem dItem = new FileSyncItem();
            dItem.setDataName(d);
            dItem.setState(OpType.ADD.name());
            return dItem;
        }).collect(Collectors.toList());
        syncItems.addAll(addDItems);

        // 处理新增目录的文件
        addDir.stream().forEach(d -> {
            nowFileMap.get(d).stream().forEach(f-> {
                FileSyncItem fItem = new FileSyncItem();
                fItem.setState(OpType.ADD.name());
                fItem.setDataName(d);
                fItem.setFileName(f.getFileName());
                syncItems.add(fItem);
            });
        });

        // 处理更新目录的操作
        Set<String> updateDir = new HashSet<>();
        updateDir.addAll(addDir);
        updateDir.retainAll(nowDirs);
        updateDir.stream().forEach(d-> {
            Set<String> oldFileSet = new HashSet<>();
            if (!CollectionUtils.isEmpty(oldFileMap.get(d))) {
                oldFileSet = oldFileMap.get(d).stream()
                        .map(f->f.getFileName()).collect(Collectors.toSet());
            }
            Set<String> nowFileSet = new HashSet<>();
            if (CollectionUtils.isEmpty(nowFileMap.get(d))) {
                nowFileSet = nowFileMap.get(d).stream()
                        .map(f -> f.getFileName()).collect(Collectors.toSet());
            }
            Set<String> removeFiles = new HashSet<>();
            removeFiles.addAll(oldFileSet);
            removeFiles.removeAll(nowFileSet);
            List<FileSyncItem> rItems = removeFiles.stream().map(f -> new FileSyncItem(d, f, OpType.DELETE.name()))
                    .collect(Collectors.toList());
            syncItems.addAll(rItems);
            Set<String> addFiles = new HashSet<>();
            addFiles.addAll(nowFileSet);
            addFiles.removeAll(oldFileSet);
            List<FileSyncItem> addItems = addFiles.stream().map(f -> new FileSyncItem(d, f, OpType.DELETE.name()))
                    .collect(Collectors.toList());;
            syncItems.addAll(addItems);
        });
        return projectSyncVo;
    }

    private void syncFileState(ProjectSyncVo projectSyncVo, Project project) {
        log.info("-----------result----------");
        log.info(JSONObject.toJSONString(projectSyncVo));
        project.setState(DirState.PRE_READY);
        project.setProjectName(projectSyncVo.getProjectName());
        List<InputDir> inputDirs = project.getDirs();

        projectSyncVo.getItems().stream().forEach(f -> {
            if (StringUtils.isEmpty(f.getFileName())) {
                Optional<InputDir> inputDirOpl = getDirByName(inputDirs, f.getDataName());
                if (!inputDirOpl.isPresent()) {
                    return;
                }
                InputDir inputDir = inputDirOpl.get();
                inputDir.setState(DirState.PRE_READY);
            } else {
                Optional<InputFile> inputFileOpl = getFileByName(inputDirs, f.getDataName(), f.getFileName());
                if (!inputFileOpl.isPresent()) {
                    return;
                }
                InputFile inputFile = inputFileOpl.get();
                inputFile.setState(FileState.PRE_READY);
            }
        });
        projectDao.save(project);
    }

    private Optional<InputDir> getDirByName(List<InputDir> dirs, String dirName) {
        if (CollectionUtils.isEmpty(dirs) || StringUtils.isEmpty(dirName)) {
            return Optional.empty();
        }
        for(InputDir dir: dirs) {
            if (dirName.equals(dir.getDirName())) {
                return Optional.of(dir);
            }
        }
        return Optional.empty();
    }

    private Optional<InputFile> getFileByName(List<InputDir> dirs,String dirName, String fileName) {
        if (CollectionUtils.isEmpty(dirs) || StringUtils.isEmpty(fileName) || StringUtils.isEmpty(dirName)) {
            return Optional.empty();
        }
        for(InputDir dir: dirs) {
           if (dirName.equals(dir.getDirName())) {
               List<InputFile> files = dir.getFiles();
               if (CollectionUtils.isEmpty(files)) {
                   return Optional.empty();
               }
               for (InputFile file: files) {
                   if (fileName.equals(file.getFileName())) {
                       return Optional.of(file);
                   }
               }
           }
        }
        return Optional.empty();
    }
}
