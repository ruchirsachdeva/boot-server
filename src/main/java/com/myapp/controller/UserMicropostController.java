package com.myapp.controller;

import com.myapp.dto.PageParams;
import com.myapp.dto.PostDTO;
import com.myapp.service.MicropostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com"})
@RequestMapping("/api/users")
public class UserMicropostController {

    private final MicropostService micropostService;

    @Autowired
    public UserMicropostController(MicropostService micropostService) {
        this.micropostService = micropostService;
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com"})
    @RequestMapping(method = RequestMethod.GET, path = "/{userId:\\d+}/microposts")
    public List<PostDTO> list(@PathVariable("userId") Long userId, PageParams pageParams) {
        return micropostService.findByUser(userId, pageParams);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com"})
    @RequestMapping(method = RequestMethod.GET, path = "/me/microposts")
    public List<PostDTO> list(PageParams pageParams) {
        return micropostService.findMyPosts(pageParams);
    }

}
