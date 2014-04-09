/**
 * 
 */
package org.cotrix.web.manage.client.codelist.linktype;



import com.google.gwt.animation.client.Animation;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that consists of a header and a content panel that discloses the
 * content when a user clicks on the header.
 * 
 * <h3>CSS Style Rules</h3> 
 * <dl class="css"> 
 * <dt>.gwt-DisclosurePanel 
 * <dd>the panel's primary style 
 * <dt>.gwt-DisclosurePanel-open 
 * <dd> dependent style set when panel is open 
 * <dt>.gwt-DisclosurePanel-closed 
 * <dd> dependent style set when panel is closed
 * </dl>
 * <p>
 * <img class='gallery' src='doc-files/DisclosurePanel.png'/>
 * </p>
 * 
 * <p>
 * The header and content sections can be easily selected using css with a child
 * selector:<br/>
 * .gwt-DisclosurePanel-open .header { ... }
 * </p>
 * <h3>Use in UiBinder Templates</h3>
 * <p>
 * DisclosurePanel elements in  
 * {@link com.google.gwt.uibinder.client.UiBinder UiBinder} templates can 
 * have one widget child and one of two types of header elements. A 
 * &lt;g:header> element can hold text (not html), or a &lt;g:customHeader> element
 * can hold a widget. (Note that the tags of the header elements are not
 * capitalized. This is meant to signal that the header is not a runtime object, 
 * and so cannot have a <code>ui:field</code> attribute.) 
 * <p>
 * For example:<pre>
 * &lt;g:DisclosurePanel>
 *   &lt;g:header>Text header&lt;/g:header>
 *   &lt;g:Label>Widget body&lt;/g:Label>
 * &lt;/g:DisclosurePanel>
 *
 * &lt;g:DisclosurePanel>
 *   &lt;g:customHeader>
 *     &lt;g:Label>Widget header&lt;/g:Label>
 *   &lt;/g:customHeader>
 *   &lt;g:Label>Widget body&lt;/g:Label>
 * &lt;/g:DisclosurePanel>
 * </pre>
 */

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkDisclosurePanel extends Composite implements HasAnimation, HasOpenHandlers<LinkDisclosurePanel>, HasCloseHandlers<LinkDisclosurePanel> {

	/**
	 * An {@link Animation} used to open the content.
	 */
	private static class ContentAnimation extends Animation {
		/**
		 * Whether the item is being opened or closed.
		 */
		private boolean opening;

		/**
		 * The {@link DisclosurePanel} being affected.
		 */
		private LinkDisclosurePanel curPanel;

		/**
		 * Open or close the content.
		 * 
		 * @param panel the panel to open or close
		 * @param animate true to animate, false to open instantly
		 */
		public void setOpen(LinkDisclosurePanel panel, boolean animate) {
			// Immediately complete previous open
			cancel();

			// Open the new item
			if (animate) {
				curPanel = panel;
				opening = panel.isOpen;
				run(ANIMATION_DURATION);
			} else {
				panel.contentWrapper.setVisible(panel.isOpen);
				if (panel.isOpen) {
					// Special treatment on the visible case to ensure LazyPanel works
					panel.getContent().setVisible(true);
				}
			}
		}

		@Override
		protected void onComplete() {
			if (!opening) {
				curPanel.contentWrapper.setVisible(false);
			}
			DOM.setStyleAttribute(curPanel.contentWrapper.getElement(), "height",
					"auto");
			curPanel = null;
		}

		@Override
		protected void onStart() {
			super.onStart();
			if (opening) {
				curPanel.contentWrapper.setVisible(true);
				// Special treatment on the visible case to ensure LazyPanel works
				curPanel.getContent().setVisible(true);
			}
		}

		@Override
		protected void onUpdate(double progress) {
			int scrollHeight = DOM.getElementPropertyInt(
					curPanel.contentWrapper.getElement(), "scrollHeight");
			int height = (int) (progress * scrollHeight);
			if (!opening) {
				height = scrollHeight - height;
			}
			height = Math.max(height, 1);
			DOM.setStyleAttribute(curPanel.contentWrapper.getElement(), "height",
					height + "px");
			DOM.setStyleAttribute(curPanel.contentWrapper.getElement(), "width",
					"auto");
		}
	}

	/**
	 * The duration of the animation.
	 */
	private static final int ANIMATION_DURATION = 350;

	private static final String STYLENAME_SUFFIX_OPEN = "open";

	private static final String STYLENAME_SUFFIX_CLOSED = "closed";

	private static final String STYLENAME_CONTENT = "content";

	/**
	 * The {@link Animation} used to open and close the content.
	 */
	private static ContentAnimation contentAnimation;

	/**
	 * top level widget. The first child will be a reference to {@link #header}.
	 * The second child will be a reference to {@link #contentWrapper}.
	 */
	private final VerticalPanel mainPanel = new VerticalPanel();

	/**
	 * The wrapper around the content widget.
	 */
	private final SimplePanel contentWrapper = new SimplePanel();

	/**
	 * holds the header widget.
	 */
	private final Widget header;

	private boolean isAnimationEnabled = false;

	private boolean isOpen = false;

	/**
	 * Creates an empty DisclosurePanel that is initially closed.
	 */
	public LinkDisclosurePanel(Widget header) {
		this.header = header;
		if (header instanceof HasClickHandlers) {
			HasClickHandlers hasClickHandlers = (HasClickHandlers) header;
			hasClickHandlers.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					toggle();
				}
			});
		}
		
		initWidget(mainPanel);
		mainPanel.add(header);
		mainPanel.add(contentWrapper);
		DOM.setStyleAttribute(contentWrapper.getElement(), "padding", "0px");
		DOM.setStyleAttribute(contentWrapper.getElement(), "overflow", "hidden");
		//setStyleName(STYLENAME_DEFAULT);
		setContentDisplay(false);
	}


	public void add(Widget w) {
		if (this.getContent() == null) {
			setContent(w);
		} else {
			throw new IllegalStateException(
					"A DisclosurePanel can only contain two Widgets.");
		}
	}

	/**
	 * Overloaded version for IsWidget.
	 * 
	 * @see #add(Widget)
	 */
	public void add(IsWidget w) {
		this.add(asWidgetOrNull(w));
	}

	public void clear() {
		setContent(null);
	}

	/**
	 * Gets the widget that was previously set in {@link #setContent(Widget)}.
	 * 
	 * @return the panel's current content widget
	 */
	public Widget getContent() {
		return contentWrapper.getWidget();
	}

	/**
	 * Gets the widget that is currently being used as a header.
	 * 
	 * @return the widget currently being used as a header
	 */
	public Widget getHeader() {
		return header;
	}


	public boolean isAnimationEnabled() {
		return isAnimationEnabled;
	}

	/**
	 * Determines whether the panel is open.
	 * 
	 * @return <code>true</code> if panel is in open state
	 */
	public boolean isOpen() {
		return isOpen;
	}


	public boolean remove(Widget w) {
		if (w == getContent()) {
			setContent(null);
			return true;
		}
		return false;
	}

	/**
	 * Overloaded version for IsWidget.
	 * 
	 * @see #remove(Widget)
	 */
	public boolean remove(IsWidget w) {
		return this.remove(asWidgetOrNull(w));
	}

	public void setAnimationEnabled(boolean enable) {
		isAnimationEnabled = enable;
	}

	/**
	 * Sets the content widget which can be opened and closed by this panel. If
	 * there is a preexisting content widget, it will be detached.
	 * 
	 * @param content the widget to be used as the content panel
	 */
	public void setContent(Widget content) {
		final Widget currentContent = getContent();

		// Remove existing content widget.
		if (currentContent != null) {
			contentWrapper.setWidget(null);
			currentContent.removeStyleName(STYLENAME_CONTENT);
		}

		// Add new content widget if != null.
		if (content != null) {
			contentWrapper.setWidget(content);
			content.addStyleName(STYLENAME_CONTENT);
			setContentDisplay(false);
		}
	}

	/**
	 * Changes the visible state of this <code>DisclosurePanel</code>.
	 * 
	 * @param isOpen <code>true</code> to open the panel, <code>false</code> to
	 *          close
	 */
	public void setOpen(boolean isOpen) {
		if (this.isOpen != isOpen) {
			this.isOpen = isOpen;
			setContentDisplay(true);
			fireEvent();
		}
	}
	
	public void toggle() {
		setOpen(!isOpen);
	}
	
	  private void fireEvent() {
		    if (isOpen) {
		      OpenEvent.fire(this, this);
		    } else {
		      CloseEvent.fire(this, this);
		    }
		  }

	/**
	 * <b>Affected Elements:</b>
	 * <ul>
	 * <li>-header = the clickable header.</li>
	 * </ul>
	 * 
	 * @see UIObject#onEnsureDebugId(String)
	 */
	@Override
	protected void onEnsureDebugId(String baseID) {
		super.onEnsureDebugId(baseID);
		header.ensureDebugId(baseID + "-header");
	}

	private void setContentDisplay(boolean animate) {
		if (isOpen) {
			removeStyleDependentName(STYLENAME_SUFFIX_CLOSED);
			addStyleDependentName(STYLENAME_SUFFIX_OPEN);
		} else {
			removeStyleDependentName(STYLENAME_SUFFIX_OPEN);
			addStyleDependentName(STYLENAME_SUFFIX_CLOSED);
		}

		if (getContent() != null) {
			if (contentAnimation == null) {
				contentAnimation = new ContentAnimation();
			}
			contentAnimation.setOpen(this, animate && isAnimationEnabled);
		}
	}


	@Override
	public HandlerRegistration addCloseHandler(CloseHandler<LinkDisclosurePanel> handler) {
		return addHandler(handler, CloseEvent.getType());
	}


	@Override
	public HandlerRegistration addOpenHandler(OpenHandler<LinkDisclosurePanel> handler) {
		return addHandler(handler, OpenEvent.getType());
	}
}

