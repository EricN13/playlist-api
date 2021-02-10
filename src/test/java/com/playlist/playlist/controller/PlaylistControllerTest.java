package com.playlist.playlist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playlist.playlist.model.PlaylistEntity;
import com.playlist.playlist.repository.PlaylistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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


}