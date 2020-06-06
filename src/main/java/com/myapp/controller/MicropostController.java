package com.myapp.controller;

import com.myapp.domain.Micropost;
import com.myapp.dto.MicropostParams;
import com.myapp.service.MicropostService;
import com.myapp.service.exceptions.NotPermittedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com"})
@RequestMapping("/api/microposts")
public class MicropostController {

    private final MicropostService micropostService;

    @Autowired
    public MicropostController(MicropostService micropostService) {
        this.micropostService = micropostService;
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com"})
    @RequestMapping(method = RequestMethod.POST)
    public Micropost create(@RequestBody MicropostParams params) {
        return micropostService.saveMyPost(params.toPost());
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws NotPermittedException {
        micropostService.delete(id);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotPermittedException.class)
    public void handleNoPermission() {
    }

}
