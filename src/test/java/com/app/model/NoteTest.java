package com.app.model;

import com.app.model.date.Date;
import com.app.model.note.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class NoteTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void postNotes() throws Exception {

        // Random example
        ResponseEntity<String> response = restTemplate.
                postForEntity("http://localhost:8080/notes?title=Ex&content=ex", "Saved", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        // Empty content
        response = restTemplate.
                postForEntity("http://localhost:8080/notes?title=Ex&content=", "Saved", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        // Null title
        response = restTemplate.
                postForEntity("http://localhost:8080/notes?content=ex", "Saved", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));

        // Wrong args
        response = restTemplate.
                postForEntity("http://localhost:8080/notes?tit1e=&content=", "Saved", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));

    }

    @Test
    public void updateNotes() {

        // Posting
        restTemplate.
                postForEntity("http://localhost:8080/notes?title=Ex1&content=ex1", "Saved", String.class);

        restTemplate.
                postForEntity("http://localhost:8080/notes?title=Ex2&content=blabla", "Saved", String.class);

        // Updating
        restTemplate.
                put("http://localhost:8080/notes?title=Ex1&content=blabla", "Updated", String.class);

        restTemplate.
                put("http://localhost:8080/notes?title=Ex2&content=blabla", "Updated", String.class);

        ResponseEntity<Note> response1 = restTemplate.getForEntity(
                "http://localhost:8080/notes?title=Ex1", Note.class);

        ResponseEntity<Note> response2 = restTemplate.getForEntity(
                "http://localhost:8080/notes?title=Ex2", Note.class);

        ObjectMapper mapper = new ObjectMapper();

        assertTrue(
                response1.getBody().getContent()
                        .equals(response2.getBody().getContent()));

    }

    @Test
    public void deleteNotes(){

        // Posting
        restTemplate.
                postForEntity("http://localhost:8080/notes?title=Ex1&content=ex1", "Saved", String.class);

        restTemplate.
                postForEntity("http://localhost:8080/notes?title=Ex1&content=blabla", "Saved", String.class);

        // Deleting
        restTemplate.delete("http://localhost:8080/notes?title=Ex1");

        ResponseEntity<Note> response2 = restTemplate.getForEntity(
                "http://localhost:8080/notes?title=Ex1", Note.class);

        assertTrue(response2.getBody().getContent().equals("blabla"));

    }

    @Test
    public void getSortedNotes(){

        // Posting 7 notes within one month
        restTemplate.
                postForLocation("http://localhost:8080/notes/generate?number=7", void.class);

        ResponseEntity<Note[]>  responseEntity = restTemplate.
                getForEntity("http://localhost:8080/notes/all",
                        Note[].class);

        List<Note> notes = Arrays.asList(responseEntity.getBody());

        // Sorting original dates
        notes = notes.stream()
                .sorted((n1,n2) -> Date.dayDifference(
                        new Date(n1.getInitialDate()),new Date(n2.getInitialDate())))
                .collect(Collectors.toList());

        // Sorting on server
        responseEntity = restTemplate.
                getForEntity("http://localhost:8080/notes/page?sortBy=initDate=&sortHow=asc",
                        Note[].class);

        List<Note> serverNotes = Arrays.asList(responseEntity.getBody());

        assertTrue(notes.equals(serverNotes));


    }



}