package com.internshipTest.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.internshipTest.exception.ResourceNotFoundException;
import com.internshipTest.model.Project;
import com.internshipTest.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private Project project;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(10));
    }

    @Test
    void testCreateProject() throws Exception {
        when(projectService.saveProject(any(Project.class))).thenReturn(project);

        ResultActions resultActions = mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project)));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Project"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void testGetAllProjects() throws Exception {
        when(projectService.getAllProjects()).thenReturn(Arrays.asList(project));

        ResultActions resultActions = mockMvc.perform(get("/api/projects")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Project"))
                .andExpect(jsonPath("$[0].description").value("Test Description"));
    }

    @Test
    void testGetProjectById() throws Exception {
        when(projectService.getProjectById(anyLong())).thenReturn(project);

        ResultActions resultActions = mockMvc.perform(get("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void testGetProjectById_NotFound() throws Exception {
        when(projectService.getProjectById(anyLong())).thenThrow(new ResourceNotFoundException("Project not found"));

        ResultActions resultActions = mockMvc.perform(get("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProjectById() throws Exception {
        Mockito.doNothing().when(projectService).deleteProjectById(anyLong());

        ResultActions resultActions = mockMvc.perform(delete("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().string("project deleted "));
    }

    @Test
    void testUpdateProject() throws Exception {
        Project updatedProject = new Project();
        updatedProject.setId(1L); // Make sure the ID is set
        updatedProject.setName("Updated Project");
        updatedProject.setDescription("Updated Description");
        updatedProject.setStartDate(LocalDate.now().plusDays(1));
        updatedProject.setEndDate(LocalDate.now().plusDays(11));

        when(projectService.updateProject(anyLong(), any(Project.class))).thenReturn(updatedProject);

        ResultActions resultActions = mockMvc.perform(put("/api/projects/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProject)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Project"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

}

