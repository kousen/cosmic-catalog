package com.example.cosmiccatalog;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final ObservationRepository observationRepository;
    
    private final ObservationService observationService;
    
    private final ApprovalService approvalService;

    public HomeController(ObservationRepository observationRepository, ObservationService observationService, ApprovalService approvalService) {
        this.observationRepository = observationRepository;
        this.observationService = observationService;
        this.approvalService = approvalService;
    }

    @GetMapping("/")
    public String index(Model model) {
        // Get all observations, sorted by ID
        var observations = observationRepository.findAll(
            PageRequest.of(0, 20, Sort.by("id").descending())
        );
        
        // Get featured observations (top 3 by score)
        var featured = observationService.getFeaturedObservations(3);
        
        model.addAttribute("observations", observations.getContent());
        model.addAttribute("featured", featured);
        
        return "index"; // resolves to src/main/resources/templates/index.html
    }
    
    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id, 
                         @RequestParam(required = false) Integer version,
                         RedirectAttributes redirectAttributes) {
        try {
            approvalService.approve(id, version);
            redirectAttributes.addFlashAttribute("success", 
                "Observation #" + id + " approved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Failed to approve: " + e.getMessage());
        }
        return "redirect:/";
    }
}

