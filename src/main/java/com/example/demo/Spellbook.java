package com.example.demo;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Spellbook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long spellbookId;
    private String name;
    @OneToMany
    private Set<Spell> spells = new HashSet<>();

    public Spellbook(String name, Set<Spell> spells) {
        this.name = name;
        this.spells = spells;
    }

    public Spellbook() {

    }

    public void setSpellbookId(Long spellbookId) {
        this.spellbookId = spellbookId;
    }

    public Long getSpellbookId() {
        return spellbookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Spell> getSpells() {
        return spells;
    }

    public void setSpells(Set<Spell> spells) {
        this.spells = spells;
    }
}
