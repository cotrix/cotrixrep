/**
 * 
 */
package org.cotrix.application.impl.mail;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.cotrix.application.MailService;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.UserQueries;
import org.cotrix.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleCollection;
import freemarker.template.Template;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractMailer {

	@Inject
	protected MailService mailService;

	@Inject
	protected UserRepository userRepository;

	protected static Configuration templateEngineConfiguration;

	private static final Logger log = LoggerFactory.getLogger(AbstractMailer.class);
	
	
	protected synchronized static Configuration getEngineConfiguration() {
		if (templateEngineConfiguration == null) {
			templateEngineConfiguration = new Configuration();

			templateEngineConfiguration.setClassForTemplateLoading(AbstractMailer.class, "/templates");

			templateEngineConfiguration.setIncompatibleImprovements(new Version(2, 3, 20));
			templateEngineConfiguration.setDefaultEncoding("UTF-8");
			templateEngineConfiguration.setLocale(Locale.US);
			templateEngineConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

			templateEngineConfiguration.setObjectWrapper(new ObjectWrapperExtension());

		}
		return templateEngineConfiguration;
	}

	public String getText(String templateName, String parName, Object parValue) {
		return getText(templateName, Collections.singletonMap(parName, parValue));
	}

	public String getText(String templateName, Map<String, Object> parameters) {
		try {
			Template template = getTemplate(templateName);
			StringWriter writer = new StringWriter();
			template.process(parameters, writer);
			return writer.toString();
		} catch(Exception e) {
			throw new RuntimeException("Failed text creation for template "+templateName, e);
		}

	}

	protected Template getTemplate(String templateName) throws IOException {
		Configuration configuration = getEngineConfiguration();
		Template template = configuration.getTemplate(templateName);
		return template;
	}

	public Iterable<User> codelistTeam(String codelistId) {
		return userRepository.get(UserQueries.teamFor(codelistId));
	}

	public Iterable<User> usersWithRole(Role role) {
		return userRepository.get(UserQueries.usersWith(role));
	}

	public void sendMail(Collection<String> recipients, String subject, String messageBody) {
		
		if (recipients.isEmpty())
			log.warn("no recepients for email: {}. Send aborted.",subject);
		else 
			mailService.sendMessage(recipients, subject, messageBody);
	}

	static class ObjectWrapperExtension extends DefaultObjectWrapper {

		protected TemplateModel handleUnknownType(Object obj) throws TemplateModelException {
			return new ReflectionHashModel(obj);
		}

		class ReflectionHashModel implements TemplateHashModelEx {

			protected Object object;
			protected Map<String, Method> methodsCache;

			public ReflectionHashModel(Object object) {
				this.object = object;
				methodsCache = new HashMap<String, Method>();
				for (Method method:object.getClass().getMethods()) if (method.getParameterTypes().length == 0) methodsCache.put(method.getName(), method);
			}

			@Override
			public TemplateModel get(String key) throws TemplateModelException {
				try {
					Method method = methodsCache.get(key);
					Object result = method.invoke(object);
					return ObjectWrapper.DEFAULT_WRAPPER.wrap(result);
				} catch (Exception e) {
					throw new TemplateModelException("Method invocation error for name "+key, e);
				}
			}

			@Override
			public boolean isEmpty() throws TemplateModelException {
				return methodsCache.isEmpty();
			}

			@Override
			public int size() throws TemplateModelException {
				return methodsCache.size();
			}

			@Override
			public TemplateCollectionModel keys() throws TemplateModelException {
				return new SimpleCollection(methodsCache.keySet());
			}

			@Override
			public TemplateCollectionModel values()	throws TemplateModelException {
				return new SimpleCollection(Collections.emptyList());
			}
		}
	}
}
