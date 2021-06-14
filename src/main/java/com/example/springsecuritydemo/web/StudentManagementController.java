package com.example.springsecuritydemo.web;

import com.example.springsecuritydemo.dao.StudentDao;
import com.example.springsecuritydemo.domain.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/management/api/v1/students")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
@RequiredArgsConstructor
public class StudentManagementController {
    private final StudentDao studentDao;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentDao.findAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void registerNewStudent(@RequestBody Student student) {
        if (!studentDao.exists(student)) {
            studentDao.save(student);
            log.info("register new Student - {}", student);
        } else {
            log.error("student with id {} already exists", student.getId());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('student:write')")
    public void deleteStudent(@PathVariable Integer id) {
        log.info("removing student with id - {}", id);
        studentDao.removeById(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(@RequestBody Student student) {
        int id = student.getId();
        studentDao.update(student);
        log.info("update student with id - {} : {}", id, student);
    }
}
