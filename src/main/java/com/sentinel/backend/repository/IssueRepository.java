package com.sentinel.backend.repository;

import com.sentinel.backend.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    // This gives us magic methods: save(), findAll(), findById()...
}