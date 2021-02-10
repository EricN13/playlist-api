package com.playlist.playlist.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PlaylistDto {
    private String name;
    private List<SongDto> songs;

    public PlaylistDto() {
    }

    public PlaylistDto(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }
}
