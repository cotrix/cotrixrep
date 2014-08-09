package org.cotrix.domain.links;

import java.util.List;

import org.cotrix.domain.codelist.Code;

public interface LinkValueType {

	List<Object> valueIn(String linkId,Code.Bean target, List<String> chain);
}
