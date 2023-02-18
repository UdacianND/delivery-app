package uz.md.shopapp.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.md.shopapp.domain.Institution;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.institution.InstitutionAddDTO;
import uz.md.shopapp.dtos.institution.InstitutionDTO;
import uz.md.shopapp.dtos.institution.InstitutionEditDTO;
import uz.md.shopapp.dtos.institution.InstitutionInfoDTO;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.InstitutionMapper;
import uz.md.shopapp.repository.InstitutionRepository;
import uz.md.shopapp.service.contract.FilesStorageService;
import uz.md.shopapp.service.contract.InstitutionService;
import uz.md.shopapp.utils.AppConstants;

import java.nio.file.Path;
import java.util.List;

@Service
public class InstitutionServiceImpl implements InstitutionService {

    @Value("${app.images.institutions.root.path}")
    private String institutionsPath;

    private Path institutionsImagesRoot ;

    @PostConstruct
    public void init() {
        institutionsImagesRoot = Path.of(institutionsPath);
    }

    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;
    private final FilesStorageService filesStorageService;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository,
                                  InstitutionMapper institutionMapper,
                                  FilesStorageService filesStorageService) {
        this.institutionRepository = institutionRepository;
        this.institutionMapper = institutionMapper;
        this.filesStorageService = filesStorageService;
    }

    @Override
    public ApiResult<InstitutionDTO> add(InstitutionAddDTO dto) {

        if (institutionRepository.existsByNameUzOrNameRu(dto.getNameUz(), dto.getNameRu()))
            throw new AlreadyExistsException("INSTITUTION_NAME_ALREADY_EXISTS");

        Institution institution = institutionMapper
                .fromAddDTO(dto);

        return ApiResult
                .successResponse(institutionMapper
                        .toDTO(institutionRepository
                                .save(institution)));
    }

    @Override
    public ApiResult<InstitutionDTO> findById(Long id) {
        return ApiResult.successResponse(institutionMapper
                .toDTO(institutionRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException("INSTITUTION_NOT_FOUND"))));
    }

    @Override
    public ApiResult<InstitutionDTO> edit(InstitutionEditDTO editDTO) {

        Institution editing = institutionRepository
                .findById(editDTO.getId())
                .orElseThrow(() -> new NotFoundException("INSTITUTION_NOT_FOUND"));

        if (institutionRepository.existsByNameUzOrNameRuAndIdIsNot(editDTO.getNameUz(), editDTO.getNameRu(), editing.getId()))
            throw new AlreadyExistsException("INSTITUTION_NAME_ALREADY_EXISTS");

        Institution institution = institutionMapper.fromEditDTO(editDTO, editing);

        return ApiResult.successResponse(institutionMapper
                .toDTO(institutionRepository.save(institution)));
    }

    @Override
    public ApiResult<List<InstitutionDTO>> getAll() {
        return ApiResult.successResponse(institutionMapper
                .toDTOList(institutionRepository
                        .findAll()));
    }

    @Override
    public ApiResult<List<InstitutionInfoDTO>> getAllForInfo() {
        return ApiResult.successResponse(institutionRepository
                .findAllForInfo());
    }

    @Override
    public ApiResult<List<InstitutionInfoDTO>> getAllByTypeId(Long typeId) {
        return ApiResult.successResponse(institutionRepository
                .findAllForInfoByTypeId(typeId));
    }

    @Override
    public ApiResult<List<InstitutionInfoDTO>> getAllByManagerId(Long managerId) {
        return ApiResult.successResponse(institutionRepository
                .findAllForInfoByManagerId(managerId));
    }

    @Override
    public ApiResult<List<InstitutionInfoDTO>> getAllForInfoByPage(String page) {
        int[] paged = {Integer.parseInt(page.split("-")[0]),
                Integer.parseInt(page.split("-")[1])};
        return ApiResult.successResponse(institutionRepository
                .findAllForInfo(PageRequest.of(paged[0], paged[1]))
                .getContent());
    }

    @Override
    public ApiResult<Void> setImage(Long institutionId, MultipartFile image) {
        Institution institution = institutionRepository
                .findById(institutionId)
                .orElseThrow(() -> new NotFoundException("INSTITUTION NOT FOUND"));
        filesStorageService.save(image, institutionsImagesRoot);
        institution.setImageUrl(institutionsImagesRoot.toUri() + image.getOriginalFilename());
        institutionRepository.save(institution);
        return ApiResult.successResponse();
    }

    @Override
    public ApiResult<Void> delete(Long id) {
        institutionRepository.deleteById(id);
        return ApiResult.successResponse();
    }
}
