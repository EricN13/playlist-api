package com.playlist.playlist.repository;

import com.playlist.playlist.model.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
    public PlaylistEntity findByName(String name);
}
