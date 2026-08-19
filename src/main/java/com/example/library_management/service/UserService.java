package com.example.library_management.service;

import com.example.library_management.dto.UserResponse;
import com.example.library_management.dto.UserUpdateRequest;
import com.example.library_management.entity.User;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.UserRepository;
import com.example.library_management.specification.UserSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
        private UserResponse packUser(User user){
               UserResponse response=new UserResponse();
               response.setId(user.getId());
               response.setName(user.getName());
               response.setEmail(user.getEmail());
               response.setRole(user.getRole());
               response.setStatus(user.getStatus());
               response.setAddress(user.getAddress());
               response.setMembershipNumber(user.getMembershipNumber());
               return response;
           }
    private final UserRepository repository;
    public UserResponse getById(Long id){
     User user=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
         return packUser(user);
       }
       public Page<UserResponse> getAll(int page,int size){
           Pageable pageable= PageRequest.of(page, size);
        Page<User> users=repository.findAll(pageable);
        return users.map(this::packUser);
       }
    public void approveUser(Long id){
        User user=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("user not found"));
        user.setStatus(User.Status.ACTIVE);
        repository.save(user);
    }
    public void suspendUser(Long id){
        User user=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("user not found"));
        user.setStatus(User.Status.SUSPENDED);
        repository.save(user);
    }
    public void changeRole(Long id,User.Role role){
            User user=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("user not found"));
            user.setRole(role);
            repository.save(user);
    }
@Transactional
 public UserResponse updateUser(Long id,UserUpdateRequest request){
        if(request.getAddress()==null && request.getName()==null&&request.getEmail()==null){
            throw new IllegalArgumentException("Nothing to update");
        }
     if (request.getEmail()!=null&&repository.existsByEmail(request.getEmail())) {
         throw new ResourceNotFoundException("Email already exists.");
     }
        User user=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("user not found"));
              if(request.getEmail()!=null){
                  user.setEmail(request.getEmail());
              }
              if(request.getName()!=null){
                  user.setName(request.getName());
              }
              if(request.getAddress()!=null){
                  user.setAddress(request.getAddress());
              }
        return packUser(repository.save(user));
    }
public  Page<UserResponse>search(
        String name,
        String email,
        String membershipNumber,
        User.Role role,
        User.Status status,
        int page,
        int size
){
    Specification<User>specification= UserSpecification.search(name,email,membershipNumber,role,status);
   Pageable pageable=PageRequest.of(page,size);
   return repository.findAll(specification,pageable).map(this::packUser);
}
}
