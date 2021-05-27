package com.example.springsecuritydemo.web;

import com.example.springsecuritydemo.domain.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "James Bond"),
            new Student(2, "Maria Jones"),
            new Student(3, "Anna Smith")
    );

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> findStudentById(@PathVariable Integer studentId) {
        return STUDENTS.stream()
                .filter(student -> Objects.equals(studentId, student.getId()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Student " + studentId + " does not exist"));
    }
}
