package io.springbatch.spring_batch.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/scheduler")
    public String scheduler(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "scheduler";
    }
}
