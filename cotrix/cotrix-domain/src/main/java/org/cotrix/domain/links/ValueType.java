package org.cotrix.domain.links;

import org.cotrix.domain.codelist.Code;

public interface ValueType {

	Object value(Code.State code);
}
