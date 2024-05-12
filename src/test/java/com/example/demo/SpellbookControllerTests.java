package com.example.demo;

import com.example.demo.Spell;
import com.example.demo.Spellbook;
import com.example.demo.SpellbookController;
import com.example.demo.SpellbookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpellbookControllerTest {

    @Mock
    private SpellbookRepository spellbookRepository;

    @InjectMocks
    private SpellbookController spellbookController;

    @Test
    void testGetNumberOfSpellsByBooks() {
        List<Spellbook> spellbooks = new ArrayList<>();
        Spellbook spellbook1 = new Spellbook("Spellbook1", new HashSet<>(Arrays.asList(
                new Spell("Spell1", 10, "Details1"),
                new Spell("Spell2", 20, "Details2")
        )));
        Spellbook spellbook2 = new Spellbook("Spellbook2", new HashSet<>(Arrays.asList(
                new Spell("Spell3", 15, "Details3"),
                new Spell("Spell4", 25, "Details4"),
                new Spell("Spell5", 30, "Details5")
        )));
        spellbooks.add(spellbook1);
        spellbooks.add(spellbook2);
        when(spellbookRepository.findAll()).thenReturn(spellbooks);
        HashMap<String, Integer> result = spellbookController.getNumberOfSpellsByBooks();
        assertEquals(2, result.size());
        assertEquals(2, result.get("Spellbook1"));
        assertEquals(3, result.get("Spellbook2"));
    }

    @Test
    void testCreateSpellbook() {
        List<Spellbook> spellbooks = new ArrayList<>();
        Spellbook spellbook = new Spellbook("Spellbook1", null);
        when(spellbookRepository.save(spellbook)).thenAnswer(invocation -> {
            spellbooks.add(spellbook);
            return spellbook;
        });

        int originalSize = spellbooks.size();
        spellbookController.createSpellbook(spellbook);
        verify(spellbookRepository).save(spellbook);
        assertEquals(originalSize + 1, spellbooks.size());
    }

    @Test
    void testUpdateSpellbook() {
        Long spellbookId = 1L;
        Spellbook existingSpellbook = new Spellbook("Spellbook1", null);
        existingSpellbook.setSpellbookId(spellbookId);
        Spellbook updatedSpellbook = new Spellbook("Updated Spellbook", null);
        when(spellbookRepository.findById(spellbookId)).thenReturn(java.util.Optional.of(existingSpellbook));
        when(spellbookRepository.save(any(Spellbook.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Spellbook result = spellbookController.updateSpellbook(updatedSpellbook, spellbookId);

        assertEquals("Updated Spellbook", result.getName());
        assertEquals(spellbookId, result.getSpellbookId());
    }

    @Test
    void testDeleteSpellbook() {
        Long spellbookId = 1L;
        spellbookController.deleteSpellbook(spellbookId);
        verify(spellbookRepository).deleteById(spellbookId);
    }
}