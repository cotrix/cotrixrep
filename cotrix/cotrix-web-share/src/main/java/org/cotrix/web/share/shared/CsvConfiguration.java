/**
 * 
 */
package org.cotrix.web.share.shared;

import java.io.Serializable;
import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvConfiguration implements Serializable {
	
	private static final long serialVersionUID = 2424486900885043596L;
	
	protected String charset;
	protected char fieldSeparator;
	protected char comment;
	protected char quote;
	protected boolean hasHeader;
	
	protected List<String> availablesCharset;

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}
	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
	/**
	 * @return the fieldSeparator
	 */
	public char getFieldSeparator() {
		return fieldSeparator;
	}
	/**
	 * @param fieldSeparator the fieldSeparator to set
	 */
	public void setFieldSeparator(char fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}
	/**
	 * @return the comment
	 */
	public char getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(char comment) {
		this.comment = comment;
	}

	/**
	 * @return the quote
	 */
	public char getQuote() {
		return quote;
	}
	/**
	 * @param quote the quote to set
	 */
	public void setQuote(char quote) {
		this.quote = quote;
	}
	/**
	 * @return the hasHeader
	 */
	public boolean isHasHeader() {
		return hasHeader;
	}
	/**
	 * @param hasHeader the hasHeader to set
	 */
	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	/**
	 * @return the availablesCharset
	 */
	public List<String> getAvailablesCharset() {
		return availablesCharset;
	}
	/**
	 * @param availablesCharset the availablesCharset to set
	 */
	public void setAvailablesCharset(List<String> availablesCharset) {
		this.availablesCharset = availablesCharset;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((charset == null) ? 0 : charset.hashCode());
		result = prime * result + comment;
		result = prime * result + fieldSeparator;
		result = prime * result + (hasHeader ? 1231 : 1237);
		result = prime * result + quote;
		return result;
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CsvConfiguration other = (CsvConfiguration) obj;
		if (charset == null) {
			if (other.charset != null)
				return false;
		} else if (!charset.equals(other.charset))
			return false;
		if (comment != other.comment)
			return false;
		if (fieldSeparator != other.fieldSeparator)
			return false;
		if (hasHeader != other.hasHeader)
			return false;
		if (quote != other.quote)
			return false;
		return true;
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CSVParserConfiguration [charset=");
		builder.append(charset);
		builder.append(", fieldSeparator=");
		builder.append(fieldSeparator);
		builder.append(", comment=");
		builder.append(comment);
		builder.append(", quote=");
		builder.append(quote);
		builder.append(", hasHeader=");
		builder.append(hasHeader);
		builder.append("]");
		return builder.toString();
	}
}
