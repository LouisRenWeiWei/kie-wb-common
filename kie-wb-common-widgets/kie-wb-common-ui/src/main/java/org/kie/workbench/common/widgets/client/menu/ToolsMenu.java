package org.kie.workbench.common.widgets.client.menu;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.guvnor.common.services.project.context.ProjectContext;
import org.guvnor.common.services.project.events.PackageChangeEvent;
import org.guvnor.common.services.project.events.ProjectChangeEvent;
import org.guvnor.common.services.project.model.Project;
import org.guvnor.common.services.project.service.ProjectService;
import org.jboss.errai.common.client.api.Caller;
import org.kie.workbench.common.widgets.client.resources.i18n.ToolsMenuConstants;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.mvp.Command;
import org.uberfire.workbench.events.OrganizationalUnitChangeEvent;
import org.uberfire.workbench.events.RepositoryChangeEvent;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;

@ApplicationScoped
public class ToolsMenu {

    @Inject
    private PlaceManager placeManager;

    @Inject
    protected Caller<ProjectService> projectService;

    @Inject
    protected ProjectContext context;

    private MenuItem projectScreen = MenuFactory.newSimpleItem( ToolsMenuConstants.INSTANCE.ProjectEditor() ).respondsWith(
            new Command() {
                @Override
                public void execute() {
                    placeManager.goTo( "projectScreen" );
                }
            } ).endMenu().build().getItems().get( 0 );

    private MenuItem dataModelerScreen = MenuFactory.newSimpleItem( ToolsMenuConstants.INSTANCE.DataModeller() ).respondsWith(
            new Command() {
                @Override
                public void execute() {
                    placeManager.goTo( "dataModelerScreen" );
                }
            } ).endMenu().build().getItems().get( 0 );

    public List<MenuItem> getMenuItems() {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

        menuItems.add( projectScreen );
        menuItems.add( dataModelerScreen );

        return menuItems;
    }

    public void selectedGroupChanged( @Observes final OrganizationalUnitChangeEvent event ) {
        enableToolsMenuItems( context.getActiveProject() );
    }

    public void selectedRepositoryChanged( @Observes final RepositoryChangeEvent event ) {
        enableToolsMenuItems( context.getActiveProject() );
    }

    public void selectedProjectChanged( @Observes final ProjectChangeEvent event ) {
        enableToolsMenuItems( context.getActiveProject() );
    }

    public void selectedPackageChanged( @Observes final PackageChangeEvent event ) {
        enableToolsMenuItems( context.getActiveProject() );
    }

    private void enableToolsMenuItems( final Project project ) {
        final boolean enabled = ( project != null );
        projectScreen.setEnabled( enabled );
        dataModelerScreen.setEnabled( enabled );
    }

}
