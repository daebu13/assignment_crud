package com.internshipTest.service;

import com.internshipTest.model.Project;

import java.util.List;


public interface ProjectService {
    Project saveProject(Project project);

    List<Project> getAllProjects();

    Project getProjectById(Long id);

    void deleteProjectById(Long id);

    Project updateProject(Long id, Project project);
}
