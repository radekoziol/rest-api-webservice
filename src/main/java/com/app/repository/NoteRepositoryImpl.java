package com.app.repository;

import com.app.exceptions.NoSuchNoteException;
import com.app.model.note.Note;
import com.app.model.note.NoteFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NoteRepositoryImpl implements NoteRepositoryCustom {

    @Autowired
    NoteRepository noteRepository;

    @Override
    public List<Note> findNotesBy(Predicate<Note> predicate) throws NoSuchNoteException {

        List<Note> notes = (List<Note>) noteRepository.findAll();

        notes = notes.stream()
                .filter(predicate)
                .collect(Collectors.toList());

        if (notes.isEmpty())
            throw new NoSuchNoteException();

        return notes;
    }

    @Override
    public void generateNotes(int number) {

        NoteFactory noteFactory = new NoteFactory();
        noteRepository.saveAll(noteFactory.createRandomNotes(number));

    }
}
