package com.github.paganini2008.springplayer.common.email;

/**
 * 
 * JavaEmailService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface JavaEmailService {

	void sendTextEmail(TextEmail textEmail) throws Exception;

	void sendRichTextEmail(RichTextEmail textEmail) throws Exception;

}