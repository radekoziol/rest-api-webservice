package com.app.repository;

import com.app.exceptions.NoSuchNoteException;
import com.app.model.note.Note;

import java.util.List;
import java.util.function.Predicate;

public interface NoteRepositoryCustom {

    List<Note> findNotesBy(Predicate<Note> predicate) throws NoSuchNoteException;

    void generateNotes(int number);

}
