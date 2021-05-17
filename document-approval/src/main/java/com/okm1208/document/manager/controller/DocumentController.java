package com.okm1208.document.manager.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
@RestController
@RequestMapping(value = "/document")
public class DocumentController {

    //문서 저장
    @PostMapping
    @RequestMapping(value = "")
    public void create(){
    }


    //문서 결제
    @PostMapping
    @RequestMapping(value = "/{no}/approval")
    public void approval(@PathVariable Long no){
        // account -> 개인에 할당 되어 있는 문서 결제
    }
}
