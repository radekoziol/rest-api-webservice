package com.app.repository;

import com.app.model.note.Note;
import org.springframework.data.repository.CrudRepository;

public interface NoteRepository extends CrudRepository<Note, Long>, NoteRepositoryCustom {

}
