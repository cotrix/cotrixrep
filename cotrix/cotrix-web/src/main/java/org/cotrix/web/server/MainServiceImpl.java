package org.cotrix.web.server;

import static org.cotrix.action.MainAction.*;
import static org.cotrix.web.share.shared.feature.ApplicationFeatures.*;
import static org.cotrix.web.shared.AuthenticationFeature.*;

import java.net.URL;
import java.util.Collection;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.cotrix.action.Action;
import org.cotrix.action.Actions;
import org.cotrix.engine.Engine;
import org.cotrix.engine.TaskOutcome;
import org.cotrix.security.LoginService;
import org.cotrix.security.impl.DefaultNameAndPasswordCollector;
import org.cotrix.user.User;
import org.cotrix.web.client.MainService;
import org.cotrix.web.share.server.task.ActionMapper;
import org.cotrix.web.share.shared.feature.ResponseWrapper;
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
	
	@Inject
	Engine engine;
	
	/** 
	 * {@inheritDoc}
	 */
	public void init() {
		
		mapper.map(LOGIN).to(CAN_LOGIN);
		mapper.map(LOGOUT).to(CAN_LOGOUT);
		mapper.map(IMPORT).to(IMPORT_CODELIST);
		mapper.map(PUBLISH).to(PUBLISH_CODELIST);
		
		URL url = MainServiceImpl.class.getResource("/test.css");
		System.out.println("URL RULR URURURURURURURURURURURURU "+url);
	}

	@Override
	public ResponseWrapper<String> login(final String username, final String password) {
		logger.trace("login username: {}",username);
		
		//SessionHolder.sessionCreated(httpServletRequest.getSession());
		
		return doLogin(LOGIN, username, password);
	}

	@Override
	public ResponseWrapper<String> logout() {
		logger.trace("logout");
		
		return doLogin(LOGOUT, null, null);
	}
	
	protected ResponseWrapper<String> doLogin(Action action, final String username, final String password)
	{
		logger.trace("doLogin action: {} username: {}", action, username);
		
		TaskOutcome<User> outcome = engine.perform(action).with(new Callable<User>() {

			@Override
			public User call() throws Exception {
				httpServletRequest.setAttribute(DefaultNameAndPasswordCollector.nameParam, username);
				httpServletRequest.setAttribute(DefaultNameAndPasswordCollector.pwdParam, password);
				User user = loginService.login(httpServletRequest);	
				logger.trace("returned user: {}",user);
				
				return user;
			}
		});
		
		User user = outcome.output();
		
		ResponseWrapper<String> wrapper = new ResponseWrapper<String>(user.id());
		
		Collection<Action> actions = Actions.filterForAction(action, user.permissions());
		
		actionMapper.fillFeatures(wrapper, actions);
		
		return wrapper;
	}

}
