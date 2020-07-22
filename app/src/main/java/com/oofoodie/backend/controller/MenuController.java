package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.EditMenuCommand;
import com.oofoodie.backend.command.GetMenuCommand;
import com.oofoodie.backend.models.entity.Menu;
import com.oofoodie.backend.models.request.EditMenuRequest;
import com.oofoodie.backend.models.request.MenuRequest;
import com.oofoodie.backend.models.request.command.EditMenuCommandRequest;
import com.oofoodie.backend.models.request.command.GetMenuCommandRequest;
import com.oofoodie.backend.models.response.MenuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MenuController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping("/api/menu/{restaurantId}")
    public Mono<Response<List<MenuResponse>>> getMenu(@PathVariable String restaurantId) {
        return commandExecutor.execute(GetMenuCommand.class, constructGetMenuCommandRequest(restaurantId))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/api/merchant/menu/{restaurantId}")
    public Mono<Response<List<MenuResponse>>> editMenu(@PathVariable String restaurantId,
                                                            @RequestBody EditMenuRequest menu,
                                                            Authentication authentication) {
        return commandExecutor.execute(EditMenuCommand.class, constructEditMenuCommandRequest(menu, authentication.getName(), restaurantId))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    private EditMenuCommandRequest constructEditMenuCommandRequest(EditMenuRequest menu,
                                                                   String merchantUsername,
                                                                   String restaurantId) {
        return EditMenuCommandRequest.builder()
                .restaurantId(restaurantId)
                .merchantUsername(merchantUsername)
                .menu(constructMenu(menu.getMenus()))
                .build();
    }

    private List<Menu> constructMenu(List<MenuRequest> menuRequests) {
        return menuRequests.stream()
                .map(menuRequest -> Menu.builder()
                        .name(menuRequest.getName())
                        .price(menuRequest.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    private GetMenuCommandRequest constructGetMenuCommandRequest(String restaurantId) {
        return GetMenuCommandRequest.builder()
                .restaurantId(restaurantId)
                .build();
    }
}
