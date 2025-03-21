package org.htw.student.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.htw.student.models.Once;
import org.htw.student.repository.OnceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OnceService {
    private static final Logger log = LoggerFactory.getLogger(OnceService.class);
    @Autowired
    private OnceRepository onceRepository;

    @Autowired
    public OnceService(OnceRepository onceRepository) {
        this.onceRepository = onceRepository;
        log.info("✅ OnceService wurde erfolgreich erstellt!");
    }

    public List<Once> searchOnce(String searchOncie) {
        if (searchOncie == null || searchOncie.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return onceRepository.findByUsernameContainingIgnoreCase(searchOncie);
    }
}
