package org.mubasherusman.cms.views.clients;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;
import java.util.Optional;
import org.mubasherusman.cms.data.Client;
import org.mubasherusman.cms.services.ClientService;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Clients")
@Menu(icon = "line-awesome/svg/person-booth-solid.svg", order = 2)
@Route(value = "master-detail/:clientID?/:action?(edit)")
@RolesAllowed("ADMIN")
public class ClientsView extends Div implements BeforeEnterObserver {

    private final String CLIENT_ID = "clientID";
    private final String CLIENT_EDIT_ROUTE_TEMPLATE = "master-detail/%s/edit";

    private final Grid<Client> grid = new Grid<>(Client.class, false);

    private TextField firstName;
    private TextField lastName;
    private TextField fatherName;
    private TextField nic;
    private TextField cast;
    private TextField occupation;
    private TextField email;
    private TextField phone;
    private DatePicker dateOfBirth;
    private TextField address;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Client> binder;

    private Client client;

    private final ClientService clientService;

    public ClientsView(ClientService clientService) {
        this.clientService = clientService;
        addClassNames("clients-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("fatherName").setAutoWidth(true);
        grid.addColumn("nic").setAutoWidth(true);
        grid.addColumn("cast").setAutoWidth(true);
        grid.addColumn("occupation").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("phone").setAutoWidth(true);
        grid.addColumn("dateOfBirth").setAutoWidth(true);
        grid.addColumn("address").setAutoWidth(true);
        grid.setItems(query -> clientService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CLIENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ClientsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Client.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.client == null) {
                    this.client = new Client();
                }
                binder.writeBean(this.client);
                clientService.update(this.client);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(ClientsView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> clientId = event.getRouteParameters().get(CLIENT_ID).map(Long::parseLong);
        if (clientId.isPresent()) {
            Optional<Client> clientFromBackend = clientService.get(clientId.get());
            if (clientFromBackend.isPresent()) {
                populateForm(clientFromBackend.get());
            } else {
                Notification.show(String.format("The requested client was not found, ID = %s", clientId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ClientsView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        fatherName = new TextField("Father Name");
        nic = new TextField("Nic");
        cast = new TextField("Cast");
        occupation = new TextField("Occupation");
        email = new TextField("Email");
        phone = new TextField("Phone");
        dateOfBirth = new DatePicker("Date Of Birth");
        address = new TextField("Address");
        formLayout.add(firstName, lastName, fatherName, nic, cast, occupation, email, phone, dateOfBirth, address);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Client value) {
        this.client = value;
        binder.readBean(this.client);

    }
}
