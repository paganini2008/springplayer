package com.github.paganini2008.springplayer.gateway.monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Label
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Label  {

	private String name;
	private String value;

	public String toString() {
		return name + "=" + value;
	}

}
