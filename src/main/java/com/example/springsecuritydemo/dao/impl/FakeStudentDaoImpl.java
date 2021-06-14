package com.example.springsecuritydemo.dao.impl;

import com.example.springsecuritydemo.dao.StudentDao;
import com.example.springsecuritydemo.domain.Student;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class FakeStudentDaoImpl implements StudentDao {
    private static final List<Student> STUDENTS = Lists.newArrayList(
            new Student(1, "James Bond"),
            new Student(2, "Maria Jones"),
            new Student(3, "Anna Smith")
    );

    @Override
    public Optional<Student> findStudentById(int id) {
        return STUDENTS.stream()
                .filter(student -> id == student.getId())
                .findFirst();
    }

    @Override
    public List<Student> findAll() {
        return STUDENTS;
    }

    @Override
    public void save(Student student) {
        STUDENTS.add(student);
    }

    @Override
    public boolean exists(Student student) {
        return STUDENTS
                .stream()
                .anyMatch(st -> st.getId().equals(student.getId()) && st.getName().equals(student.getName()));
    }

    @Override
    public boolean removeById(int id) {
        return STUDENTS.removeIf(student -> Objects.equals(id, student.getId()));
    }

    @Override
    public void update(Student student) {
        Objects.requireNonNull(student.getId(), "updated student must contains 'id'");
        removeById(student.getId());
        save(student);
    }
}
