/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import java.io.Serializable;
import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvParserConfiguration implements Serializable {
	
	private static final long serialVersionUID = 2424486900885043596L;
	
	public enum NewLine {LF, CRLF, CR};
	
	protected String charset;
	protected char fieldSeparator;
	protected char comment;
	protected NewLine lineSeparator;
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
	 * @return the lineSeparator
	 */
	public NewLine getLineSeparator() {
		return lineSeparator;
	}
	/**
	 * @param lineSeparator the lineSeparator to set
	 */
	public void setLineSeparator(NewLine lineSeparator) {
		this.lineSeparator = lineSeparator;
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CSVParserConfiguration [charset=");
		builder.append(charset);
		builder.append(", fieldSeparator=");
		builder.append(fieldSeparator);
		builder.append(", comment=");
		builder.append(comment);
		builder.append(", lineSeparator=");
		builder.append(lineSeparator);
		builder.append(", quote=");
		builder.append(quote);
		builder.append(", hasHeader=");
		builder.append(hasHeader);
		builder.append("]");
		return builder.toString();
	}
}
