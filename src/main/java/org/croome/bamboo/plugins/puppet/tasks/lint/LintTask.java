package org.croome.bamboo.plugins.puppet.tasks.lint;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.build.test.TestCollationService;
import com.atlassian.bamboo.build.test.TestCollectionResult;
import com.atlassian.bamboo.build.test.TestCollectionResultBuilder;
import com.atlassian.bamboo.build.test.TestReportProvider;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.utils.process.ExternalProcess;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import org.croome.bamboo.plugins.puppet.tasks.TestParserLogInterceptor;
import org.croome.bamboo.plugins.puppet.tasks.lint.LintTestParser;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class LintTask implements TaskType {
	
    // ------------------------------------------------------------------------------------------------------- Constants
    public static final String PUPPET_LINT_CAPABILITY_PREFIX = CapabilityDefaultsHelper.CAPABILITY_BUILDER_PREFIX + ".puppet-lint";
    public static final String PUPPET_LINT_EXE_NAME = "puppet-lint"; 
    public static final String PUPPET_LINT_LABEL = "Puppet Lint"; 
    
    // ------------------------------------------------------------------------------------------------- Type Properties
    // ---------------------------------------------------------------------------------------------------- Dependencies
    private final ProcessService processService;
    private final EnvironmentVariableAccessor environmentVariableAccessor;
    private final CapabilityContext capabilityContext;
    private final TestCollationService testCollationService;
    
    // ---------------------------------------------------------------------------------------------------- Constructors	

    public LintTask(ProcessService processService, EnvironmentVariableAccessor environmentVariableAccessor, CapabilityContext capabilityContext, TestCollationService testCollationService)
    {
        this.processService = processService;
        this.environmentVariableAccessor = environmentVariableAccessor;
        this.capabilityContext = capabilityContext;
        this.testCollationService = testCollationService;
    }

    // ----------------------------------------------------------------------------------------------- Interface Methods
    @NotNull
    @java.lang.Override
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException
    {
    	final TestParserLogInterceptor lintLogInterceptor = new TestParserLogInterceptor(new LintTestParser());
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        final TaskResultBuilder taskResultBuilder = TaskResultBuilder.create(taskContext);
        
        try {
            final ConfigurationMap configurationMap = taskContext.getConfigurationMap();
            final Map<String, String> environment = environmentVariableAccessor.splitEnvironmentAssignments(configurationMap.get(LintTaskConfigurator.ENVIRONMENT), false);
            final String command = Preconditions.checkNotNull(capabilityContext.getCapabilityValue(LintTask.PUPPET_LINT_CAPABILITY_PREFIX + "." + LintTask.PUPPET_LINT_LABEL), LintTask.PUPPET_LINT_EXE_NAME + " path is not defined");
            final List<String> arguments = Lists.newArrayList(command);
            
            arguments.add("--with-filename");
            arguments.add("--with-context");
            
            final Boolean failOnWarnings = Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.FAIL_ON_WARNINGS));
            if (failOnWarnings)
            	arguments.add("--fail-on-warnings");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_RIGHT_TO_LEFT_RELATIONSHIP_CHECK)))
                arguments.add("--no-right_to_left_relationship-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_AUTOLOADER_LAYOUT_CHECK)))
            	arguments.add("--no-autoloader_layout-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_NAMES_CONTAINING_DASH_CHECK)))
            	arguments.add("--no-names_containing_dash-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_CLASS_INHERITS_FROM_PARAMS_CLASS_CHECK)))
            	arguments.add("--no-class_inherits_from_params_class-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_CLASS_PARAMETER_DEFAULTS_CHECK)))
            	arguments.add("--no-class_parameter_defaults-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_PARAMETER_ORDER_CHECK)))
            	arguments.add("--no-parameter_order-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_INHERITS_ACROSS_NAMESPACES_CHECK)))
            	arguments.add("--no-inherits_across_namespaces-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_NESTED_CLASSES_OR_DEFINES_CHECK)))
            	arguments.add("--no-nested_classes_or_defines-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_VARIABLE_SCOPE_CHECK)))
            	arguments.add("--no-variable_scope-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_SLASH_COMMENTS_CHECK)))
            	arguments.add("--no-slash_comments-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_STAR_COMMENTS_CHECK)))
            	arguments.add("--no-star_comments-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_SELECTOR_INSIDE_RESOURCE_CHECK)))
            	arguments.add("--no-selector_inside_resource-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_CASE_WITHOUT_DEFAULT_CHECK)))
            	arguments.add("--no-case_without_default-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_DOCUMENTATION_CHECK)))
            	arguments.add("--no-documentation-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_DOUBLE_QUOTED_STRINGS_CHECK)))
            	arguments.add("--no-double_quoted_strings-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_ONLY_VARIABLE_STRING_CHECK)))
            	arguments.add("--no-only_variable_string-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_VARIABLES_NOT_ENCLOSED_CHECK)))
            	arguments.add("--no-variables_not_enclosed-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_SINGLE_QUOTE_STRING_WITH_VARIABLES_CHECK)))
            	arguments.add("--no-single_quote_string_with_variables-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_QUOTED_BOOLEANS_CHECK)))
            	arguments.add("--no-quoted_booleans-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_VARIABLE_CONTAINS_DASH_CHECK)))
            	arguments.add("--no-variable_contains_dash-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_HARD_TABS_CHECK)))
            	arguments.add("--no-hard_tabs-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_TRAILING_WHITESPACE_CHECK)))
            	arguments.add("--no-trailing_whitespace-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_80CHARS_CHECK)))
            	arguments.add("--no-80chars-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_2SP_SOFT_TABS_CHECK)))
            	arguments.add("--no-2sp_soft_tabs-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_ARROW_ALIGNMENT_CHECK)))
            	arguments.add("--no-arrow_alignment-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_UNQUOTED_RESOURCE_TITLE_CHECK)))
            	arguments.add("--no-unquoted_resource_title-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_ENSURE_FIRST_PARAM_CHECK)))
            	arguments.add("--no-ensure_first_param-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_DUPLICATE_PARAMS_CHECK)))
            	arguments.add("--no-duplicate_params-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_UNQUOTED_FILE_MODE_CHECK)))
            	arguments.add("--no-unquoted_file_mode-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_FILE_MODE_CHECK)))
            	arguments.add("--no-file_mode-check");
            if (Boolean.parseBoolean(configurationMap.get(LintTaskConfigurator.NO_ENSURE_NOT_SYMLINK_TARGET_CHECK)))
            	arguments.add("--no-ensure_not_symlink_target-check");            

            // Use the relative path to avoid long pathnames in test results
            arguments.add(".");
            
            buildLogger.addBuildLogEntry("Executing " + command);
        	buildLogger.getInterceptorStack().add(lintLogInterceptor);

            final ExternalProcess externalProcess = processService.executeProcess(taskContext, new ExternalProcessBuilder()
	            .command(arguments)
	            .env(environment)
	            .workingDirectory(taskContext.getWorkingDirectory()));
            
            testCollationService.collateTestResults(taskContext, new TestReportProvider() {
                @NotNull
                @Override
                public TestCollectionResult getTestCollectionResult()
                {
                    return new TestCollectionResultBuilder()
                            .addFailedTestResults(lintLogInterceptor.getFailingTestResults(failOnWarnings))
                            .addSuccessfulTestResults(lintLogInterceptor.getSuccessfulTestResults())
                            .build();
                }
            });

            taskResultBuilder.checkTestFailures();
            return taskResultBuilder.build();
        }
        catch (Exception e)
        {
            throw new TaskException("Failed to execute task", e);
        }
        finally
        {
            buildLogger.getInterceptorStack().remove(lintLogInterceptor);
        }
        
    }	

}
