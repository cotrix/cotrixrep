package org.cotrix.domain.links;

import org.cotrix.domain.codelist.Code;

public interface LinkType {

	Object value(Code.State code);
}
