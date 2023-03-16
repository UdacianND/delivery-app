package uz.md.shopapp.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.md.shopapp.domain.Institution;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.institution.InstitutionAddDTO;
import uz.md.shopapp.dtos.institution.InstitutionDTO;
import uz.md.shopapp.dtos.institution.InstitutionEditDTO;
import uz.md.shopapp.dtos.institution.InstitutionInfoDTO;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.BadRequestException;
import uz.md.shopapp.exceptions.NotAllowedException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.InstitutionMapper;
import uz.md.shopapp.repository.InstitutionRepository;
import uz.md.shopapp.repository.InstitutionTypeRepository;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.FilesStorageService;
import uz.md.shopapp.service.contract.InstitutionService;

import java.nio.file.Path;
import java.util.List;

import static uz.md.shopapp.utils.MessageConstants.*;

@Service
public class InstitutionServiceImpl implements InstitutionService {

    @Value("${app.images.institutions.root.path}")
    private String institutionsPath;

    private Path institutionsImagesRoot;

    @PostConstruct
    public void init() {
        institutionsImagesRoot = Path.of(institutionsPath);
    }

    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;
    private final FilesStorageService filesStorageService;
    private final UserRepository userRepository;
    private final InstitutionTypeRepository institutionTypeRepository;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository,
                                  InstitutionMapper institutionMapper,
                                  FilesStorageService filesStorageService,
                                  UserRepository userRepository,
                                  InstitutionTypeRepository institutionTypeRepository) {
        this.institutionRepository = institutionRepository;
        this.institutionMapper = institutionMapper;
        this.filesStorageService = filesStorageService;
        this.userRepository = userRepository;
        this.institutionTypeRepository = institutionTypeRepository;
    }

    @Override
    public ApiResult<InstitutionDTO> add(InstitutionAddDTO dto) {

        if (dto == null
                || dto.getNameUz() == null
                || dto.getNameRu() == null
                || dto.getInstitutionTypeId() == null
                || dto.getManagerId() == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();

        if (institutionRepository.existsByNameUzOrNameRu(dto.getNameUz(), dto.getNameRu()))
            throw AlreadyExistsException.builder()
                    .messageRu("")
                    .messageUz("INSTITUTION_NAME_ALREADY_EXISTS")
                    .build();

        Institution institution = institutionMapper
                .fromAddDTO(dto);

        institutionTypeRepository.findById(dto.getInstitutionTypeId())
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz(INSTITUTION_TYPE_NOT_FOUND_UZ)
                        .messageRu(INSTITUTION_TYPE_NOT_FOUND_RU)
                        .build());

        User manager = userRepository
                .findById(dto.getManagerId())
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("MANAGER_NOT_FOUND")
                        .messageRu("")
                        .build());

        if (!manager.getRole().getName().equals("MANAGER"))
            throw NotAllowedException.builder()
                    .messageRu("")
                    .messageUz("User with id " + dto.getManagerId() + " is not a manager")
                    .build();

        institution.setManager(manager);
        return ApiResult
                .successResponse(institutionMapper
                        .toDTO(institutionRepository
                                .save(institution)));
    }

    @Override
    public ApiResult<InstitutionDTO> findById(Long id) {
        if (id == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();

        return ApiResult.successResponse(institutionMapper
                .toDTO(institutionRepository
                        .findById(id)
                        .orElseThrow(() -> NotFoundException.builder()
                                .messageUz(INSTITUTION_NOT_FOUND_UZ)
                                .messageRu(INSTITUTION_NOT_FOUND_RU)
                                .build())));
    }

    @Override
    public ApiResult<InstitutionDTO> edit(InstitutionEditDTO editDTO) {

        if (editDTO == null
                || editDTO.getNameUz() == null
                || editDTO.getId() == null
                || editDTO.getNameRu() == null
                || editDTO.getLocation() == null
                || editDTO.getInstitutionTypeId() == null
                || editDTO.getManagerId() == null)
            throw BadRequestException.builder()
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .build();

        Institution editing = institutionRepository
                .findById(editDTO.getId())
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz(INSTITUTION_NOT_FOUND_UZ)
                        .messageRu(INSTITUTION_NOT_FOUND_RU)
                        .build());

        if (institutionRepository.existsByNameUzOrNameRuAndIdIsNot(editDTO.getNameUz(), editDTO.getNameRu(), editing.getId()))
            throw AlreadyExistsException.builder()
                    .messageRu("")
                    .messageUz("INSTITUTION_NAME_ALREADY_EXISTS")
                    .build();

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
        if (typeId == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();
        return ApiResult.successResponse(institutionRepository
                .findAllForInfoByTypeId(typeId));
    }

    @Override
    public ApiResult<List<InstitutionInfoDTO>> getAllByManagerId(Long managerId) {
        if (managerId == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();
        return ApiResult.successResponse(institutionRepository
                .findAllForInfoByManagerId(managerId));
    }

    @Override
    public ApiResult<List<InstitutionInfoDTO>> getAllForInfoByPage(String page) {
        if (page == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();
        int[] paged = {Integer.parseInt(page.split("-")[0]),
                Integer.parseInt(page.split("-")[1])};
        return ApiResult.successResponse(institutionRepository
                .findAllForInfo(PageRequest.of(paged[0], paged[1]))
                .getContent());
    }

    @Override
    public ApiResult<Void> setImage(Long institutionId, MultipartFile image) {
        if (institutionId == null || image == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();
        Institution institution = institutionRepository
                .findById(institutionId)
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz(INSTITUTION_NOT_FOUND_UZ)
                        .messageRu(INSTITUTION_NOT_FOUND_RU)
                        .build());
        filesStorageService.save(image, institutionsImagesRoot);
        institution.setImageUrl(institutionsImagesRoot.toUri() + image.getOriginalFilename());
        institutionRepository.save(institution);
        return ApiResult.successResponse();
    }

    @Override
    public ApiResult<Void> delete(Long id) {
        if (id == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();
        institutionRepository.deleteById(id);
        return ApiResult.successResponse();
    }
}
