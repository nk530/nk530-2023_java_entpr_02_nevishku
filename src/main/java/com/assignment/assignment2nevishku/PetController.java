package com.assignment.assignment2nevishku;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PetController {
    private final PetRepository petRepository;

    @Autowired
    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        List<Pet> pets = (List<Pet>) petRepository.findAll();
        model.addAttribute("pets", pets);
        return "index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showAddForm(Model model) {
        model.addAttribute("pet", new Pet());
        return "add";
    }

    @PostMapping("/add")
    public String addPet(@Valid Pet pet, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add";
        }
        petRepository.save(pet);
        return "redirect:/";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String showEditForm(long id, Model model) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet Id: " + id));
        model.addAttribute("pet", pet);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePet(long id, @Valid Pet pet, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            pet.setId(id); // Set the ID to retain the URL parameter
            return "edit";
        }
        petRepository.save(pet);
        return "redirect:/";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deletePet(long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet Id: " + id));
        petRepository.delete(pet);
        return "redirect:/";
    }
}
