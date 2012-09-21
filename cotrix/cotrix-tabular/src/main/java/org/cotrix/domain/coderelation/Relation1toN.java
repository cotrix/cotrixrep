package org.cotrix.domain.coderelation;

import java.util.List;

import org.cotrix.domain.code.Code;

public class Relation1toN extends CodeRelation {

	protected Code fromCode;
	protected List<Code> toCollection;

	public Code getFromCode() {
		return fromCode;
	}

	public void setFromCode(Code fromCode) {
		this.fromCode = fromCode;
	}

	public List<Code> getToCollection() {
		return toCollection;
	}

	public void setToCollection(List<Code> toCollection) {
		this.toCollection = toCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromCode == null) ? 0 : fromCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relation1toN other = (Relation1toN) obj;
		if (fromCode == null) {
			if (other.fromCode != null)
				return false;
		} else if (!fromCode.equals(other.fromCode))
			return false;
		return true;
	}

}
