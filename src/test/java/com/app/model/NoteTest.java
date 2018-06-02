package com.app.model;

import com.app.exceptions.NoSuchNoteException;
import com.app.model.note.Note;
import com.app.repository.NoteRepository;
import com.app.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class NoteTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NoteRepository noteRepository;

    private ResponseEntity<String> response;

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @Before
    public void createNotes(){

        // Random example
        response = restTemplate.
                postForEntity("http://localhost:8080/note?title=Ex1&content=ex", "Saved", String.class);

        // Empty content
        response = restTemplate.
                postForEntity("http://localhost:8080/note?title=Ex2&content=", "Saved", String.class);

    }


    @Test
    public void postNotes() throws Exception {

        // Null title
        response = restTemplate.
                postForEntity("http://localhost:8080/note?content=ex", "Saved", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));

        // Wrong args
        response = restTemplate.
                postForEntity("http://localhost:8080/note?tit1e=&content=", "Saved", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));

    }

    @Test
    public void updateNotes() {

        // Updating
        restTemplate.
                put("http://localhost:8080/note/1?content=updated!", "Updated", String.class);

        ResponseEntity<Note> response = restTemplate.getForEntity(
                "http://localhost:8080/note/1", Note.class);

        assertTrue(
                response.getBody().getContent()
                        .equals("updated!"));
    }

    @Test
    public void deleteNotes() {

        // Deleting
        restTemplate.delete("http://localhost:8080/note/2");

        try{
            restTemplate.getForEntity(
                    "http://localhost:8080/note/2", Note.class);
        }catch (NoSuchNoteException ex){

        }

    }

    @Test
    public void getSortedNotes() {

        noteRepository.deleteAll();

        // Posting 3 notes within one month
        Note note1 = new Note();
        note1.setTitle("ex1");
        note1.setInitialDate("2017-07-01");
        note1.setLastModificationDate("2017-07-04");

        Note note2 = new Note();
        note2.setTitle("ex2");
        note2.setInitialDate("2017-06-01");
        note2.setLastModificationDate("2017-07-01");

        Note note3 = new Note();
        note3.setTitle("ex3");
        note3.setInitialDate("2017-09-01");
        note3.setLastModificationDate("2017-10-01");


        // Creating sorted list
        List<String> sortedNotes = new ArrayList<>();
        sortedNotes.add(note2.getInitialDate());
        sortedNotes.add(note1.getInitialDate());
        sortedNotes.add(note3.getInitialDate());


        // Adding them to server
        NoteService noteService = new NoteService(noteRepository);
        noteService.addNote(note1);
        noteService.addNote(note2);
        noteService.addNote(note3);


        // Sorting on server
        ResponseEntity<Note[]> responseEntity = restTemplate.
                getForEntity("http://localhost:8080/notes/page?sortBy=initDate&sortHow=asc",
                        Note[].class);

        List<String> serverNotes = Arrays.stream(responseEntity.getBody())
                .map(Note::getInitialDate)
                .collect(Collectors.toList());

        System.out.println(serverNotes);
        System.out.println(sortedNotes);



        assertTrue(sortedNotes.equals(serverNotes));


    }


}