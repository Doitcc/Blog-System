package com.caoxin.controller;

import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.Link;
import com.caoxin.domain.vo.OrdinaryLinkVo;
import com.caoxin.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/getLinkAdmin")
    public ResponseResult getAllLinkAdmin() {

        return linkService.getAllLinkAdmin();
    }

    @GetMapping("/getLinkOrdinary")
    public ResponseResult getAllLinkOrdinary() {

        return linkService.getAllLinkOrdinary();
    }
    
    @PutMapping("/editLink")
    public ResponseResult editLinkStatus(Long id,String status){
        return linkService.editLinkStatus(id,status);
    }
    
    @DeleteMapping("/deleteLink")
    public ResponseResult deleteLink(Long id){
        return linkService.deleteLink(id);
    }
    
    @GetMapping("/queryStatus")
    public ResponseResult queryStatus(){
        return linkService.queryStatus();
    }
    
    @PostMapping("/applyLink")
    public ResponseResult applyLink(@RequestBody Link link){
        return linkService.applyLink(link);
    }
    
    
    
    @GetMapping("/queryStatusSelf")
    public ResponseResult queryStatusSelf(){
        return linkService.queryStatusSelf();
    }
    
    @PutMapping("/modifyLink")
    public ResponseResult modifyLink(@RequestBody OrdinaryLinkVo ordinaryLinkVo) {
        return linkService.modifyLink(ordinaryLinkVo);
    }
}
