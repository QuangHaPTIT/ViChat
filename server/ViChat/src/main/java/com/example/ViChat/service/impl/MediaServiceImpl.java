package com.example.ViChat.service.impl;

import com.example.ViChat.dto.request.MediaRequest;
import com.example.ViChat.entity.Media;
import com.example.ViChat.entity.Message;
import com.example.ViChat.mapper.MediaMapper;
import com.example.ViChat.repository.MediaRepository;
import com.example.ViChat.service.MediaService;
import com.example.ViChat.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;
    @Override
    @Transactional
    public Media save(Message message, MediaRequest mediaRequest) {
        Media newMedia = mediaMapper.toMedia(mediaRequest);
        newMedia.setMessage(message);
        newMedia = mediaRepository.save(newMedia);
        return newMedia;
    }

    @Override
    public List<Media> getAllMediaByMessageId(int messageId) {
        return null;
    }

    @Override
    @Transactional
    public void deleteMediaById(int mediaId) {

    }
}
