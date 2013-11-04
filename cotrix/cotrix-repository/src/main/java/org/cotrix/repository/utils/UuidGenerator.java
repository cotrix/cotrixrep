package org.cotrix.repository.utils;

import java.util.UUID;

import org.cotrix.domain.spi.IdGenerator;

public class UuidGenerator implements IdGenerator {

	@Override
	public String id() {
		return UUID.randomUUID().toString();
	}
}
