package com.example.library_management.service;

import com.example.library_management.dto.PublisherRequest;
import com.example.library_management.dto.PublisherResponse;
import com.example.library_management.entity.Publisher;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.PublisherRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    private final PublisherRepository repository;
    PublisherService(PublisherRepository repository){
        this.repository=repository;
    }
    private PublisherResponse pack(Publisher publisher){
        PublisherResponse response=new PublisherResponse();
        response.setId(publisher.getId());
        response.setName(publisher.getName());
        response.setPhone(publisher.getPhone());
        response.setAddress(publisher.getAddress());
       return response;
    }
    public PublisherResponse create(PublisherRequest request){
        Publisher publisher=new Publisher();
        publisher.setName(request.getName());
        publisher.setPhone(request.getPhone());
        publisher.setAddress(request.getAddress());
        Publisher publisherRes=repository.save(publisher);
        PublisherResponse response=new PublisherResponse();
        response.setId(publisherRes.getId());
        response.setName(publisherRes.getName());
        response.setPhone(publisherRes.getPhone());
        response.setAddress(publisherRes.getAddress());
     return  response;
    }

    public Page<PublisherResponse> getAll(int page, int size){
        Pageable pageable= PageRequest.of(page, size);
        Page<Publisher> publishers=repository.findAll(pageable);
           return publishers.map(this::pack);
    }
    public PublisherResponse update(Long id,PublisherRequest request){
        Publisher publisher=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("not found"));
        if(request.getAddress()!=null)publisher.setAddress(request.getAddress());
        if(request.getPhone()!=null)publisher.setPhone(request.getPhone());
        if(request.getName()!=null)publisher.setName(request.getName());
        Publisher publisherRes=repository.save(publisher);
        PublisherResponse response=new PublisherResponse();
        response.setId(publisherRes.getId());
        response.setName(publisherRes.getName());
        response.setPhone(publisherRes.getPhone());
        response.setAddress(publisherRes.getAddress());
        return  response;
    }
}
