package com.playlist.playlist.model;

import lombok.Data;

@Data
public class SongDto {
    private String name;

    public SongDto() {
    }

    public SongDto(String name) {
        this.name = name;
    }
}
