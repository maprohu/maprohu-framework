/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package hu.mapro.gxt.theme.green.client.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import hu.mapro.gxt.theme.green.client.tabs.BluePlainTabPanelAppearance.BluePlainTabPanelResources;
import hu.mapro.gxt.theme.green.client.tabs.BluePlainTabPanelAppearance.BluePlainTabPanelStyle;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel.PlainTabPanelAppearance;

/**
 * A blue-coloured appearance for {@link PlainTabPanel} with tabs below the
 * content area. This appearance differs from
 * {@link BlueTabPanelBottomAppearance} in that it has a simplified tab strip.
 */
public class BluePlainTabPanelBottomAppearance extends BlueTabPanelBottomAppearance implements PlainTabPanelAppearance {

  public interface PlainTabPanelBottomTemplates extends BottomTemplate {

    @XTemplate(source = "TabPanelBottom.html")
    SafeHtml render(TabPanelStyle style);

    @XTemplate(source = "PlainTabPanelBottom.html")
    SafeHtml renderPlain(BluePlainTabPanelStyle style);

  }

  protected PlainTabPanelBottomTemplates template;
  protected BluePlainTabPanelResources resources;

  public BluePlainTabPanelBottomAppearance() {
    this(GWT.<BluePlainTabPanelResources> create(BluePlainTabPanelResources.class),
        GWT.<PlainTabPanelBottomTemplates> create(PlainTabPanelBottomTemplates.class),
        GWT.<ItemTemplate> create(ItemTemplate.class));
  }

  public BluePlainTabPanelBottomAppearance(BluePlainTabPanelResources resources, PlainTabPanelBottomTemplates template,
      ItemTemplate itemTemplate) {
    super(resources, template, itemTemplate);
    this.resources = resources;
    this.template = template;
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(template.renderPlain(resources.style()));
  }

}
