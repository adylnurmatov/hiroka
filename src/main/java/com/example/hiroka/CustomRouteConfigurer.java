package com.example.hiroka;

import com.example.hiroka.views.MainLayout;
import com.example.hiroka.views.admin.AdminpodcastsView;
import com.example.hiroka.views.admin.AdminusersView;
import com.vaadin.flow.router.RouteConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomRouteConfigurer {

    public static void configureRoutes() {
        RouteConfiguration configuration = RouteConfiguration.forApplicationScope();

        // Получение текущего пользователя и его ролей
        // Это зависит от вашей системы безопасности, здесь пример с Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Регистрация маршрутов в зависимости от ролей
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            // Только для администраторов
            configuration.setRoute("podcasts", AdminpodcastsView.class, MainLayout.class);
            configuration.setRoute("users", AdminusersView.class, MainLayout.class);
        }

        // Общедоступные страницы

    }
}
