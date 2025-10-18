package com.labs.lg.food.ordering.system.payment.service.boot;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameters({
		@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, json:target/atdd-reports/cucumber.json, "
				+ "html:target/atdd-reports/cucumber-reports.html"),
		@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "com.labs.lg.food.ordering.system.payment.service")})
class AcceptanceTestCase {

	@Test
	void test() {
		final File feature = new File("src/test/resources/features");
		assertTrue(feature.exists());
	}
}
