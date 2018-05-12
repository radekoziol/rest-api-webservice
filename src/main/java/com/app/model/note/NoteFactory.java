package com.app.model.note;

import com.app.model.date.Date;

import java.util.*;
import java.util.stream.IntStream;

public class NoteFactory {

    private static final Map<String,String> notes;
    static {
        notes = new HashMap<>();
        notes.put("Sucess", "\"Success is most often achieved by those who don't know that failure is inevitable.\" -- Coco Chanel");
        notes.put("Courage", "\"Courage is grace under pressure\" -- Ernest Hemingway");
        notes.put("Life", "\"Learn from yesterday, live for today, hope for tomorrow. The important thing is not to stop questioning\" --  Albert Einstein");
        notes.put("Soul", "\"Sometimes you can't see yourself clearly until you see yourself through the eyes of others.\" --  Ellen DeGeneres");
        notes.put("Work", "\"It does not matter how slowly you go, so long as you do not stop.\" -- Confucius");
    }

    public List<Note> createRandomNotes(int number){

        List<Note> notes = new LinkedList<>();

        IntStream.range(1, number).forEach(i -> notes.add(createRandomNote()));

        return notes;
    }

    private Note createRandomNote() {

        List<String> keysAsArray = new ArrayList<>(notes.keySet());
        Random r = new Random();

        Note note = new Note();
        note.setTitle(keysAsArray.get((r.nextInt(keysAsArray.size()))));
        note.setContent(notes.get(note.getTitle()));
        note.setInitialDate(getRandomDate());
        note.setLastModificationDate(
                new Date(note.getInitialDate())
                        .shiftDate(r.nextInt(20))
                        .toString()
        );

        return note;
    }

    /**
     * Assuming all are from 2017
     */
    private String getRandomDate(){

        Random r = new Random();

        String day = String.valueOf(r.nextInt(25) + 1);
        String month = String.valueOf(5);
        Date date = new Date("2017",month,day);

        return date.toString();
    }

}
