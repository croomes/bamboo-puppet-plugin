package org.croome.bamboo.plugins.puppet.tasks.lint;

import java.util.Map;
import java.util.Set;

import com.atlassian.bamboo.build.Job;
import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskConfigConstants;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.task.BuildTaskRequirementSupport;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.v2.build.agent.capability.Requirement;
import com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport;
import com.google.common.collect.Sets;
import com.opensymphony.xwork.TextProvider;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LintTaskConfigurator extends AbstractTaskConfigurator
{
    // ------------------------------------------------------------------------------------------------------- Constants
//    public static final String LABEL = "label";
	public static final String RUNTIME = "runtime";
	public static final String ENVIRONMENT = "environmentVariables";
    public static final String FAIL_ON_WARNINGS = "failOnWarnings";
    public static final String NO_RIGHT_TO_LEFT_RELATIONSHIP_CHECK = "noRtlRelationshipCheck";
    public static final String NO_AUTOLOADER_LAYOUT_CHECK = "noAutoloaderLayoutCheck";
    public static final String NO_NAMES_CONTAINING_DASH_CHECK = "noNamesContainingDashCheck";
    public static final String NO_CLASS_INHERITS_FROM_PARAMS_CLASS_CHECK = "noClassInheritsFromParamsClassCheck";
    public static final String NO_CLASS_PARAMETER_DEFAULTS_CHECK = "noClassParameterDefaultsCheck";
    public static final String NO_PARAMETER_ORDER_CHECK = "noParameterOrderCheck";
    public static final String NO_INHERITS_ACROSS_NAMESPACES_CHECK = "noInheritsAcrossNamespacesCheck";
    public static final String NO_NESTED_CLASSES_OR_DEFINES_CHECK = "noNestedClassesOrDefinesCheck";
    public static final String NO_VARIABLE_SCOPE_CHECK = "noVariableScopeCheck";
    public static final String NO_SLASH_COMMENTS_CHECK = "noSlashCommentsCheck";
    public static final String NO_STAR_COMMENTS_CHECK = "noStarCommentsCheck";
    public static final String NO_SELECTOR_INSIDE_RESOURCE_CHECK = "noSelectorInsideResourceCheck";
    public static final String NO_CASE_WITHOUT_DEFAULT_CHECK = "noCaseWithoutDefaultCheck";
    public static final String NO_DOCUMENTATION_CHECK = "noDocumentationCheck";
    public static final String NO_DOUBLE_QUOTED_STRINGS_CHECK = "noDoubleQuotedStringsCheck";
    public static final String NO_ONLY_VARIABLE_STRING_CHECK = "noOnlyVariableStringCheck";
    public static final String NO_VARIABLES_NOT_ENCLOSED_CHECK = "noVariablesNotEnclosedCheck";
    public static final String NO_SINGLE_QUOTE_STRING_WITH_VARIABLES_CHECK = "lint.noSingleQuoteStringWithVariablesCheck";
    public static final String NO_QUOTED_BOOLEANS_CHECK = "noQuotedBooleansCheck";
    public static final String NO_VARIABLE_CONTAINS_DASH_CHECK = "noVariableContainsDashCheck";
    public static final String NO_HARD_TABS_CHECK = "noHardTabsCheck";
    public static final String NO_TRAILING_WHITESPACE_CHECK = "noTrailingWhitespaceCheck";
    public static final String NO_80CHARS_CHECK = "no80charsCheck";
    public static final String NO_2SP_SOFT_TABS_CHECK = "no2spSoftTabsCheck";
    public static final String NO_ARROW_ALIGNMENT_CHECK = "noArrowAlignmentCheck";
    public static final String NO_UNQUOTED_RESOURCE_TITLE_CHECK = "noUnquotedResourceTitleCheck";
    public static final String NO_ENSURE_FIRST_PARAM_CHECK = "noEnsureFirstParamCheck";
    public static final String NO_DUPLICATE_PARAMS_CHECK = "noDuplicateParamsCheck";
    public static final String NO_UNQUOTED_FILE_MODE_CHECK = "noUnquotedFileModeCheck";
    public static final String NO_FILE_MODE_CHECK = "noFileModeCheck";
    public static final String NO_ENSURE_NOT_SYMLINK_TARGET_CHECK = "noEnsureNotSymlinkTargetCheck";
    
    public static final String CTX_UI_CONFIG_BEAN = "uiConfigBean";
	
    protected static final Set<String> FIELDS_TO_COPY = Sets.newHashSet();
    

    static {
        FIELDS_TO_COPY.add(TaskConfigConstants.CFG_WORKING_SUB_DIRECTORY);
        FIELDS_TO_COPY.add(RUNTIME);
        FIELDS_TO_COPY.add(ENVIRONMENT);
        FIELDS_TO_COPY.add(FAIL_ON_WARNINGS);
        FIELDS_TO_COPY.add(NO_RIGHT_TO_LEFT_RELATIONSHIP_CHECK);
        FIELDS_TO_COPY.add(NO_AUTOLOADER_LAYOUT_CHECK);
        FIELDS_TO_COPY.add(NO_NAMES_CONTAINING_DASH_CHECK);
        FIELDS_TO_COPY.add(NO_CLASS_INHERITS_FROM_PARAMS_CLASS_CHECK);
        FIELDS_TO_COPY.add(NO_CLASS_PARAMETER_DEFAULTS_CHECK);
        FIELDS_TO_COPY.add(NO_PARAMETER_ORDER_CHECK);
        FIELDS_TO_COPY.add(NO_INHERITS_ACROSS_NAMESPACES_CHECK);
        FIELDS_TO_COPY.add(NO_NESTED_CLASSES_OR_DEFINES_CHECK);
        FIELDS_TO_COPY.add(NO_VARIABLE_SCOPE_CHECK);
        FIELDS_TO_COPY.add(NO_SLASH_COMMENTS_CHECK);
        FIELDS_TO_COPY.add(NO_STAR_COMMENTS_CHECK);
        FIELDS_TO_COPY.add(NO_SELECTOR_INSIDE_RESOURCE_CHECK);
        FIELDS_TO_COPY.add(NO_CASE_WITHOUT_DEFAULT_CHECK);
        FIELDS_TO_COPY.add(NO_DOCUMENTATION_CHECK);
        FIELDS_TO_COPY.add(NO_DOUBLE_QUOTED_STRINGS_CHECK);
        FIELDS_TO_COPY.add(NO_ONLY_VARIABLE_STRING_CHECK);
        FIELDS_TO_COPY.add(NO_VARIABLES_NOT_ENCLOSED_CHECK);
        FIELDS_TO_COPY.add(NO_SINGLE_QUOTE_STRING_WITH_VARIABLES_CHECK);
        FIELDS_TO_COPY.add(NO_QUOTED_BOOLEANS_CHECK);
        FIELDS_TO_COPY.add(NO_VARIABLE_CONTAINS_DASH_CHECK);
        FIELDS_TO_COPY.add(NO_HARD_TABS_CHECK);
        FIELDS_TO_COPY.add(NO_TRAILING_WHITESPACE_CHECK);
        FIELDS_TO_COPY.add(NO_80CHARS_CHECK);
        FIELDS_TO_COPY.add(NO_2SP_SOFT_TABS_CHECK);
        FIELDS_TO_COPY.add(NO_ARROW_ALIGNMENT_CHECK);
        FIELDS_TO_COPY.add(NO_UNQUOTED_RESOURCE_TITLE_CHECK);
        FIELDS_TO_COPY.add(NO_ENSURE_FIRST_PARAM_CHECK);
        FIELDS_TO_COPY.add(NO_DUPLICATE_PARAMS_CHECK);
        FIELDS_TO_COPY.add(NO_UNQUOTED_FILE_MODE_CHECK);
        FIELDS_TO_COPY.add(NO_FILE_MODE_CHECK);
        FIELDS_TO_COPY.add(NO_ENSURE_NOT_SYMLINK_TARGET_CHECK);        
    }
 
    // ------------------------------------------------------------------------------------------------- Type Properties
    // ---------------------------------------------------------------------------------------------------- Dependencies
    public TextProvider textProvider;
    public UIConfigSupport uiConfigSupport;
    
    // ---------------------------------------------------------------------------------------------------- Constructors
    // ----------------------------------------------------------------------------------------------- Interface Methods
    @Override
    public void populateContextForCreate(@NotNull Map<String, Object> context)
    {
        super.populateContextForCreate(context);
        populateContextForAllOperations(context);
    }

    @Override
    public void populateContextForEdit(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition)
    {
        super.populateContextForEdit(context, taskDefinition);
        populateContextForAllOperations(context);
        taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS_TO_COPY);
    }

    public void populateContextForAllOperations(@NotNull Map<String, Object> context)
    {
        context.put(CTX_UI_CONFIG_BEAN, uiConfigSupport);
    }

    public void setTextProvider(TextProvider textProvider)
    {
        this.textProvider = textProvider;
    }

    public void setUiConfigSupport(UIConfigSupport uiConfigSupport)
    {
        this.uiConfigSupport = uiConfigSupport;
    }
    
    @Override
    public void validate(@NotNull ActionParametersMap params, @NotNull ErrorCollection errorCollection)
    {
        super.validate(params, errorCollection);

//        if (StringUtils.isEmpty(params.getString(AbstractPuppetConfigurator.RUNTIME)))
//        {
//            errorCollection.addError(AbstractPuppetConfigurator.RUNTIME, super.textProvider.getText("lint.runtime.error"));
//        }
//        if (StringUtils.isEmpty(params.getString(AbstractPuppetConfigurator.COMMAND)))
//        {
//            errorCollection.addError(AbstractPuppetConfigurator.COMMAND, super.textProvider.getText("lint.command.error"));
//        }
    }

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull ActionParametersMap params, @Nullable TaskDefinition previousTaskDefinition)
    {
        final Map<String, String> map = super.generateTaskConfigMap(params, previousTaskDefinition);
        taskConfiguratorHelper.populateTaskConfigMapWithActionParameters(map, params, FIELDS_TO_COPY);
        return map;
    }
    
    @NotNull
    @Override
    public Set<Requirement> calculateRequirements(@NotNull final TaskDefinition taskDefinition, @NotNull final Job job)
    {
        Set<Requirement> requirements = Sets.newHashSet();
        taskConfiguratorHelper.addSystemRequirementFromConfiguration(requirements, taskDefinition,
                                        LintTask.PUPPET_LINT_LABEL, LintTask.PUPPET_LINT_CAPABILITY_PREFIX);
        return requirements;
    }

    // -------------------------------------------------------------------------------------------------- Action Methods
    // -------------------------------------------------------------------------------------------------- Public Methods
    // ------------------------------------------------------------------------------------------------- Helper Methods
    // -------------------------------------------------------------------------------------- Basic Accessors / Mutators
}