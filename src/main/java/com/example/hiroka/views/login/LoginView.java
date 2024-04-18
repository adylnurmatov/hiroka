package com.example.hiroka.views.login;

import com.example.hiroka.repo.UserRepository;
import com.example.hiroka.service.AuthService;
import com.example.hiroka.user.Role;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;

@Route("login")
@AnonymousAllowed

public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthService authService;
    private final UserRepository userRepository;

    LoginForm login = new LoginForm();


    private Button register;

    public LoginView(AuthService authService, UserRepository repository){

        System.out.println("\n\n\n\n\n\n"+ VaadinSession.getCurrent().getSession().getAttribute("email"));
        this.authService = authService;
        this.userRepository = repository;

        login.setClassName("Login");
        login.setAction("login");
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);


        login.addLoginListener( e -> {
            try {
                authService.login(e.getUsername(), e.getPassword());
                Notification.show("successfully login");
                if(repository.findByEmail(e.getUsername()).get().getRole().equals(Role.ADMIN)){
                    UI.getCurrent().navigate("admin/users");
                }
                else {
                    UI.getCurrent().navigate("main");
                }
            }catch (Exception exception){
                Notification.show("incorrect p/login");
                throw new BadCredentialsException(exception.getMessage());
            }
        });
        register = new Button("register");
        register.addClickListener( e -> {
            UI.getCurrent().navigate("register");
        });

        add(login, register);

    }
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (VaadinSession.getCurrent().getSession().getAttribute("email") != null){
            System.out.println("its works2");
            beforeEnterEvent.forwardTo("main");

        }

        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }

    }
}