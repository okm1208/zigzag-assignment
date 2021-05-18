package com.okm1208.document.manager.controller;

import com.okm1208.document.common.model.CommonResponse;
import com.okm1208.document.manager.model.DocumentCreateRequestVo;
import com.okm1208.document.manager.model.DocumentCreateResponseVo;
import com.okm1208.document.manager.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
@RestController
@RequestMapping(value = "/document")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    //문서 저장
    @PostMapping
    @RequestMapping(value = "")
    public CommonResponse<DocumentCreateResponseVo> create(@RequestBody @Valid DocumentCreateRequestVo documentCreateRequest){
        Long documentNo = documentService.create("admin", documentCreateRequest);
        return CommonResponse.success(new DocumentCreateResponseVo(documentNo));
    }


    //문서 결제
    @PostMapping
    @RequestMapping(value = "/{no}/approval")
    public void approval(@PathVariable Long no){
        // account -> 개인에 할당 되어 있는 문서 결제
    }
}
