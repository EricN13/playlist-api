package com.playlist.playlist;

import com.playlist.playlist.exception.PlaylistCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({PlaylistCreationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void playlistAppExceptionHandler(){
    }
}