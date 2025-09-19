package com.upc.appecotech.controllers;


import com.upc.appecotech.entidades.Feedback;
import com.upc.appecotech.interfaces.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FeedbackController {
    @Autowired
    private IFeedbackService feedbackService;

    @GetMapping("/feedbacks")
    public List<Feedback> listarFeedback()
    {
        return feedbackService.listarFeedback();
    }
}
