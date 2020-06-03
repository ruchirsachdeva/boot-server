package com.myapp.controller;

import com.myapp.service.RelationshipService;
import com.myapp.service.exceptions.RelationshipNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com/"})
@RequestMapping("/api/relationships")
public class RelationshipController {

    private final RelationshipService relationshipService;

    @Autowired
    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com/"})
    @RequestMapping(value = "/to/{followedId}", method = RequestMethod.POST)
    public void follow(@PathVariable("followedId") Long followedId) {
        relationshipService.follow(followedId);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com/"})
    @RequestMapping(value = "/to/{followedId}", method = RequestMethod.DELETE)
    public void unfollow(@PathVariable("followedId") Long followedId) throws RelationshipNotFoundException {
        relationshipService.unfollow(followedId);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(RelationshipNotFoundException.class)
    public void handleRelationshipNotFound() {
    }

}
