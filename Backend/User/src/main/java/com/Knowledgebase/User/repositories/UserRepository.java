package com.Knowledgebase.User.repositories;

import com.Knowledgebase.User.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmail(String email);

    List<User> findByNameIn(Set<String> names);

}
