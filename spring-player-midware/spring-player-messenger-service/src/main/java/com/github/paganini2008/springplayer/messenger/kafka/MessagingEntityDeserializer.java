package com.github.paganini2008.springplayer.messenger.kafka;

import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.github.paganini2008.springplayer.common.messenger.model.MessageDTO;

/**
 * 
 * MessagingEntityDeserializer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class MessagingEntityDeserializer extends JsonDeserializer<MessageDTO> {
}
