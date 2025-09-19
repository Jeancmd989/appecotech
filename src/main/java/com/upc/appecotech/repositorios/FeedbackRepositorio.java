package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FeedbackRepositorio extends JpaRepository<Feedback, Long> {
}
