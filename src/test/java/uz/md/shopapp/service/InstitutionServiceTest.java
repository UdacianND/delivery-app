package uz.md.shopapp.service;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uz.md.shopapp.domain.Address;
import uz.md.shopapp.domain.Institution;
import uz.md.shopapp.domain.InstitutionType;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.institution.InstitutionAddDTO;
import uz.md.shopapp.dtos.institution.InstitutionDTO;
import uz.md.shopapp.dtos.institution.InstitutionEditDTO;
import uz.md.shopapp.dtos.institution.InstitutionInfoDTO;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.repository.*;
import uz.md.shopapp.service.contract.InstitutionService;
import uz.md.shopapp.util.TestUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class InstitutionServiceTest {

    @Value("${app.images.institutions.root.path}")
    private String rootPathUrl;

    private Path rootPath;

    @PostConstruct
    public void init() throws Exception {
        rootPath = Path.of(rootPathUrl);
    }

    private static final String TYPE_NAME_UZ = "typeUz";
    private static final String TYPE_NAME_RU = "typeRu";

    private static final String TYPE_DESCRIPTION_UZ = "descriptionUz";
    private static final String TYPE_DESCRIPTION_RU = "descriptionRu";

    private static final String NAME_UZ = "institutionUz";
    private static final String NAME_RU = "institutionRu";

    private static final String DESCRIPTION_UZ = "descriptionUz";
    private static final String DESCRIPTION_RU = "descriptionRu";

    private static final String ADDING_NAME_UZ = "addingTypeUz";
    private static final String ADDING_NAME_RU = "addingTypeRu";

    private static final String ADDING_DESCRIPTION_UZ = "addingDescriptionUz";
    private static final String ADDING_DESCRIPTION_RU = "addingDescriptionRu";

    private Institution institution;
    private InstitutionType institutionType;
    private User manager;
    private Address address;

    @Autowired
    private InstitutionService institutionService;
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private InstitutionTypeRepository institutionTypeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;

    @AfterEach
    void destroy() {
        institutionRepository.deleteAll();
    }

    @BeforeEach
    void setup() {
        setupType();
        setupManager();
        setupAddress();
        institution = new Institution(
                NAME_UZ,
                NAME_RU,
                DESCRIPTION_UZ,
                DESCRIPTION_RU,
                null,
                address,
                institutionType,
                null,
                manager);
    }

    private void setupAddress() {
        address = new Address(
                manager,
                15,
                "street",
                "city"
        );
        addressRepository.saveAndFlush(address);
    }

    private void setupManager() {
        manager = new User(
                "Ali",
                "Yusupov",
                "+998902002020",
                roleRepository
                        .findByName("MANAGER")
                        .orElseThrow(() -> new NotFoundException("ROLE NOT FOUND")));
        userRepository.save(manager);
    }

    private void setupType() {
        institutionType = new InstitutionType(
                TYPE_NAME_UZ,
                TYPE_NAME_RU,
                TYPE_DESCRIPTION_UZ,
                TYPE_DESCRIPTION_RU
        );
        institutionTypeRepository.saveAndFlush(institutionType);
    }

    @Test
    void shouldAddInstitutionType() {

        InstitutionAddDTO addDTO = new InstitutionAddDTO(
                ADDING_NAME_UZ,
                ADDING_NAME_RU,
                ADDING_DESCRIPTION_UZ,
                ADDING_DESCRIPTION_RU,
                institutionType.getId());

        ApiResult<InstitutionDTO> add = institutionService.add(addDTO);

        assertTrue(add.isSuccess());
        InstitutionDTO data = add.getData();
        assertEquals(data.getNameUz(), ADDING_NAME_UZ);
        assertEquals(data.getNameRu(), ADDING_NAME_RU);
        assertEquals(data.getDescriptionUz(), ADDING_DESCRIPTION_UZ);
        assertEquals(data.getDescriptionRu(), ADDING_DESCRIPTION_RU);
    }

    @Test
    void shouldNotAddWithExistedName() {
        institutionRepository.save(institution);
        InstitutionAddDTO addDTO = new InstitutionAddDTO(
                institution.getNameUz(),
                ADDING_NAME_RU,
                ADDING_DESCRIPTION_UZ,
                ADDING_DESCRIPTION_RU,
                institutionType.getId());

        assertThrows(AlreadyExistsException.class, () -> institutionService.add(addDTO));
    }

    @Test
    void shouldFindById() {
        institutionRepository.save(institution);
        ApiResult<InstitutionDTO> byId = institutionService.findById(institution.getId());
        assertTrue(byId.isSuccess());
        InstitutionDTO data = byId.getData();
        assertEquals(institution.getNameUz(), data.getNameUz());
        assertEquals(institution.getNameRu(), data.getNameRu());
        assertEquals(institution.getDescriptionUz(), data.getDescriptionUz());
        assertEquals(institution.getDescriptionRu(), data.getDescriptionRu());
    }

    @Test
    void shouldNotFindByNotExistedId() {
        assertThrows(NotFoundException.class, () -> institutionService.findById(15L));
    }

    @Test
    void shouldEditInstitution() {

        institutionRepository.saveAndFlush(institution);

        InstitutionEditDTO editDTO = new InstitutionEditDTO(
                ADDING_NAME_UZ,
                ADDING_NAME_RU,
                ADDING_DESCRIPTION_UZ,
                ADDING_DESCRIPTION_RU,
                institution.getId(),
                institutionType.getId());

        ApiResult<InstitutionDTO> edit = institutionService.edit(editDTO);

        assertTrue(edit.isSuccess());
        InstitutionDTO data = edit.getData();
        assertEquals(data.getNameUz(), ADDING_NAME_UZ);
        assertEquals(data.getNameRu(), ADDING_NAME_RU);
        assertEquals(data.getDescriptionUz(), ADDING_DESCRIPTION_UZ);
        assertEquals(data.getDescriptionRu(), ADDING_DESCRIPTION_RU);
    }

    @Test
    void shouldNotEditInstitutionWithNotExistedId() {

        InstitutionEditDTO editDTO = new InstitutionEditDTO(
                ADDING_NAME_UZ,
                ADDING_NAME_RU,
                ADDING_DESCRIPTION_UZ,
                ADDING_DESCRIPTION_RU,
                15L,
                institutionType.getId());

        assertThrows(NotFoundException.class, () -> institutionService.edit(editDTO));

    }

    @Test
    void shouldNotEditInstitutionToExistedName() {

        institutionRepository.saveAndFlush(new Institution("existing",
                "existing",
                "description",
                "description",
                null,
                null,
                institutionType,
                null,
                manager));

        institutionRepository.saveAndFlush(institution);

        InstitutionEditDTO editDTO = new InstitutionEditDTO(
                "existing",
                ADDING_NAME_RU,
                ADDING_DESCRIPTION_UZ,
                ADDING_DESCRIPTION_RU,
                institution.getId(),
                institutionType.getId());

        assertThrows(AlreadyExistsException.class, () -> institutionService.edit(editDTO));

    }

    @Test
    void shouldGetAll() {

        List<Institution> institutions = TestUtil.generateMockInstitutions(3,institutionType, manager);
        institutions = institutionRepository.saveAllAndFlush(institutions);
        ApiResult<List<InstitutionDTO>> all = institutionService.getAll();

        assertTrue(all.isSuccess());
        List<InstitutionDTO> data = all.getData();
        assertEquals(3, data.size());

        assertEquals(data.get(0).getNameUz(), institutions.get(0).getNameUz());
        assertEquals(data.get(0).getNameRu(), institutions.get(0).getNameRu());
        assertEquals(data.get(0).getDescriptionUz(), institutions.get(0).getDescriptionUz());
        assertEquals(data.get(0).getDescriptionRu(), institutions.get(0).getDescriptionRu());

        assertEquals(data.get(1).getNameUz(), institutions.get(1).getNameUz());
        assertEquals(data.get(1).getNameRu(), institutions.get(1).getNameRu());
        assertEquals(data.get(1).getDescriptionUz(), institutions.get(1).getDescriptionUz());
        assertEquals(data.get(1).getDescriptionRu(), institutions.get(1).getDescriptionRu());

        assertEquals(data.get(2).getNameUz(), institutions.get(2).getNameUz());
        assertEquals(data.get(2).getNameRu(), institutions.get(2).getNameRu());
        assertEquals(data.get(2).getDescriptionUz(), institutions.get(2).getDescriptionUz());
        assertEquals(data.get(2).getDescriptionRu(), institutions.get(2).getDescriptionRu());
    }


    @Test
    void shouldGetAllByTypeId() {

        List<Institution> institutions = TestUtil.generateMockInstitutions(3, institutionType, manager);

        institutions = institutionRepository.saveAllAndFlush(institutions);

        InstitutionType anotherType = institutionTypeRepository
                .saveAndFlush(new InstitutionType("type2", "type2", "", ""));
        institutions.add(new Institution("another",
                "another",
                "desc",
                "desc",
                anotherType,
                manager));

        ApiResult<List<InstitutionInfoDTO>> all = institutionService.getAllByTypeId(institutionType.getId());

        assertTrue(all.isSuccess());

        List<InstitutionInfoDTO> data = all.getData();
        assertEquals(3, data.size());
        TestUtil.checkInstitutionsEqualityEntityAndInfo(institutions.subList(0,3), data);
    }

    @Test
    void shouldGetAllByManagerId() {

        List<Institution> institutions = TestUtil.generateMockInstitutions(4,institutionType, manager);
        institutions = institutionRepository.saveAllAndFlush(institutions);

        User anotherManager = userRepository
                .saveAndFlush(new User("type2",
                        "type2",
                        "",
                        roleRepository
                                .findByName("MANAGER")
                                .orElseThrow(() -> new NotFoundException("DEFAULT ROLE NOT FOUND"))));
        institutions.add(new Institution("another",
                "another",
                "desc",
                "desc",
                institutionType,
                anotherManager));

        ApiResult<List<InstitutionInfoDTO>> all = institutionService.getAllByManagerId(manager.getId());

        assertTrue(all.isSuccess());

        List<InstitutionInfoDTO> data = all.getData();
        assertEquals(4, data.size());
        TestUtil.checkInstitutionsEqualityEntityAndInfo(institutions.subList(0,4), data);
    }

    @Test
    void shouldGetAllByPage() {

        List<Institution> institutions = TestUtil.generateMockInstitutions(10,institutionType, manager);
        institutions = institutionRepository.saveAllAndFlush(institutions);
        ApiResult<List<InstitutionInfoDTO>> all = institutionService
                .getAllForInfoByPage("0-4");

        assertTrue(all.isSuccess());

        List<InstitutionInfoDTO> data = all.getData();
        assertEquals(4, data.size());
        TestUtil.checkInstitutionsEqualityEntityAndInfo(institutions.subList(0, 4), data);
    }

    @Test
    void shouldGetAllByPage2() {

        List<Institution> institutions = TestUtil.generateMockInstitutions(10,institutionType, manager);
        institutions = institutionRepository.saveAllAndFlush(institutions);
        ApiResult<List<InstitutionInfoDTO>> all = institutionService
                .getAllForInfoByPage("1-4");

        assertTrue(all.isSuccess());

        List<InstitutionInfoDTO> data = all.getData();
        assertEquals(4, data.size());
        TestUtil.checkInstitutionsEqualityEntityAndInfo(institutions.subList(4, 8), data);
    }

    @Test
    void shouldDeleteById() {
        institutionRepository.saveAndFlush(institution);
        ApiResult<Void> delete = institutionService.delete(institution.getId());
        assertTrue(delete.isSuccess());
    }

    @Test
    void shouldSetImage() throws Exception {
        institutionRepository.saveAndFlush(institution);
        InputStream stream = new FileInputStream("src/test/java/uz/md/shopapp/images/mock_images/family.jpeg");
        MockMultipartFile image = new MockMultipartFile(
                "family.jpeg",
                "family.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                stream);

        ApiResult<Void> result = institutionService.setImage(institution.getId(), image);
        assertTrue(result.isSuccess());

        Optional<Institution> optional = institutionRepository
                .findById(institution.getId());

        assertTrue(optional.isPresent());
        Institution res = optional.get();
        assertEquals(rootPath.toUri() + "family.jpeg",
                res.getImageUrl());
    }




}
