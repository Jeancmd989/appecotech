package com.upc.appecotech.Services;

import com.upc.appecotech.entidades.Feedback;
import com.upc.appecotech.interfaces.IFeedbackService;
import com.upc.appecotech.repositorios.FeedbackRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService implements IFeedbackService {
    @Autowired
    private FeedbackRepositorio feedbackRepositorio;


    @Override
    public List<Feedback> listarFeedback() {
        return  feedbackRepositorio.findAll();
    }
}
