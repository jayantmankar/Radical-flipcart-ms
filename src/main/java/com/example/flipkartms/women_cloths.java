package com.example.flipkartms;

import org.springframework.web.bind.annotation.GetMapping;

public class women_cloths {
    @GetMapping("/women_cloths")
    public String getData(){
        return "Please book your  hotel in goa  ticket, Choose Your Fav Airlines Idigo";
    }

}
