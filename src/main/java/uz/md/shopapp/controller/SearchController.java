package uz.md.shopapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.institution.InstitutionDTO;
import uz.md.shopapp.service.contract.SearchService;
import uz.md.shopapp.utils.AppConstants;

import java.util.List;

@RestController
@RequestMapping(SearchController.BASE_URL)
@RequiredArgsConstructor
public class SearchController {

    public static final String BASE_URL = AppConstants.BASE_URL + "/search";

    private final SearchService searchService;

    @GetMapping
    public ApiResult<List<InstitutionDTO>> getBySearchByPage(@RequestParam String value) {
        return searchService.getBySearch(value);
    }

}
