package com.internshipTest.service.serviceImpl;



import com.internshipTest.exception.ResourceNotFoundException;
import com.internshipTest.model.Project;
import com.internshipTest.repository.ProjectRepository;
import com.internshipTest.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(10));
    }

    @Test
    void testSaveProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        Project savedProject = projectService.saveProject(project);
        assertNotNull(savedProject);
        assertEquals(project.getName(), savedProject.getName());
    }

    @Test
    void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project));
        List<Project> projects = projectService.getAllProjects();
        assertEquals(1, projects.size());
        assertEquals(project.getName(), projects.get(0).getName());
    }

    @Test
    void testGetProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        Project foundProject = projectService.getProjectById(1L);
        assertNotNull(foundProject);
        assertEquals(project.getName(), foundProject.getName());
    }

    @Test
    void testGetProjectById_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> projectService.getProjectById(1L));
    }

    @Test
    void testDeleteProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        doNothing().when(projectRepository).delete(project);
        projectService.deleteProjectById(1L);
        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    void testDeleteProjectById_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> projectService.deleteProjectById(1L));
    }

    @Test
    void testUpdateProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project projectDetails = new Project();
        projectDetails.setName("Updated Project");
        projectDetails.setDescription("Updated Description");
        projectDetails.setStartDate(LocalDate.now().plusDays(1));
        projectDetails.setEndDate(LocalDate.now().plusDays(11));

        Project updatedProject = projectService.updateProject(1L, projectDetails);
        assertNotNull(updatedProject);
        assertEquals("Updated Project", updatedProject.getName());
        assertEquals("Updated Description", updatedProject.getDescription());
    }

    @Test
    void testUpdateProject_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        Project projectDetails = new Project();
        assertThrows(ResourceNotFoundException.class, () -> projectService.updateProject(1L, projectDetails));
    }
}
