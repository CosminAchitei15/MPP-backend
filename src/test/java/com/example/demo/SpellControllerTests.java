package com.example.demo;

import com.example.demo.ResourceNotFoundException;
import com.example.demo.Spell;
import com.example.demo.SpellController;
import com.example.demo.SpellRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpellControllerTest {

    @Mock
    private SpellRepository spellRepository;

    @InjectMocks
    private SpellController spellController;

    @Test
    void testGetAll() {
        List<Spell> spells = new ArrayList<>();
        spells.add(new Spell("Fireball", 20, "Deals fire damage"));
        spells.add(new Spell("Ice Lance", 15, "Freezes the target"));
        when(spellRepository.findAll()).thenReturn(spells);

        List<Spell> result = spellController.getAll();

        assertEquals(2, result.size());
        assertEquals("Fireball", result.get(0).getName());
        assertEquals("Ice Lance", result.get(1).getName());
    }

    @Test
    void testAddSpell_ValidSpell() {
        Spell spell = new Spell("Magic Missile", 25, "Hits the target");
        when(spellRepository.save(spell)).thenReturn(spell);

        assertDoesNotThrow(() -> spellController.addSpell(spell));
        verify(spellRepository, times(1)).save(spell);
    }

    @Test
    void testAddSpell_BannedSpell() {
        Spell bannedSpell = new Spell("Remove Curse", 30, "Removes curses");
        assertThrows(ResourceNotFoundException.class, () -> spellController.addSpell(bannedSpell));
        verify(spellRepository, never()).save(bannedSpell);
    }


    @Test
    void testGetSpellById_ExistingSpell() {
        long spellId = 1L;
        Spell spell = new Spell("Lightning Bolt", 30, "Deals lightning damage");
        when(spellRepository.findById(spellId)).thenReturn(Optional.of(spell));

        Spell result = spellController.getSpellById(spellId);

        assertEquals("Lightning Bolt", result.getName());
        assertEquals(30, result.getRange());
    }

    @Test
    void testGetSpellById_NonExistingSpell() {
        long nonExistingId = 100L;
        when(spellRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> spellController.getSpellById(nonExistingId));
    }

    @Test
    void testDeleteSpell_ExistingSpell() {
        long spellId = 1L;
        assertDoesNotThrow(() -> spellController.deleteSpell(spellId));
        verify(spellRepository, times(1)).deleteById(spellId);
    }

    @Test
    void testDeleteSpell_NonExistingSpell() {
        long nonExistingId = 100L;

        doThrow(ResourceNotFoundException.class).when(spellRepository).deleteById(nonExistingId);

        assertThrows(ResourceNotFoundException.class, () -> spellController.deleteSpell(nonExistingId));
        verify(spellRepository, times(1)).deleteById(nonExistingId);
    }
}
