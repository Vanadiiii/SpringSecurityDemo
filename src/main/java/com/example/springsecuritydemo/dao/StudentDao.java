package com.example.springsecuritydemo.dao;

import com.example.springsecuritydemo.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {
    Optional<Student> findStudentById(int id);

    List<Student> findAll();

    void save(Student student);

    boolean exists(Student student);

    boolean removeById(int id);

    void update(Student student);
}
