package com.online_store.backend.api.user.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.online_store.backend.api.user.user.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
