package com.internshipTest.controller;

import com.internshipTest.model.Project;
import com.internshipTest.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) {
        //i can also make a dto of project and send response but for now i am using Project Entity only
        Project projects = projectService.saveProject(project);
        return new ResponseEntity<>(projects, HttpStatus.CREATED);

    }

    @GetMapping
    public List<Project> getAllProjects(){
        return projectService.getAllProjects();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id){
        Project projectById = projectService.getProjectById(id);
        return new ResponseEntity<>(projectById,HttpStatus.OK);
    }


    //delete project
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProjectById(@PathVariable Long id){
        projectService.deleteProjectById(id);
        return new ResponseEntity<>("project deleted ",HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id,@Valid @RequestBody Project projectDetails){
        Project project = projectService.updateProject(id, projectDetails);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }




}
