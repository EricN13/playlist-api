package com.playlist.playlist.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class PlaylistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<SongEntity> songs;

    public PlaylistEntity() {
    }

    public PlaylistEntity(String name) {
        this.name = name;
        this.songs = new ArrayList<SongEntity>();
    }
}
