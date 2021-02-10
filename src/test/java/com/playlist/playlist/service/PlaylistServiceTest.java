package com.playlist.playlist.service;

import com.playlist.playlist.exception.AddSongException;
import com.playlist.playlist.exception.PlaylistCreationException;
import com.playlist.playlist.model.PlaylistEntity;
import com.playlist.playlist.model.SongDto;
import com.playlist.playlist.model.SongEntity;
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
        when(playlistRepository.findByName("sample playlist")).thenReturn(null);
        service.createPlaylist("sample playlist");
        verify(playlistRepository, times(1)).save(playlist);
        verify(playlistRepository, times(1)).findByName("sample playlist");
        verifyNoMoreInteractions(playlistRepository);
    }

    @Test
    public void createPlaylist_throwsExceptionIfGivenNameExistsInDB() {
        PlaylistEntity playlist = new PlaylistEntity("sample playlist");
        when(playlistRepository.findByName("sample playlist")).thenReturn(playlist);
        assertThrows(PlaylistCreationException.class,
                () -> service.createPlaylist("sample playlist"),
                "Cannot Create: Playlist With That Name Already Exists");
        verify(playlistRepository, times(1)).findByName("sample playlist");
        verifyNoMoreInteractions(playlistRepository);
    }

    @Test
    public void createPlaylist_throwsExceptionIfNotProvidedPlaylistName() {
        assertThrows(PlaylistCreationException.class,
                () -> service.createPlaylist(null),
                "Cannot Create: Please Provide Name For Playlist");
        verifyNoInteractions(playlistRepository);
    }

    @Test
    public void createPlaylist_throwsExceptionIfProvidedEmptyString() {
        assertThrows(PlaylistCreationException.class,
                () -> service.createPlaylist(""),
                "Cannot Create: Please Provide Name For Playlist");
        verifyNoInteractions(playlistRepository);
    }

    @Test
    public void addSong_addsSongToPlaylist() {
        SongDto songDto = new SongDto("song to save");
        PlaylistEntity playlistEntity = new PlaylistEntity("samplePlaylist");
        PlaylistEntity playListWithSong = new PlaylistEntity("samplePlaylist");
        playListWithSong.getSongs().add(new SongEntity("song to save"));
        when(playlistRepository.findByName("samplePlaylist")).thenReturn(playlistEntity);
        when(playlistRepository.save(playListWithSong)).thenReturn(null);
        service.addSong("samplePlaylist", songDto);
        verify(playlistRepository, times(1)).findByName("samplePlaylist");
        verify(playlistRepository, times(1)).save(playListWithSong);
        verifyNoMoreInteractions(playlistRepository);
    }

    @Test
    public void addSong_throwsExceptionIfPlaylistDoesNotExist() {
        SongDto songDto = new SongDto("song to save");
        when(playlistRepository.findByName("nonExistantPlaylist")).thenReturn(null);
        assertThrows(AddSongException.class,
                ()-> service.addSong("nonExistantPlaylist", songDto),
                "Cannot Add To Playlist: Playlist Does Not Exist");
        verify(playlistRepository, times(1)).findByName("nonExistantPlaylist");
        verifyNoMoreInteractions(playlistRepository);
    }

    @Test
    public void addSong_throwsExceptionIfSongAlreadyExistsInPlaylist() {
        SongDto songDto = new SongDto("song to save");
        PlaylistEntity playListWithSong = new PlaylistEntity("samplePlaylist");
        playListWithSong.getSongs().add(new SongEntity("song to save"));
        when(playlistRepository.findByName("samplePlaylist")).thenReturn(playListWithSong);
        assertThrows(AddSongException.class,
                ()-> service.addSong("samplePlaylist", songDto),
                "Cannot Add To Playlist: Song Already In Playlist");
        verify(playlistRepository, times(1)).findByName("samplePlaylist");
        verifyNoMoreInteractions(playlistRepository);
    }
}