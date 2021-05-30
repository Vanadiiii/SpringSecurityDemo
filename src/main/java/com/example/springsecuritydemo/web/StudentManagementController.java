package com.example.springsecuritydemo.web;

import com.example.springsecuritydemo.domain.Student;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/management/api/v1/students")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
public class StudentManagementController {

    private static final List<Student> STUDENTS = Lists.newArrayList(
            new Student(1, "James Bond"),
            new Student(2, "Maria Jones"),
            new Student(3, "Anna Smith")
    );

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(STUDENTS);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void registerNewStudent(@RequestBody Student student) {
        boolean studentExist = STUDENTS.stream()
                .map(Student::getId)
                .anyMatch(integer -> Objects.equals(integer, student.getId()));
        if (!studentExist) {
            STUDENTS.add(student);
            log.error("register new Student - {}", student);
        } else {
            log.error("student with id {} already exists", student.getId());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('student:write')")
    public void deleteStudent(@PathVariable Integer id) {
        STUDENTS.removeIf(student -> Objects.equals(id, student.getId()));
        log.error("remove student with id - {}", id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        STUDENTS.removeIf(stdnt -> Objects.equals(id, stdnt.getId()));
        STUDENTS.add(student);
        log.error("update student with id - {} : {}", id, student);
    }
}
