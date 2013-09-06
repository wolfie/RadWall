package com.github.wolfie.radwall;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.annotation.WebServlet;

import org.apache.http.client.ClientProtocolException;

import com.github.wolfie.radwall.rest.ChangeDAO;
import com.github.wolfie.radwall.rest.GerritAPI;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
@Theme("radwall")
public class RadwallUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = RadwallUI.class, widgetset = "com.github.wolfie.radwall.widgetset.RadwallWidgetset")
    public static class Servlet extends VaadinServlet {
    }

    private final GerritAPI api = new GerritAPI();

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        setContent(layout);

        try {

            List<ChangeDAO> allChanges = api.getAllChanges();

            Map<String, List<ChangeDAO>> projectToChanges = new HashMap<String, List<ChangeDAO>>();
            for (ChangeDAO change : allChanges) {
                List<ChangeDAO> changesInProject = projectToChanges
                        .get(change.project);
                if (changesInProject == null) {
                    changesInProject = new ArrayList<ChangeDAO>();
                    projectToChanges.put(change.project, changesInProject);
                }
                changesInProject.add(change);
            }

            layout.addComponent(new Label("Currently open changes: "
                    + allChanges.size()));
            for (ChangeDAO change : allChanges) {
                layout.addComponent(new Label(change.subject));
            }

            for (Entry<String, List<ChangeDAO>> entry : projectToChanges
                    .entrySet()) {
                Label heading = new Label(entry.getKey());
                heading.setStyleName(Reindeer.LABEL_H1);
                layout.addComponent(heading);

                ArrayList<ChangeDAO> changes = new ArrayList<ChangeDAO>(
                        entry.getValue());
                Collections.sort(changes, new Comparator<ChangeDAO>() {
                    @Override
                    public int compare(ChangeDAO o1, ChangeDAO o2) {
                        int branch = o1.branch.compareTo(o2.branch);
                        if (branch != 0)
                            return branch;
                        else
                            return o1.created.compareTo(o2.created);
                    }
                });

                for (ChangeDAO change : changes) {
                    layout.addComponent(new Label(change.subject + " ("
                            + change.branch + ")"));
                }
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}