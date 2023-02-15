package uz.md.shopapp.resource;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.md.shopapp.service.contract.ClientService;
import uz.md.shopapp.utils.AppConstants;

@RestController
@RequestMapping(ClientResource.BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Client", description = "Endpoints for Client")
@Slf4j
public class ClientResource {

    public static final String BASE_URL = AppConstants.BASE_URL + "client";
    private final ClientService clientService;


}
