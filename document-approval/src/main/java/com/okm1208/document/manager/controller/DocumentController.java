package com.okm1208.document.manager.controller;

import com.okm1208.document.common.model.CommonResponse;
import com.okm1208.document.manager.model.DocumentApprovalRequestVo;
import com.okm1208.document.manager.model.DocumentCreateRequestVo;
import com.okm1208.document.manager.model.DocumentCreateResponseVo;
import com.okm1208.document.manager.model.DocumentSearchResponseVo;
import com.okm1208.document.manager.service.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
@RestController
@RequestMapping(value = "/document")
@Api(tags = "문서 전자 결재 APIs")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    //문서 생성
    @ApiOperation(value = "문서 생성 API" , notes = "문서 생성")
    @PostMapping
    public CommonResponse<DocumentCreateResponseVo> create(@RequestBody @Valid DocumentCreateRequestVo documentCreateRequest){
        Long documentNo = documentService.create("admin", documentCreateRequest);
        return CommonResponse.success(new DocumentCreateResponseVo(documentNo));
    }


    //문서 결제
    @ApiOperation(value = "문서 결제 API" , notes = "문서 결재")
    @PostMapping(value = "/{no}/approval")
    public CommonResponse<Void> approval(@PathVariable Long no , @RequestBody DocumentApprovalRequestVo approvalRequestVo){
        documentService.approve("admin",no,approvalRequestVo.getApproveType(), approvalRequestVo.getComment());
        return CommonResponse.success();
    }

    //문서 조회

    @ApiOperation(value = "문서 조회 API" , notes = "문서 조회")
    @GetMapping
    public CommonResponse<DocumentSearchResponseVo> search(){
        return CommonResponse.success(documentService.search("admin"));
    }



}
