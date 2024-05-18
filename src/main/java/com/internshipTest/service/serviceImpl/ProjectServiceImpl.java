package com.internshipTest.service.serviceImpl;

import com.internshipTest.exception.ResourceNotFoundException;
import com.internshipTest.model.Project;
import com.internshipTest.repository.ProjectRepository;
import com.internshipTest.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;


    @Override
    public Project saveProject(Project project) {
       return projectRepository.save(project);

    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Project not found with id " + id)
        );
        return project;



    }

    @Override
    public void deleteProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Project not found with id " + id)
        );
        projectRepository.delete(project);
    }

    @Override
    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Project not found with id " + id)
        );

        // we can also use ModelMapper
       project.setName(projectDetails.getName());
       project.setDescription(projectDetails.getDescription());
       project.setStartDate(projectDetails.getStartDate());
       project.setEndDate(projectDetails.getEndDate());
       return projectRepository.save(project);


    }
}
