package io.vertigo.struts2;

import io.vertigo.core.node.config.DefinitionProviderConfig;
import io.vertigo.core.node.config.Features;
import io.vertigo.dynamo.plugins.environment.DynamoDefinitionProvider;
import io.vertigo.struts2.boot.initializer.TestStruts2MasterDataDefinitionProvider;
import io.vertigo.struts2.dao.movies.MovieDAO;
import io.vertigo.struts2.services.movies.MovieServices;
import io.vertigo.struts2.services.movies.MovieServicesImpl;
import io.vertigo.struts2.services.users.UserServices;
import io.vertigo.struts2.services.users.UserServicesImpl;

public class TestStrus2Features extends Features<TestStrus2Features> {

	public TestStrus2Features() {
		super("test-vertigo-struts2");
	}

	@Override
	protected void buildFeatures() {
		getModuleConfigBuilder()
				.addDefinitionProvider(DefinitionProviderConfig.builder(DynamoDefinitionProvider.class)
						.addDefinitionResource("classes", "io.vertigo.struts2.domain.DtDefinitions")
						.addDefinitionResource("kpr", "/META-INF/io/vertigo/struts2/execution.kpr")
						.build())
				.addDefinitionProvider(TestStruts2MasterDataDefinitionProvider.class)
				.addComponent(MovieDAO.class)
				.addComponent(MovieServices.class, MovieServicesImpl.class)
				.addComponent(UserServices.class, UserServicesImpl.class)
				.build();
	}

}