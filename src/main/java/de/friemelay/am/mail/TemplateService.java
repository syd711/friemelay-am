package de.friemelay.am.mail;

import de.friemelay.am.config.Config;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Template service instance based on FreeMarker.
 */
public class TemplateService {
  private final static Logger LOG = Logger.getLogger(TemplateService.class);

  public static final String ENCODING = "UTF-8";

  private static TemplateSet templateSet;

  public static TemplateSet getTemplateSet() {
    try {
      Configuration cfg = new Configuration();
      cfg.setDirectoryForTemplateLoading(new File(Config.getString("template.directory")));
      cfg.setDefaultEncoding(ENCODING);
      cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
      return new TemplateSet(cfg);
    } catch (Exception e) {
      LOG.error("Failed to create template set for directory " + new File(Config.getString("template.directory")).getAbsolutePath() + ": " + e.getMessage(), e);
    }
    return null;
  }
}
