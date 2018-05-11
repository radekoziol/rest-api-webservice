package com.app.api.note.controller;

import com.app.model.Note;
import com.app.model.NoteFactory;
import com.app.repository.NoteRepository;
import com.app.repository.NoteRepositoryImpl;
import com.app.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.app.model.date.Date;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(path = "/notes")
public class NoteController {

    private NoteService noteService;

    // This means to get the bean called noteRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    @Autowired
    private NoteRepository noteRepository;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    String createNewNote(@RequestParam String title
            , @RequestParam String content) {

        Note n = new Note();
        n.setTitle(title);
        n.setContent(content);
        n.setInitialDate(Date.getCurrentDate().toString());
        n.setLastModificationDate(Date.getCurrentDate().toString());

        noteService.addNote(n);
        return "Saved\n";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody
    String updateNote(@RequestParam String title,
                      @RequestParam String content) {

        Note note = noteRepository.findNotesBy(n -> n.getTitle().equals(title)).get(0);
        note.setContent(content);
        note.setLastModificationDate(Date.getCurrentDate().toString());
        noteRepository.save(note);

        return "Updated\n";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    String deleteNote(@RequestParam String title) {

        Note note = noteRepository.findNotesBy(n -> n.getTitle().equals(title)).get(0);
        noteRepository.delete(note);

        return "Deleted\n";
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Note getNote(@RequestParam String title) {
        return noteRepository.findNotesBy(n -> n.getTitle().equals(title)).get(0);
    }

    @RequestMapping(path = "/page", method = RequestMethod.GET)
    public @ResponseBody
    List<Note> getSortedNotes(@RequestParam String sortBy,
                              @RequestParam String sortHow) {

        List<Note> notes = (List<Note>) noteRepository.findAll();

        switch (sortBy) {
            case "modDate":
                if (sortHow.equals("asc"))
                    notes.sort(Comparator.comparing(Note::getLastModificationDate));
                else if (sortHow.equals("des"))
                    notes.sort(Comparator.comparing(Note::getLastModificationDate).reversed());
                break;
            case "initDate":
                if (sortHow.equals("asc"))
                    notes.sort(Comparator.comparing(Note::getLastModificationDate));
                else if (sortHow.equals("des"))
                    notes.sort(Comparator.comparing(Note::getLastModificationDate).reversed());
                break;
            case "title":
                if (sortHow.equals("asc"))
                    notes.sort(Comparator.comparing(Note::getTitle));
                else if (sortHow.equals("des"))
                    notes.sort(Comparator.comparing(Note::getTitle).reversed());
                break;
            case "contLen":
                if (sortHow.equals("asc"))
                    notes.sort(Comparator.comparingInt(a -> a.getContent().length()));
                else if (sortHow.equals("des")) {
                    notes.sort(Comparator.comparingInt(a -> a.getContent().length()));
                    Collections.reverse(notes);
                }

                break;
            default:
                // TODO
        }

        return notes;

    }


    @RequestMapping(path = "/modMorThan", method = RequestMethod.GET)
    public @ResponseBody
    List<Note> getNotes(@RequestParam int days) {
        return noteRepository
                .findNotesBy(d ->
                        Date.dayDifference
                                (Date.getCurrentDate(), new Date(d.getLastModificationDate()))
                                >= days
                );
    }

    @GetMapping(path = "/generate")
    public @ResponseBody
    String generateNotes(@RequestParam int number) {
        noteRepository.generateNotes(number);
        return String.valueOf(number) + " notes added\n";
    }


    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Note> getAllNotes() {
        return noteRepository.findAll();
    }


}