package com.app.repository;

import com.app.model.note.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NoteRepository extends CrudRepository<Note, Long>, NoteRepositoryCustom {

    Optional<Note> findById(long id);
}
