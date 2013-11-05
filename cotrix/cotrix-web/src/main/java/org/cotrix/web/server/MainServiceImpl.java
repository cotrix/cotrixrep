package org.cotrix.web.server;

import static org.cotrix.action.MainAction.*;
import static org.cotrix.web.share.shared.feature.ApplicationFeatures.*;
import static org.cotrix.web.shared.AuthenticationFeature.*;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.cotrix.action.Action;
import org.cotrix.action.Actions;
import org.cotrix.action.CodelistAction;
import org.cotrix.engine.Engine;
import org.cotrix.engine.TaskOutcome;
import org.cotrix.security.LoginService;
import org.cotrix.security.impl.DefaultNameAndPasswordCollector;
import org.cotrix.user.User;
import org.cotrix.web.client.MainService;
import org.cotrix.web.share.server.task.ActionMapper;
import org.cotrix.web.share.shared.feature.FeatureCarrier;
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
	
	protected static final Callable<Void> NOP = new Callable<Void>() {

		@Override
		public Void call() throws Exception {
			return null;
		}
	};
	
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
	public ResponseWrapper<String> login(final String username, final String password, List<String> openCodelists) {
		logger.trace("login username: {}",username);
		return doLogin(LOGIN, username, password, openCodelists);
	}

	@Override
	public ResponseWrapper<String> logout(List<String> openCodelists) {
		logger.trace("logout");
		
		return doLogin(LOGOUT, null, null, openCodelists);
	}
	
	protected ResponseWrapper<String> doLogin(Action action, final String username, final String password, List<String> openCodelists)
	{
		logger.trace("doLogin action: {} username: {} openCodelists: {}", action, username, openCodelists);
		
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
		
		ResponseWrapper<String> wrapper = new ResponseWrapper<String>(user.fullName());
		
		Collection<Action> actions = Actions.filterForAction(action, user.permissions());
		
		actionMapper.fillFeatures(wrapper, actions);
		
		fillOpenCodelistsActions(openCodelists, user, wrapper);
		
		return wrapper;
	}
	
	protected void fillOpenCodelistsActions(List<String> openCodelists, User user, FeatureCarrier featureCarrier)
	{
		for (String openCodelist:openCodelists) fillCodelistActions(openCodelist, user, featureCarrier);
	}
	
	protected void fillCodelistActions(String codelistId, User user, FeatureCarrier featureCarrier)
	{
		engine.perform(CodelistAction.VIEW.on(codelistId)).with(NOP);
		Collection<Action> actions = Actions.filterForAction(CodelistAction.VIEW, user.permissions());
		actionMapper.fillFeatures(featureCarrier, codelistId, actions);
	}

}
