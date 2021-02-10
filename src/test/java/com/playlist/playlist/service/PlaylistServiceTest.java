package com.playlist.playlist.service;

import com.playlist.playlist.exception.PlaylistCreationException;
import com.playlist.playlist.model.PlaylistEntity;
import com.playlist.playlist.repository.PlaylistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    @Mock
    PlaylistRepository playlistRepository;

    @InjectMocks
    PlaylistService service;

    @Test
    public void createPlaylist_callsSaveOnPlaylistRepo() {
        PlaylistEntity playlist = new PlaylistEntity("sample playlist");
        when(playlistRepository.save(playlist)).thenReturn(null);
        service.createPlaylist("sample playlist");
        verify(playlistRepository, times(1)).save(playlist);
    }

    @Test
    public void createPlaylist_throwsExceptionIfGivenNameExistsInDB() {
        PlaylistEntity playlist = new PlaylistEntity("sample playlist");
        when(playlistRepository.findByName("sample playlist")).thenReturn(playlist);
        assertThrows(PlaylistCreationException.class,
                () -> service.createPlaylist("sample playlist"),
                "Cannot Create: Playlist With That Name Already Exists");
        verify(playlistRepository, times(1)).findByName("sample playlist");
    }

    @Test
    public void createPlaylist_throwsExceptionIfNotProvidedPlaylistName() {
        assertThrows(PlaylistCreationException.class,
                () -> service.createPlaylist(null),
                "Cannot Create: Please Provide Name For Playlist");
    }

    @Test
    public void createPlaylist_throwsExceptionIfProvidedEmptyString() {
        assertThrows(PlaylistCreationException.class,
                () -> service.createPlaylist(""),
                "Cannot Create: Please Provide Name For Playlist");
    }
}