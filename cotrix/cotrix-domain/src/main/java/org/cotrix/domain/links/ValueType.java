package org.cotrix.domain.links;

import java.util.List;

import org.cotrix.domain.codelist.Code;

public interface ValueType {

	List<Object> valueIn(String linkId,Code.State target, List<String> chain);
}
