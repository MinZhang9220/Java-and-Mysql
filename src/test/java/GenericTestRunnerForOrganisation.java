import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/organisations",
        plugin = {"pretty", "html:target/site/cucumber -pretty", "json:target/cucumber.json"},
        glue = "steps/organisations",
        snippets = SnippetType.CAMELCASE
)
public class GenericTestRunnerForOrganisation {

}
