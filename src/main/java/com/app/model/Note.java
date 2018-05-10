package com.app.model;


import com.app.model.date.Date;
import jdk.nashorn.internal.objects.annotations.Property;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.web.bind.annotation.Mapping;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Repeatable;


@Entity
public class Note {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    private String title;
    private String content;

    private String initialDate;
    private String lastModificationDate;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public String getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(String lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

}
