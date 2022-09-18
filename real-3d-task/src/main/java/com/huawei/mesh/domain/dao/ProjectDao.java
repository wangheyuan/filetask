package com.huawei.mesh.domain.dao;

import com.huawei.mesh.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDao extends JpaRepository<Project, String> {
}
