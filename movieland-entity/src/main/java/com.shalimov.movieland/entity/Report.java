package com.shalimov.movieland.entity;

import javax.persistence.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Entity
@Table(name = "movie")
public class Report {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "body")
    private byte[] body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(body);
    }
}
