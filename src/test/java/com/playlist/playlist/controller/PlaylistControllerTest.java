package com.playlist.playlist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playlist.playlist.model.PlaylistDto;
import com.playlist.playlist.model.PlaylistEntity;
import com.playlist.playlist.model.SongDto;
import com.playlist.playlist.model.SongEntity;
import com.playlist.playlist.repository.PlaylistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PlaylistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void createPlaylist_successfullyCreatesPlaylist() throws Exception {
        mockMvc.perform(post("/playlist?playlistName=samplePlaylist"))
                .andExpect(status().isCreated());

        PlaylistEntity result = playlistRepository.findByName("samplePlaylist");
        assertEquals("samplePlaylist", result.getName());
        assertEquals(0, result.getSongs().size());
    }

    @Test
    public void createPlaylist_throwsExceptionIfNotGivenName() throws Exception {
        mockMvc.perform(post("/playlist?playlistName="))
                .andExpect(status().isBadRequest());

        List<PlaylistEntity> result = playlistRepository.findAll();
        assertEquals(0, result.size());
    }

    @Test
    public void createPlaylist_throwsExceptionIfPlaylistAlreadyExists() throws Exception {
        playlistRepository.save(new PlaylistEntity("samplePlaylist"));

        mockMvc.perform(post("/playlist?playlistName=samplePlaylist"))
                .andExpect(status().isBadRequest());

        List<PlaylistEntity> result = playlistRepository.findAll();
        assertEquals(1, result.size());
    }

    @Test
    public void addSong_addsSongToPlayList() throws Exception {
        mapper = new ObjectMapper();
        playlistRepository.save(new PlaylistEntity("samplePlaylist"));
        SongDto songDto = new SongDto("some song");
        String songString = mapper.writeValueAsString(songDto);

        mockMvc.perform(put("/playlist/samplePlaylist/song")
                .contentType(MediaType.APPLICATION_JSON)
                .content(songString))
                .andExpect(status().isOk());

        PlaylistEntity result = playlistRepository.findByName("samplePlaylist");
        assertEquals("some song", result.getSongs().get(0).getName());
    }

    @Test
    public void addSong_throwsExceptionIfSongAlreadyInPlaylist() throws Exception {
        mapper = new ObjectMapper();
        PlaylistEntity entity = new PlaylistEntity("samplePlaylist");
        entity.getSongs().add(new SongEntity("some song"));
        playlistRepository.save(entity);
        SongDto songDto = new SongDto("some song");
        String songString = mapper.writeValueAsString(songDto);

        mockMvc.perform(put("/playlist/samplePlaylist/song")
                .contentType(MediaType.APPLICATION_JSON)
                .content(songString))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addSong_throwsExceptionIfPlaylistDoesNotExist() throws Exception {
        mapper = new ObjectMapper();
        SongDto songDto = new SongDto("some song");
        String songString = mapper.writeValueAsString(songDto);

        mockMvc.perform(put("/playlist/samplePlaylist/song")
                .contentType(MediaType.APPLICATION_JSON)
                .content(songString))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void removeSong_removesSelectedSongFromPlaylist() throws Exception {
        PlaylistEntity playListWithSong = new PlaylistEntity("samplePlaylist");
        playListWithSong.getSongs().add(new SongEntity("other-song"));
        playlistRepository.save(playListWithSong);

        mockMvc.perform(put("/playlist/samplePlaylist/song/other-song"))
                .andExpect(status().isNoContent());

        PlaylistEntity result = playlistRepository.findByName("samplePlaylist");

        assertEquals(0, result.getSongs().size());
    }

    @Test
    public void getPlaylist() throws Exception {
        PlaylistEntity playListWithSong = new PlaylistEntity("samplePlaylist");
        playListWithSong.getSongs().add(new SongEntity("other song"));
        playlistRepository.save(playListWithSong);

        PlaylistDto playlistDto = new PlaylistDto("samplePlaylist");
        playlistDto.getSongs().add(new SongDto("other song"));

        ObjectMapper mapper = new ObjectMapper();
        String expected = mapper.writeValueAsString(playlistDto);

        mockMvc.perform(get("/playlist/samplePlaylist"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }
}