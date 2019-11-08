package com.buysell.demo.service;

import com.buysell.demo.entity.User;
import com.buysell.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> listAll() {
        return (List<User>) userRepository.findAll();
    }

    public User get(Long id) {
        return userRepository.findById(id).get();
    }

    public User get(String name) {
        return userRepository.findByName(name);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
