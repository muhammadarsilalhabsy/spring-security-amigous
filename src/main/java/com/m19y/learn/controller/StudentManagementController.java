package com.m19y.learn.controller;

import com.m19y.learn.model.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/management/api/v1/students")
public class StudentManagementController {

  private static final List<Student> STUDENTS = List.of(
          new Student(1, "John Doe"),
          new Student(2, "Jane Doe"),
          new Student(3, "Mike Doe"),
          new Student(4, "Jamal")
  );

// untuk @PreAuthorize
//  hasRole('ROLE_') hasAnyRole('ROLE_', 'ROLE_') hasAuthority(permission) hasAnyAuthority(permission)
  @GetMapping
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ADMINTRAINEE')")
  public List<Student> getStudents() {
    return STUDENTS;
  }

  @PostMapping
  @PreAuthorize("hasAuthority('student:write')")
  public void registerStudent(@RequestBody Student student) {

    System.out.println(student.getName() + " has been registered");
  }

  @PutMapping(path = "{studentId}")
  @PreAuthorize("hasAnyAuthority('course:write','student:write')")
  public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
    System.out.printf("student with id %s has been updated -> %s\n", studentId, student);
  }

  @DeleteMapping(path = "{studentId}")
  @PreAuthorize("hasAnyAuthority('course:write','student:write')")
  public void deleteStudent(@PathVariable("studentId") Integer studentId) {
    System.out.printf("student with id %s has been deleted\n", studentId);
  }
}
