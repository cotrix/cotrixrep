package org.cotrix.web.server;

import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.cotrix.security.LoginService;
import org.cotrix.security.impl.DefaultNameAndPasswordCollector;
import org.cotrix.user.PredefinedUsers;
import org.cotrix.user.User;
import org.cotrix.web.client.MainService;
import org.cotrix.web.share.server.task.ActionMapper;
import org.cotrix.web.share.shared.feature.FeatureCarrier;
import org.cotrix.web.share.shared.feature.ResponseWrapper;
import org.cotrix.web.share.shared.feature.UIFeature;
import org.cotrix.web.shared.AuthenticationFeature;
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
		
		Set<UIFeature> features = actionMapper.mapActions(user.permissions());
		
		if (user.equals(PredefinedUsers.guest)) features.add(AuthenticationFeature.CAN_LOGIN);
		else features.add(AuthenticationFeature.CAN_LOGOUT);
		
		wrapper.setApplicationFeatures(features);
		
		return wrapper;
	}

	@Override
	public FeatureCarrier.Void logout() {
		return FeatureCarrier.getVoid();
	}

}
