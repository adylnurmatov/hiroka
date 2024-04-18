package com.example.hiroka.views.admin;

import com.example.hiroka.form.UserForm;
import com.example.hiroka.service.UserService;
import com.example.hiroka.user.User;
import com.example.hiroka.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;


@PageTitle("Admin/users")
@Route(value = "/admin/users", layout = MainLayout.class)
@PermitAll
public class AdminUsersView extends VerticalLayout {
    private final UserService userService;
    Grid<User> grid = new Grid<>(User.class);
    TextField filterText =new TextField();

    UserForm userForm;


    public AdminUsersView(UserService userService) {
        this.userService = userService;
        addClassName("list-view");
        setSizeFull();

        configureUserForm();
        configureGrid();
        add(
                getToolBar(),
                getContent()
        );
        updateList();
        closeEditor();

    }

    private void closeEditor() {
        userForm.setUser(null);
        userForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(userService.findAllUsers(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(grid, userForm);
        horizontalLayout.setFlexGrow(2, grid);
        horizontalLayout.setFlexGrow(1, userForm);
        horizontalLayout.addClassName("content");
        horizontalLayout.setSizeFull();
        return horizontalLayout;
    }

    private void configureUserForm() {
        userForm = new UserForm();
        userForm.setWidth("25em");

        userForm.addSaveListener(this::saveUser);
        userForm.addDeleteListener(this::deleteUser);
        userForm.addCloseListener(e -> closeEditor());
    }

    private void deleteUser(UserForm.DeleteEvent deleteEvent) {
        userService.deleteUser(deleteEvent.getUser());
        updateList();
        closeEditor();
    }

    private void saveUser(UserForm.SaveEvent event){
        userService.saveUser(event.getUser());
        updateList();
        closeEditor();
    }

    private Component getToolBar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button contactBtn = new Button("Add contact");
        contactBtn.addClickListener(e -> addContact());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, contactBtn);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editUser(new User());
    }

    private void configureGrid() {
        grid.addClassName("user-grid");
        grid.setSizeFull();
        grid.setColumns("username", "phoneNumber", "email");
        grid.getColumns().forEach(contactColumn -> contactColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editUser(e.getValue()));
    }

    private void editUser(User value) {
        if(value == null){
            closeEditor();
        }else {
            userForm.setUser(value);
            userForm.setVisible(true);
            addClassName("editing");
        }
    }
}
