package com.github.paganini2008.springplayer.messenger.kafka;

import org.springframework.kafka.support.serializer.JsonSerializer;

import com.github.paganini2008.springplayer.common.messenger.model.MessageDTO;

/**
 * 
 * MessagingEntitySerializer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class MessagingEntitySerializer extends JsonSerializer<MessageDTO> {
}
