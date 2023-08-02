package com.example.pagenation.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pagenation.model.Pager;
import com.example.pagenation.repository.PageRepository;



@RestController
@RequestMapping("/api")
public class PageController {
    

    @Autowired
    PageRepository pageRepository;

    @GetMapping("/show")
    public List<Pager> getAllPager(){
        return (List<Pager>) pageRepository.findAll();
    }

   @PostMapping("/create")
   public ResponseEntity<Pager> createpage(@RequestBody Pager paging){
    Pager _page=pageRepository.save(new Pager(paging.getName(),paging.getCity(),paging.getPassword()));
    return new ResponseEntity<Pager>(_page, HttpStatus.OK);
   }


   @DeleteMapping("/Delete")
   public ResponseEntity<HttpStatus> deletepage(){
    pageRepository.deleteAll();
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

   @PutMapping("/update/{id}")
   public ResponseEntity<Pager> updatePage(@PathVariable Long id,@RequestBody Pager pagging){
    Optional<Pager> _pagging=pageRepository.findById(id);
    if(_pagging.isPresent()){
        Pager paggingid=_pagging.get();
        paggingid.setName(pagging.getName());
        paggingid.setCity(pagging.getCity());
        paggingid.setPassword(pagging.getPassword());
        return new ResponseEntity<>(pageRepository.save(paggingid), HttpStatus.OK);  
    }else{
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

   }

   private Sort.Direction getSortDirection(String sort){
    if(sort.equals("asc")){
        return Sort.Direction.ASC;
    }else if(sort.equals("desc")){
        return Sort.Direction.DESC;
    }
    return Sort.Direction.ASC;
   }


   @GetMapping("/pagination")
   public ResponseEntity<Map<String,Object>> getAllPage(
    @RequestParam(defaultValue="0") int page,
    @RequestParam(defaultValue = "2") int size,
    @RequestParam(defaultValue = "id,asc")String[] sort){


        List<Order>order = new ArrayList<Order>();

        if(sort[0].contains(",")){
           for(String sortorder:sort){
            String[] _sort=sortorder.split(",");
             order.add(new Order(getSortDirection(_sort[1]),_sort[0]));
           }

        }else{
             order.add(new Order(getSortDirection(sort[1]),sort[0]));
        }

    List<Pager> paging =new ArrayList<Pager>();
    Pageable pagingsort =PageRequest.of(page,size,Sort.by(order));

    Page<Pager> pageTuts;
    pageTuts = pageRepository.findAll(pagingsort);
    
    paging = pageTuts.getContent();

    Map<String, Object> response=new HashMap<>();
    response.put("Tutorials",paging);
    response.put("currentpage",pageTuts.getNumber());
    response.put("pagesize",pageTuts.getTotalElements());
    response.put("totalpage",pageTuts.getTotalPages());

    return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
