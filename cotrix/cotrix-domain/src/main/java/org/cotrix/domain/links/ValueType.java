package org.cotrix.domain.links;

import java.util.Collection;
import java.util.List;

import org.cotrix.domain.codelist.Code;

public interface ValueType {

	Collection<Object> valueIn(String linkId,Code.State target, List<String> chain);
}
