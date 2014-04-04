package org.cotrix.domain.links;

import org.cotrix.domain.codelist.Code;

public interface ValueType {

	Object valueIn(Code.State code);
}
