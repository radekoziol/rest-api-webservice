package com.app.repository;

import com.app.model.Note;
import com.app.model.date.Date;

import java.util.List;
import java.util.function.Predicate;

public interface NoteRepositoryCustom {

    Note findByTitle(String title);

    List<Note> findByModificationDate(Predicate<Date> predicate);

}
