package com.example.flipkartms;

import org.springframework.web.bind.annotation.GetMapping;

public class mens_cloths {
    @GetMapping("/mens_cloths")
    public String getData(){
        return "Please book your  hotel in goa  ticket, Choose Your Fav Airlines Idigo";
    }

}
