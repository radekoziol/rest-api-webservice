package com.app.service;

import com.app.model.Note;
import com.app.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class NoteService {

    private NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void addNote(Note note) {
        noteRepository.save(note);
    }

/*  // Assuming notes titles or content can repeat
    private void checkForDuplicates(Note note) {

        if(!noteRepository.findNotesBy(n -> n.getTitle().equals(note.getTitle())).isEmpty()) {
        }
    }
*/

}
