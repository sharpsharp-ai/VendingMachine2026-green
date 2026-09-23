package de.sharpsharp.vendingmachine;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Runs every feature file under src/test/resources/features with the JUnit 4 runner.
 * Read from the source folder, not the classpath: a deleted feature file must stop running at once,
 * and Maven never removes stale copies from target/test-classes.
 * The step definitions live in this package (the "glue"); the browser hooks in the
 * smoke subpackage only fire for scenarios tagged @browser.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "de.sharpsharp.vendingmachine",
        tags = "~@Ignore",
        plugin = {"pretty", "html:target/cucumber-report.html"},
        snippets = CucumberOptions.SnippetType.CAMELCASE)
public class RunCucumberTest {
}
