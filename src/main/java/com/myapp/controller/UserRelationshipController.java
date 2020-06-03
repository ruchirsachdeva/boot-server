package com.myapp.controller;

import com.myapp.dto.PageParams;
import com.myapp.dto.RelatedUserDTO;
import com.myapp.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com"})
@RequestMapping("/api/users/{userId}")
public class UserRelationshipController {

    private final RelationshipService relationshipService;

    @Autowired
    public UserRelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com"})
    @RequestMapping(method = RequestMethod.GET, path = "/followings")
    public List<RelatedUserDTO> followings(@PathVariable("userId") long userId, PageParams pageParams) {
        return relationshipService.findFollowings(userId, pageParams);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com"})
    @RequestMapping(method = RequestMethod.GET, path = "/followers")
    public List<RelatedUserDTO> followers(@PathVariable("userId") long userId, PageParams pageParams) {
        return relationshipService.findFollowers(userId, pageParams);
    }

}
