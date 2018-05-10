package com.app.repository;

import com.app.model.Note;
import com.app.model.date.Date;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NoteRepositoryImpl implements NoteRepositoryCustom {

    @Autowired
    NoteRepository noteRepository;

    @Override
    public Note findByTitle(String title) {
        List<Note> notes = (List<Note>) noteRepository.findAll();
        return notes.stream()
                .filter(n -> n.getTitle().equals(title))
                .findFirst().orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Note> findByModificationDate(Predicate<Date> predicate) {
        List<Note> notes = (List<Note>) noteRepository.findAll();
        return notes.stream()
                .filter(n -> predicate.test(new Date(n.getLastModificationDate())))
                .collect(Collectors.toList());
    }
}
