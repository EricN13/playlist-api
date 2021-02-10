package com.playlist.playlist.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class PlaylistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private List<SongEntity> songs;

    public PlaylistEntity() {
    }

    public PlaylistEntity(String name) {
        this.name = name;
        this.songs = new ArrayList();
    }
}
