package me.vudb.backend.controller;

import me.vudb.backend.university.University;
import me.vudb.backend.university.UniversityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/university")
public class UniversityController {
    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<University> getAll(){
        return universityService.findAll();
    }
}
