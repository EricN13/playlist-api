package com.playlist.playlist.service;

import com.playlist.playlist.exception.AddSongException;
import com.playlist.playlist.exception.PlaylistCreationException;
import com.playlist.playlist.model.PlaylistDto;
import com.playlist.playlist.model.PlaylistEntity;
import com.playlist.playlist.model.SongDto;
import com.playlist.playlist.model.SongEntity;
import com.playlist.playlist.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void addSong(String playlistName, SongDto songDto) {
        PlaylistEntity playlistToUpdate = playlistRepository.findByName(playlistName);
        if(playlistToUpdate == null) {
            throw new AddSongException("Cannot Add To Playlist: Playlist Does Not Exist");
        } else if(isSongInPlaylist(playlistToUpdate.getSongs(), songDto.getName())) {
            throw new AddSongException("Cannot Add To Playlist: Song Already In Playlist");
        }
        playlistToUpdate.getSongs().add(convertToSongEntity(songDto));
        playlistRepository.save(playlistToUpdate);
    }

    public void removeSong(String playlistName, String songName) {
        PlaylistEntity playlistToUpdate = playlistRepository.findByName(playlistName);
        SongEntity songToRemove = null;
        for(SongEntity song : playlistToUpdate.getSongs()) {
            if(song.getName().equals(songName)) {
                songToRemove = song;
            }
        }

        if(songToRemove != null) {
            playlistToUpdate.getSongs().remove(songToRemove);
            playlistRepository.save(playlistToUpdate);
        }
    }

    public PlaylistDto getPlaylist(String playlistName) {
        return convertToPlaylistDto(playlistRepository.findByName(playlistName));
    }

    public PlaylistDto convertToPlaylistDto(PlaylistEntity playlistEntity) {
        PlaylistDto playlistDto = new PlaylistDto(playlistEntity.getName());
        for(SongEntity song : playlistEntity.getSongs()) {
            playlistDto.getSongs().add(convertToSongDto(song));
        }
        return playlistDto;
    }

    SongEntity convertToSongEntity(SongDto songDto) {
        return new SongEntity(songDto.getName());
    }

    SongDto convertToSongDto(SongEntity songEntity) {
        return new SongDto(songEntity.getName());
    }

    boolean isSongInPlaylist(List<SongEntity> existingSongs, String songName) {
        for(SongEntity song : existingSongs) {
            if(song.getName().equals(songName)) {
                return true;
            }
        }
        return false;
    }
}
