package com.stat.statserver;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {


    @GetMapping("/news/")
    public String rejectNews() {
        return "it works";
    }


}
