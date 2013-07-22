/**
 * 
 */
package org.cotrix.web.importwizard.client.step.metadata;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DefaultFormat implements Format {

	private static final String DATE_BOX_FORMAT_ERROR = "dateBoxFormatError";
	
    private final DateTimeFormat dateTimeFormat;

    /**
     * Creates a new default format instance.
     */
    public DefaultFormat() {
      dateTimeFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM);
    }

    /**
     * Creates a new default format instance.
     *
     * @param dateTimeFormat the {@link DateTimeFormat} to use with this
     *          {@link Format}.
     */
    public DefaultFormat(DateTimeFormat dateTimeFormat) {
      this.dateTimeFormat = dateTimeFormat;
    }

    public String format(DateBox box, Date date) {
      if (date == null) {
        return "";
      } else {
        return dateTimeFormat.format(date);
      }
    }

    /**
     * Gets the date time format.
     *
     * @return the date time format
     */
    public DateTimeFormat getDateTimeFormat() {
      return dateTimeFormat;
    }

    @SuppressWarnings("deprecation")
    public Date parse(DateBox dateBox, String dateText, boolean reportError) {
      Date date = null;
      try {
        if (dateText.length() > 0) {
          date = dateTimeFormat.parse(dateText);
        }
      } catch (IllegalArgumentException exception) {
        try {
          date = new Date(dateText);
        } catch (IllegalArgumentException e) {
          if (reportError) {
            dateBox.addStyleName(DATE_BOX_FORMAT_ERROR);
          }
          return null;
        }
      }
      return date;
    }

    public void reset(DateBox dateBox, boolean abandon) {
      dateBox.removeStyleName(DATE_BOX_FORMAT_ERROR);
    }
}
