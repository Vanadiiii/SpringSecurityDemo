package com.example.springsecuritydemo.web;

import com.example.springsecuritydemo.dao.StudentDao;
import com.example.springsecuritydemo.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentDao studentDao;

    @GetMapping("/{id}")
    public ResponseEntity<Student> findStudentById(@PathVariable int id) {
        return studentDao.findStudentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Student " + id + " does not exist"));
    }
}
