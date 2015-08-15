package de.friemelay.am.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.log4j.Logger;

import java.io.StringWriter;
import java.io.Writer;

public class TemplateSet {
  private final static Logger LOG = Logger.getLogger(TemplateSet.class);

  private Configuration configuration;

  public TemplateSet(Configuration configuration) {
    this.configuration = configuration;
  }

  public String renderTemplate(String name, Object model) {
    try {
      Template temp = configuration.getTemplate(name);
      Writer writer = new StringWriter();
      temp.process(model, writer);
      return writer.toString();
    } catch (Exception e) {
      LOG.error("Error rendering template '" + name + "': " + e.getMessage(), e);
    }
    return null;
  }
}
