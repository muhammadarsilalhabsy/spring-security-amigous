package com.m19y.learn.controller;

import com.m19y.learn.model.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

  private static final List<Student> STUDENTS = List.of(
          new Student(1, "John Doe"),
          new Student(2, "Jane Doe"),
          new Student(3, "Mike Doe")
  );

  @GetMapping(path = "{studentId}")
  public Student getStudent(@PathVariable("studentId") Integer studentId) {
    return STUDENTS.stream()
            .filter(student -> studentId.equals(student.getStudentId()))
            .findFirst()
            .orElseThrow(()-> new IllegalStateException("Student not found!"));
  }
}
