package com.playlist.playlist.controller;

import com.playlist.playlist.model.SongDto;
import com.playlist.playlist.service.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    private PlaylistService service;

    public PlaylistController(PlaylistService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPlaylist(@RequestParam String playlistName) {
        service.createPlaylist(playlistName);
    }

    @PutMapping("/{playlistName}")
    public void addSong(@PathVariable String playlistName, @RequestBody SongDto songDto){
        service.addSong(playlistName, songDto);
    }

    @GetMapping("/temp")
    public String DELETEME() {
        return "I AM WORKING!!";
    }
}
