/**
 * Copyright 2013-2018 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 *   Author: Guoqiang Chen
 *    Email: subchen@gmail.com
 *   WebURL: https://github.com/subchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import jetbrick.config.Config;
import jetbrick.config.ConfigLoader;
import jetbrick.template.loader.ClasspathResourceLoader;
import jetbrick.template.loader.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JetConfig {
    public static final String DEFAULT_CONFIG_FILE = "classpath:/jetbrick-template.properties";

    public static final String IMPORT_CLASSES = "jetx.import.classes";
    public static final String IMPORT_METHODS = "jetx.import.methods";
    public static final String IMPORT_FUNCTIONS = "jetx.import.functions";
    public static final String IMPORT_TAGS = "jetx.import.tags";
    public static final String IMPORT_MACROS = "jetx.import.macros";
    public static final String IMPORT_DEFINES = "jetx.import.defines";
    public static final String AUTOSCAN_PACKAGES = "jetx.autoscan.packages";
    public static final String AUTOSCAN_SKIPERRORS = "jetx.autoscan.skiperrors";
    public static final String TEMPLATE_LOADERS = "jetx.template.loaders";
    public static final String TEMPLATE_SUFFIX = "jetx.template.suffix";
    public static final String TEMPLATE_SOURCE_SECURITYMANAGER = "jetx.template.source.securityManager";
    public static final String INPUT_ENCODING = "jetx.input.encoding";
    public static final String OUTPUT_ENCODING = "jetx.output.encoding";
    public static final String SYNTAX_STRICT = "jetx.syntax.strict";
    public static final String SYNTAX_SAFECALL = "jetx.syntax.safecall";
    public static final String TRIM_LEADING_WHITESPACES = "jetx.trim.leading.whitespaces";
    public static final String TRIM_DIRECTIVE_WHITESPACES = "jetx.trim.directive.whitespaces";
    public static final String TRIM_DIRECTIVE_COMMENTS = "jetx.trim.directive.comments";
    public static final String TRIM_DIRECTIVE_COMMENTS_PREFIX = "jetx.trim.directive.comments.prefix";
    public static final String TRIM_DIRECTIVE_COMMENTS_SUFFIX = "jetx.trim.directive.comments.suffix";
    public static final String IO_SKIPERRORS = "jetx.io.skiperrors";

    private final Logger log = LoggerFactory.getLogger(JetConfig.class);
    private List<String> importClasses;
    private List<String> importMethods;
    private List<String> importFunctions;
    private List<String> importTags;
    private List<String> importMacros;
    private List<String> importDefines;
    private List<String> autoscanPackages;
    private boolean autoscanSkiperrors;
    private List<ResourceLoader> templateLoaders;
    private String templateSuffix;
    private JetSecurityManager templateSecurityManager;
    private Charset inputEncoding;
    private Charset outputEncoding;
    private boolean syntaxStrict;
    private boolean syntaxSafecall;
    private boolean trimLeadingWhitespaces;
    private boolean trimDirectiveWhitespaces;
    private boolean trimDirectiveComments;
    private String trimDirectiveCommentsPrefix;
    private String trimDirectiveCommentsSuffix;
    private boolean ioSkiperrors;

    protected JetConfig(Properties config, String configLocation) {
        ConfigLoader loader = new ConfigLoader();

        if (config != null) {
            loader.load(config);
        }

        if (configLocation != null) {
            try {
                log.info("Loading config file: {}", configLocation);
                loader.load(configLocation);
            } catch (IllegalStateException e) {
                // 默认配置文件允许不存在
                if (!DEFAULT_CONFIG_FILE.equals(configLocation)) {
                    throw e;
                }
                log.warn("no default config file found: {}", DEFAULT_CONFIG_FILE);
            }
        }

        initialize(loader.asConfig());
    }

    private void initialize(Config config) {
        importClasses = config.asStringList(IMPORT_CLASSES);
        importMethods = config.asStringList(IMPORT_METHODS);
        importFunctions = config.asStringList(IMPORT_FUNCTIONS);
        importTags = config.asStringList(IMPORT_TAGS);
        importMacros = config.asStringList(IMPORT_MACROS);
        importDefines = config.asStringList(IMPORT_DEFINES);
        autoscanPackages = config.asStringList(AUTOSCAN_PACKAGES);
        autoscanSkiperrors = config.asBoolean(AUTOSCAN_SKIPERRORS, "false");
        templateLoaders = config.asObjectList(TEMPLATE_LOADERS, ResourceLoader.class, ClasspathResourceLoader.class.getName());
        templateSuffix = config.asString(TEMPLATE_SUFFIX, ".jetx");
        templateSecurityManager = config.asObject(TEMPLATE_SOURCE_SECURITYMANAGER, JetSecurityManager.class);
        inputEncoding = config.asCharset(INPUT_ENCODING, "utf-8");
        outputEncoding = config.asCharset(OUTPUT_ENCODING, "utf-8");
        syntaxStrict = config.asBoolean(SYNTAX_STRICT, "false");
        syntaxSafecall = config.asBoolean(SYNTAX_SAFECALL, "false");
        trimLeadingWhitespaces = config.asBoolean(TRIM_LEADING_WHITESPACES, "false");
        trimDirectiveWhitespaces = config.asBoolean(TRIM_DIRECTIVE_WHITESPACES, "true");
        trimDirectiveComments = config.asBoolean(TRIM_DIRECTIVE_COMMENTS, "false");
        trimDirectiveCommentsPrefix = config.asString(TRIM_DIRECTIVE_COMMENTS_PREFIX, "<!--");
        trimDirectiveCommentsSuffix = config.asString(TRIM_DIRECTIVE_COMMENTS_SUFFIX, "-->");
        ioSkiperrors = config.asBoolean(IO_SKIPERRORS, "false");
    }

    public List<String> getImportClasses() {
        return importClasses;
    }

    public List<String> getImportMethods() {
        return importMethods;
    }

    public List<String> getImportFunctions() {
        return importFunctions;
    }

    public List<String> getImportTags() {
        return importTags;
    }

    public List<String> getImportMacros() {
        return importMacros;
    }

    public List<String> getImportDefines() {
        return importDefines;
    }

    public List<String> getAutoscanPackages() {
        return autoscanPackages;
    }

    public boolean isAutoscanSkiperrors() {
        return autoscanSkiperrors;
    }

    public List<ResourceLoader> getTemplateLoaders() {
        return templateLoaders;
    }

    public String getTemplateSuffix() {
        return templateSuffix;
    }

    public JetSecurityManager getTemplateSecurityManager() {
        return templateSecurityManager;
    }

    public Charset getInputEncoding() {
        return inputEncoding;
    }

    public Charset getOutputEncoding() {
        return outputEncoding;
    }

    public boolean isSyntaxStrict() {
        return syntaxStrict;
    }

    public boolean isSyntaxSafecall() {
        return syntaxSafecall;
    }

    public boolean isTrimLeadingWhitespaces() {
        return trimLeadingWhitespaces;
    }

    public boolean isTrimDirectiveWhitespaces() {
        return trimDirectiveWhitespaces;
    }

    public boolean isTrimDirectiveComments() {
        return trimDirectiveComments;
    }

    public String getTrimDirectiveCommentsPrefix() {
        return trimDirectiveCommentsPrefix;
    }

    public String getTrimDirectiveCommentsSuffix() {
        return trimDirectiveCommentsSuffix;
    }

    public boolean isIoSkiperrors() {
        return ioSkiperrors;
    }

}
