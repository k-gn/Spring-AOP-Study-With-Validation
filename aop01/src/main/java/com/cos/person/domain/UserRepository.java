package com.cos.person.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "ssar", "1234", "0101111"));
        users.add(new User(2, "cos", "1234", "0101111"));
        users.add(new User(3, "love", "1234", "0101111"));
        return users;
    }

    public User findById(int id) {
        return new User(1, "ssar", "1234", "0101111");
    }

    public void save(JoinReqDto joinReqDto) {
        System.out.println("DB - INSERT");
    }

    public void delete(int id) {
        System.out.println("DB - DELETE");
    }

    public void update(UpdateReqDto updateReqDto, int id) {
        System.out.println("DB - UPDATE");
        throw new IllegalArgumentException();
    }
}
