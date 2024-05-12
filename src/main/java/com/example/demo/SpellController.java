package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SpellController {
    private SpellRepository spellRepository;
    private List<String> bannedSpells;

    public SpellController(SpellRepository spellRepository)
    {
        this.spellRepository = spellRepository;
        this.bannedSpells = new ArrayList<>();
        this.bannedSpells.add("Remove Curse");
    }

    @GetMapping("/spells")
    public List<Spell> getAll(){
        return (List<Spell>) spellRepository.findAll();
    }

    @PostMapping("/spells")
    void addSpell(@RequestBody Spell spell){
        Boolean ok = true;
        for(String s: this.bannedSpells){
            if(Objects.equals(s, spell.getName()))
                ok = false;
        }
        if(ok)
            spellRepository.save(spell);
        else
            throw new ResourceNotFoundException("This spell is too OP");
    }

    @GetMapping("/spells/{id}")
    Spell getSpellById(@PathVariable Long id){
        return spellRepository.findById(id)
                .orElseThrow();
    }

    @PutMapping("/spells/{id}")
    Spell replaceSpell(@RequestBody Spell newspell, @PathVariable Long id){
        Boolean ok = true;
        for(String s: this.bannedSpells){
            if(Objects.equals(s, newspell.getName()))
                ok = false;
        }
        if(ok)
            return spellRepository.findById(id)
                    .map(spell -> {
                        spell.setName(newspell.getName());
                        spell.setRange(newspell.getRange());
                        spell.setDetails(newspell.getDetails());
                        return spellRepository.save(spell);
                    })
                    .orElseGet(() -> {
                       newspell.setId(id);
                       return spellRepository.save(newspell);
                    });
        System.out.println("This is banned for being too op");
        throw new ResourceNotFoundException("This spell is too OP");
    }

    @DeleteMapping("/spells/{id}")
    void deleteSpell(@PathVariable Long id){
        spellRepository.deleteById(id);
    }
}
