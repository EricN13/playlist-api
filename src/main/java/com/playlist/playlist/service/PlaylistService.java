package com.playlist.playlist.service;

import com.playlist.playlist.exception.PlaylistCreationException;
import com.playlist.playlist.model.PlaylistEntity;
import com.playlist.playlist.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    private PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public void createPlaylist(String playlistName) {
        if(playlistName == null || playlistName.isEmpty()) {
            throw new PlaylistCreationException("Cannot Create: Please Provide Name For Playlist");
        }

        PlaylistEntity playlistToSave = playlistRepository.findByName(playlistName);

        if(playlistToSave != null) {
            throw new PlaylistCreationException("Cannot Create: Playlist With That Name Already Exists");
        }

        playlistRepository.save(new PlaylistEntity(playlistName));
    }
}
