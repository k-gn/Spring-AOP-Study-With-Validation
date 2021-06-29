package com.cos.person.web;

import com.cos.person.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 데이터를 리턴할 API Controller
@RequiredArgsConstructor
public class UserController {

    // 스프링의 장점! -> 타입을 통한 DI
    private final UserRepository userRepository;

    // 주소에 동사가 아닌 모델을 넣자!

    @GetMapping("/user")
    public ResponseEntity<CommonDto<List<User>>> findAll() {
        System.out.println("findAll");
        return new ResponseEntity<>(new CommonDto<>(HttpStatus.OK.value(), userRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<CommonDto<User>> findById(@PathVariable int id) {
        System.out.println("findById : " + id);
        return new ResponseEntity<>(new CommonDto<>(HttpStatus.OK.value(), userRepository.findById(id)), HttpStatus.OK);
    }

    // 내부에서 서버에 요청을 하는게 아닌 외부에서 자바스크립트로 서버에 요청을 보내는 것은 기본적으로 막아놓음 (CORS)
    @CrossOrigin // CORS 상관없어!!
    @PostMapping("/user")
    // x-www-form-urlencoded => request.getParameter() (기본값 - 그냥 받을 수 있음)
    // text/plain, application/json => @RequestBody
    public ResponseEntity<CommonDto<String>> save(@RequestBody JoinReqDto joinReqDto) {
        System.out.println("save : " + joinReqDto);
        userRepository.save(joinReqDto);
        return new ResponseEntity<>(new CommonDto<>(HttpStatus.OK.value(), "OK"), HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<CommonDto> delete(@PathVariable int id) {
        System.out.println("delete : " + id);
        userRepository.delete(id);
        return new ResponseEntity<>(new CommonDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<CommonDto> update(@RequestBody UpdateReqDto updateReqDto, @PathVariable int id) {
        System.out.println("update");
        userRepository.update(updateReqDto, id);
        return new ResponseEntity<>(new CommonDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }
}


