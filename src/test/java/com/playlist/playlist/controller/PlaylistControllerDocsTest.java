package com.playlist.playlist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playlist.playlist.model.PlaylistDto;
import com.playlist.playlist.model.PlaylistEntity;
import com.playlist.playlist.model.SongDto;
import com.playlist.playlist.model.SongEntity;
import com.playlist.playlist.repository.PlaylistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PlaylistControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void createPlaylist_successfullyCreatesPlaylist() throws Exception {
        mockMvc.perform(post("/playlist?playlistName=samplePlaylist"))
                .andExpect(status().isCreated())
                .andDo(document("createPlaylist", requestParameters(
                        parameterWithName("playlistName").description("name of playlist to save")
                )));

    }

    @Test
    public void createPlaylist_throwsExceptionIfNotGivenName() throws Exception {
        mockMvc.perform(post("/playlist?playlistName="))
                .andExpect(status().isBadRequest())
                .andDo(document("createNoName", requestParameters(
                        parameterWithName("playlistName").description("name of playlist to save")
                )));


    }

    @Test
    public void createPlaylist_throwsExceptionIfPlaylistAlreadyExists() throws Exception {
        playlistRepository.save(new PlaylistEntity("samplePlaylist"));

        mockMvc.perform(post("/playlist?playlistName=samplePlaylist"))
                .andExpect(status().isBadRequest())
                .andDo(document("createAlreadyExists", requestParameters(
                        parameterWithName("playlistName").description("name of playlist to save")
                )));

    }

    @Test
    public void addSong_addsSongToPlayList() throws Exception {
        mapper = new ObjectMapper();
        playlistRepository.save(new PlaylistEntity("samplePlaylist"));
        SongDto songDto = new SongDto("some song");
        String songString = mapper.writeValueAsString(songDto);

        mockMvc.perform(put("/playlist/{playlistName}/song", "samplePlaylist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(songString))
                .andExpect(status().isOk())
                .andDo(document("addSong", pathParameters(
                    parameterWithName("playlistName").description("name of playlist to add song")
                    ),
                requestFields(
                        fieldWithPath("name").description("name of song to save")
                    )
                ));
    }

    @Test
    public void addSong_throwsExceptionIfSongAlreadyInPlaylist() throws Exception {
        mapper = new ObjectMapper();
        PlaylistEntity entity = new PlaylistEntity("samplePlaylist");
        entity.getSongs().add(new SongEntity("some song"));
        playlistRepository.save(entity);
        SongDto songDto = new SongDto("some song");
        String songString = mapper.writeValueAsString(songDto);

        mockMvc.perform(put("/playlist/{playlistName}/song", "samplePlaylist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(songString))
                .andExpect(status().isBadRequest())
                .andDo(document("songAlreadyInPlaylist", pathParameters(
                        parameterWithName("playlistName").description("name of playlist to add song")
                        ),
                        requestFields(
                                fieldWithPath("name").description("name of song to save")
                        )
                ));
    }

    @Test
    public void addSong_throwsExceptionIfPlaylistDoesNotExist() throws Exception {
        mapper = new ObjectMapper();
        SongDto songDto = new SongDto("some song");
        String songString = mapper.writeValueAsString(songDto);

        mockMvc.perform(put("/playlist/{playlistName}/song", "samplePlaylist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(songString))
                .andExpect(status().isBadRequest())
                .andDo(document("addPlaylistDoesntExist", pathParameters(
                        parameterWithName("playlistName").description("name of playlist to add song")
                        ),
                        requestFields(
                                fieldWithPath("name").description("name of song to save")
                        )
                ));
    }

    @Test
    public void removeSong_removesSelectedSongFromPlaylist() throws Exception {
        PlaylistEntity playListWithSong = new PlaylistEntity("samplePlaylist");
        playListWithSong.getSongs().add(new SongEntity("other-song"));
        playlistRepository.save(playListWithSong);

        mockMvc.perform(put("/playlist/{playlistName}/song/{songName}" , "samplePlaylist", "other-song"))
                .andExpect(status().isNoContent())
                .andDo(document("removeSong", pathParameters(
                        parameterWithName("playlistName").description("name of playlist where song is to be removed"),
                        parameterWithName("songName").description("name of song to be removed")
                )));
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

        mockMvc.perform(get("/playlist/{playlistName}", "samplePlaylist"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected))
                .andDo(document("getPlaylist", pathParameters(
                        parameterWithName("playlistName").description("name of playlist where song is to be removed")),
                        responseFields(
                                fieldWithPath("name").description("name of playlist"),
                                fieldWithPath("songs").description("list of songs in playlist"),
                                fieldWithPath("songs.[].name").description("name of song")
                        )));
    }
}