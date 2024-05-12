package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SpellbookController {
    private SpellbookRepository spellbookRepository;

    public SpellbookController(SpellbookRepository spellbookRepository)
    {
        this.spellbookRepository = spellbookRepository;
    }

    @GetMapping("/spellbooks")
    public List<Spellbook> getAllSpellbooks(){
        return (List<Spellbook>) spellbookRepository.findAll();
    }

    @PostMapping("/spellbooks")
    public void createSpellbook(@RequestBody Spellbook spellbook){
        spellbookRepository.save(spellbook);
    }

    @GetMapping("/spellbooks/{id}")
    public Spellbook getSpellbookById(@PathVariable Long id){
        return spellbookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spellbook not found with id " + id));
    }

    @PutMapping("/spellbooks/{id}")
    public Spellbook updateSpellbook(@RequestBody Spellbook newSpellbook, @PathVariable Long id){
        return spellbookRepository.findById(id)
                .map(spellbook -> {
                    spellbook.setName(newSpellbook.getName());
                    spellbook.setSpells(newSpellbook.getSpells());
                    return spellbookRepository.save(spellbook);
                })
                .orElseGet(() -> {
                    newSpellbook.setSpellbookId(id);
                    return spellbookRepository.save(newSpellbook);
                });
    }

    @DeleteMapping("/spellbooks/{id}")
    public void deleteSpellbook(@PathVariable Long id){
        spellbookRepository.deleteById(id);
    }

    @GetMapping("/spellbooks/bronze")
    public HashMap<String, Integer> getNumberOfSpellsByBooks(){
        HashMap<String, Integer> map = new HashMap<>();
        for(Spellbook s : (List<Spellbook>) spellbookRepository.findAll()){
            map.put(s.getName(), s.getSpells().size());
        }
        return map;
    }
}