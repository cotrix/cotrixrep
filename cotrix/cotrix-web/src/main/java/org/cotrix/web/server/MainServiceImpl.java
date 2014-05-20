package org.cotrix.web.server;

import static org.cotrix.action.GuestAction.*;
import static org.cotrix.action.MainAction.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.web.common.shared.feature.ApplicationFeatures.*;
import static org.cotrix.web.shared.AuthenticationFeature.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.cotrix.action.Action;
import org.cotrix.action.Actions;
import org.cotrix.action.CodelistAction;
import org.cotrix.action.MainAction;
import org.cotrix.application.NewsService;
import org.cotrix.application.NewsService.NewsItem;
import org.cotrix.application.StatisticsService;
import org.cotrix.application.StatisticsService.Statistics;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.user.User;
import org.cotrix.engine.Engine;
import org.cotrix.engine.TaskOutcome;
import org.cotrix.security.InvalidCredentialsException;
import org.cotrix.security.InvalidUsernameException;
import org.cotrix.security.LoginRequest;
import org.cotrix.security.LoginService;
import org.cotrix.security.SignupService;
import org.cotrix.security.impl.DefaultNameAndPasswordCollector;
import org.cotrix.web.client.MainService;
import org.cotrix.web.common.server.task.ActionMapper;
import org.cotrix.web.common.server.util.ExceptionUtils;
import org.cotrix.web.common.server.util.Users;
import org.cotrix.web.common.shared.UIUser;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.common.shared.feature.ApplicationFeatures;
import org.cotrix.web.common.shared.feature.FeatureCarrier;
import org.cotrix.web.shared.LoginToken;
import org.cotrix.web.shared.UINews;
import org.cotrix.web.shared.UIStatistics;
import org.cotrix.web.shared.UrlToken;
import org.cotrix.web.shared.UsernamePasswordToken;
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
	protected StatisticsService statisticsService;

	@Inject
	protected NewsService newsService;

	@Inject
	protected SignupService signupService;

	@Inject
	ActionMapper mapper;

	@Inject
	Engine engine;

	@Current
	@Inject
	User currentUser;

	/** 
	 * {@inheritDoc}
	 */
	public void init() {

		mapper.map(SIGNUP).to(CAN_REGISTER);
		mapper.map(LOGIN).to(CAN_LOGIN);
		mapper.map(LOGOUT).to(CAN_LOGOUT);
		mapper.map(IMPORT).to(IMPORT_CODELIST);
		mapper.map(PUBLISH).to(PUBLISH_CODELIST);
		mapper.map(MainAction.ACCESS_ADMIN_AREA).to(ApplicationFeatures.ACCESS_ADMIN_AREA);
	}

	@Override
	public UIUser login(LoginToken token, List<String> openCodelists) throws ServiceException {
		logger.trace("login token: {} openCodelists: {}",token, openCodelists);

		try {
			return doLogin(LOGIN, token, openCodelists);
		} catch(Exception exception) {
			logger.error("failed login for token "+token, exception);

			InvalidCredentialsException unknownUserException = ExceptionUtils.unfoldException(exception, InvalidCredentialsException.class);
			if (unknownUserException!=null) {
				throw new org.cotrix.web.shared.UnknownUserException(exception.getMessage());
			} else {
				throw new ServiceException(exception.getMessage());
			}
		}
	}

	@Override
	public UIUser logout(List<String> openCodelists) {
		logger.trace("logout");

		return doLogin(LOGOUT, UsernamePasswordToken.GUEST, openCodelists);
	}

	protected UIUser doLogin(Action action, final LoginToken token, List<String> openCodelists)
	{
		logger.trace("doLogin action: {} token: {} openCodelists: {}", action, token, openCodelists);

		LoginRequest loginRequest = toLoginRequest(token);
		User user = loginService.login(loginRequest);	
		logger.trace("logged user: {}",user);

		UIUser uiUser = Users.toUiUser(user);

		Collection<Action> actions = Actions.filterForAction(action, user.permissions());

		actionMapper.fillFeatures(uiUser, actions);

		fillOpenCodelistsActions(openCodelists, user, uiUser);

		return uiUser;
	}

	protected LoginRequest toLoginRequest(LoginToken token) {
		LoginRequest loginRequest = new LoginRequest();
		if (token instanceof UsernamePasswordToken) {
			UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;
			loginRequest.setAttribute(DefaultNameAndPasswordCollector.nameParam, usernamePasswordToken.getUsername());
			loginRequest.setAttribute(DefaultNameAndPasswordCollector.pwdParam, usernamePasswordToken.getPassword());
			logger.trace("added name and psw");
		}

		if (token instanceof UrlToken) {
			UrlToken urlToken = (UrlToken)token;
			loginRequest.setAttribute("TOKEN", urlToken.getToken());
			logger.trace("added token");
		}
		return loginRequest;
	}

	protected void fillOpenCodelistsActions(List<String> openCodelists, User user, FeatureCarrier featureCarrier)
	{
		for (String openCodelist:openCodelists) fillCodelistActions(openCodelist, user, featureCarrier);
	}

	protected void fillCodelistActions(String codelistId, User user, FeatureCarrier featureCarrier)
	{
		TaskOutcome<Void> outcome = engine.perform(CodelistAction.VIEW.on(codelistId)).with(NOP);
		actionMapper.fillFeatures(featureCarrier, codelistId, outcome.nextActions());
	}
	
	@Override
	public UIStatistics getStatistics() throws ServiceException {
		
		try {
			Statistics statistics = statisticsService.statistics();
			UIStatistics uiStatistics = new UIStatistics();
			uiStatistics.setCodelists(statistics.totalCodelists());
			uiStatistics.setCodes(statistics.totalCodes());
			uiStatistics.setUsers(statistics.totalUsers());
			uiStatistics.setRepositories(statistics.totalRepositories());
			return uiStatistics;
		} catch(Exception e) {
			logger.error("Error occurred getting statistics", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public List<UINews> getNews() throws ServiceException {
		logger.trace("getNews");
		
		try {
			List<UINews> news = new ArrayList<UINews>();

			for (NewsItem newsItem:newsService.news()) {
				UINews uiNews = new UINews();
				uiNews.setTimestamp(newsItem.timestamp());
				uiNews.setText(newsItem.text());
				news.add(uiNews);
			}

			Collections.reverse(news);

			return news;
		} catch(Exception e) {
			logger.error("Error occurred getting news", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public UIUser registerUser(String username, String password, String email, List<String> openCodelists) throws ServiceException {
		logger.trace("registerUser username: {} email: {}", username, email);

		try {
			User user = user().name(username).fullName(username).email(email).build();
			signupService.signup(user, password);

			return doLogin(LOGIN, new UsernamePasswordToken(username, password), openCodelists);
		} catch(Exception exception) {
			logger.error("failed login for user "+username, exception);
			
			InvalidCredentialsException unknownUserException = ExceptionUtils.unfoldException(exception, InvalidCredentialsException.class);
			if (unknownUserException!=null) throw new org.cotrix.web.shared.UnknownUserException(exception.getMessage());

			InvalidUsernameException invalidUsernameException = ExceptionUtils.unfoldException(exception, InvalidUsernameException.class);
			if (invalidUsernameException!=null) throw new org.cotrix.web.shared.InvalidUsernameException(invalidUsernameException.getMessage());

			throw new ServiceException(exception.getMessage());
		}
	}

	@Override
	public UIUser getCurrentUser() throws ServiceException {
		logger.trace("getCurrentUser");
		UIUser uiUser = Users.toUiUser(currentUser);
		actionMapper.fillFeatures(uiUser, currentUser.permissions());
		return uiUser;
	}

}
