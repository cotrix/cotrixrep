package org.cotrix.web.server;

import static org.cotrix.action.GenericAction.*;
import static org.cotrix.web.share.shared.feature.ApplicationFeatures.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.cotrix.security.LoginService;
import org.cotrix.security.impl.DefaultNameAndPasswordCollector;
import org.cotrix.user.User;
import org.cotrix.web.client.MainService;
import org.cotrix.web.share.server.task.ActionMapper;
import org.cotrix.web.share.shared.feature.FeatureCarrier;
import org.cotrix.web.share.shared.feature.ResponseWrapper;
import org.jboss.weld.servlet.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class MainServiceImpl extends RemoteServiceServlet implements MainService {

	protected Logger logger = LoggerFactory.getLogger(MainServiceImpl.class);
	
	@Inject
	protected LoginService loginService;
	
	@Inject
	protected ActionMapper actionMapper;
	
	@Inject
	protected HttpServletRequest httpServletRequest;
	
	@Inject
	ActionMapper mapper;
	
	/** 
	 * {@inheritDoc}
	 */
	public void init() {
		
		mapper.map(IMPORT).to(IMPORT_CODELIST);
		mapper.map(PUBLISH).to(PUBLISH_CODELIST);
	}

	@Override
	public ResponseWrapper<String> login(String username, String password) {
		logger.trace("login username: {}",username);
		
		//HttpServletRequest httpServletRequest = getThreadLocalRequest();
		//System.out.println("SESSION ID: "+httpServletRequest.getSession().getId());
		SessionHolder.sessionCreated(httpServletRequest.getSession());
		
		httpServletRequest.setAttribute(DefaultNameAndPasswordCollector.nameParam, username);
		httpServletRequest.setAttribute(DefaultNameAndPasswordCollector.pwdParam, password);
		User user = loginService.login(httpServletRequest);	
		logger.trace("returned user: {}",user);
		
		ResponseWrapper<String> wrapper = new ResponseWrapper<String>(user.id());
		
		actionMapper.fillFeatures(wrapper, null, user.permissions());
		
		return wrapper;
	}

	@Override
	public FeatureCarrier.Void logout() {
		return FeatureCarrier.getVoid();
	}

}
