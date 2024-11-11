package article1be.user.service;

import article1be.common.exception.CustomException;
import article1be.outfit.entity.*;
import article1be.outfit.repository.SelectOutfitRepository;
import article1be.outfit.repository.SelectRecordRepository;
import article1be.user.dto.*;
import article1be.user.entity.*;
import article1be.user.repository.ConditionRepository;
import article1be.user.repository.StyleRepository;
import article1be.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StyleRepository styleRepository;

    @Mock
    private ConditionRepository conditionRepository;

    @Mock
    private SelectOutfitRepository selectOutfitRepository;

    @Mock
    private SelectRecordRepository selectRecordRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User(UserSocialSite.KAKAO, "test@example.com", "Test User", "010-1234-5678",
                LocalDate.of(1990, 1, 1), UserGender.MALE, UserState.ACTIVE, UserAuth.USER);
    }

    @Test
    public void testGetUserDetail() {
        when(userRepository.findByUserSeq(anyLong())).thenReturn(Optional.of(user));

        UserResponseDTO userResponse = userService.getUserDetail(1L);

        assertEquals("Test User", userResponse.getUserName());
        assertEquals("test@example.com", userResponse.getUserId());
    }

    @Test
    public void testUpdateUser() {
        when(userRepository.findByUserSeq(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUser(1L, "updatedNickname");

        assertEquals("updatedNickname", user.getUserNickname());
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.findByUserSeq(anyLong())).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    public void testGetUserPreference() {
        Style style = new Style(1L, "casual");
        Condition condition = new Condition(1L, "HOT");

        UserDataDTO userDataDTO = new UserDataDTO();

        userDataDTO.setUserNickname("nickname");
        userDataDTO.setStyleSeq(1L);
        userDataDTO.setConditionSeq(1L);

        user.createUserData(userDataDTO, style, condition);

        when(userRepository.findByUserSeqWithStyleAndCondition(anyLong())).thenReturn(Optional.of(user));

        PreferenceResponseDTO preferenceResponse = userService.getUserPreference(1L);

        assertEquals(style.getStyleSeq(), preferenceResponse.getStyleSeq());
        assertEquals(condition.getConditionSeq(), preferenceResponse.getConditionSeq());
    }

    @Test
    public void testUpdatePreference() {
        Style style = new Style(1L, "casual");
        Condition condition = new Condition(1L, "HOT");

        // userSeq는 1L로 가정하고, 직접 설정 X
        user = new User(UserSocialSite.KAKAO, "test@example.com", "Test User",
                "010-1234-5678", LocalDate.of(1990, 1, 1),
                UserGender.MALE, UserState.ACTIVE, UserAuth.USER);

        UserDataDTO userDataDTO = new UserDataDTO();

        userDataDTO.setUserNickname("nickname");
        userDataDTO.setStyleSeq(style.getStyleSeq());
        userDataDTO.setConditionSeq(condition.getConditionSeq());

        user.createUserData(userDataDTO, style, condition);

        when(userRepository.findByUserSeq(anyLong())).thenReturn(Optional.of(user)); // 여기서 userSeq는 Mockito에 의해 처리
        when(styleRepository.findStyleByStyleSeq(style.getStyleSeq())).thenReturn(style);
        when(conditionRepository.findConditionByConditionSeq(condition.getConditionSeq())).thenReturn(condition);

        UserPreferDTO preferData = new UserPreferDTO();

        preferData.setStyleSeq(style.getStyleSeq());
        preferData.setConditionSeq(condition.getConditionSeq());

        userService.updatePreference(1L, preferData); // userSeq를 1L로 직접 지정

        assertEquals(style.getStyleSeq(), user.getStyle().getStyleSeq());
        assertEquals(condition.getConditionSeq(), user.getCondition().getConditionSeq());
    }

    @Test
    public void testCheckUserNickname_DuplicateNickname() {
        // 중복 닉네임을 가진 사용자 객체를 Mocking
        User existingUser = mock(User.class);

        when(existingUser.getUserSeq()).thenReturn(1L); // userSeq를 1L로 반환
        when(existingUser.getUserNickname()).thenReturn("testUser");
        when(userRepository.findByUserNickname("testUser")).thenReturn(Optional.of(existingUser));

        assertDoesNotThrow(() -> {
            userService.checkUserNickname("testUser", 1L);
        });

        assertThrows(CustomException.class, () -> {
            userService.checkUserNickname("testUser", 2L);
        });
    }

    @Test
    public void testGetUserSelectOutfit() {
        User user = mock(User.class);

        when(user.getUserSeq()).thenReturn(1L);

        Style style = mock(Style.class);

        when(style.getStyleSeq()).thenReturn(1L);
        when(style.getStyleName()).thenReturn("casual");

        Condition condition = mock(Condition.class);

        when(condition.getConditionSeq()).thenReturn(1L);
        when(condition.getConditionName()).thenReturn("HOT");

        when(user.getStyle()).thenReturn(style);
        when(user.getCondition()).thenReturn(condition);


        Outfit outfit1 = mock(Outfit.class);

        when(outfit1.getOutfitSeq()).thenReturn(1L);
        when(outfit1.getOutfitName()).thenReturn("Casual Top");
        when(outfit1.getOutfitCategory()).thenReturn(OutfitCategory.TOP);

        Outfit outfit2 = mock(Outfit.class);

        when(outfit2.getOutfitSeq()).thenReturn(2L);
        when(outfit2.getOutfitName()).thenReturn("Casual Bottom");
        when(outfit2.getOutfitCategory()).thenReturn(OutfitCategory.BOTTOM);

        Outfit outfit3 = mock(Outfit.class);

        when(outfit3.getOutfitSeq()).thenReturn(3L);
        when(outfit3.getOutfitName()).thenReturn("Sneakers");
        when(outfit3.getOutfitCategory()).thenReturn(OutfitCategory.SHOES);

        Outfit outfit4 = mock(Outfit.class);

        when(outfit4.getOutfitSeq()).thenReturn(4L);
        when(outfit4.getOutfitName()).thenReturn("Jacket");
        when(outfit4.getOutfitCategory()).thenReturn(OutfitCategory.OUTERWEAR);

        Outfit outfit5 = mock(Outfit.class);

        when(outfit5.getOutfitSeq()).thenReturn(5L);
        when(outfit5.getOutfitName()).thenReturn("Hat");
        when(outfit5.getOutfitCategory()).thenReturn(OutfitCategory.ACCESSORY);

        Long selectSeq = 1L;

        SelectOutfit selectOutfit1 = mock(SelectOutfit.class);

        when(selectOutfit1.getOutfit()).thenReturn(outfit1);

        SelectOutfit selectOutfit2 = mock(SelectOutfit.class);

        when(selectOutfit2.getOutfit()).thenReturn(outfit2);

        SelectOutfit selectOutfit3 = mock(SelectOutfit.class);

        when(selectOutfit3.getOutfit()).thenReturn(outfit3);

        SelectOutfit selectOutfit4 = mock(SelectOutfit.class);

        when(selectOutfit4.getOutfit()).thenReturn(outfit4);

        SelectOutfit selectOutfit5 = mock(SelectOutfit.class);

        when(selectOutfit5.getOutfit()).thenReturn(outfit5);

        List<SelectOutfit> selectOutfits = Arrays.asList(
                selectOutfit1,
                selectOutfit2,
                selectOutfit3,
                selectOutfit4,
                selectOutfit5
        );

        when(userRepository.findByUserSeqWithStyleAndCondition(1L)).thenReturn(Optional.of(user));
        when(selectOutfitRepository.findBySelectRecord_SelectSeq(selectSeq)).thenReturn(selectOutfits);

        SelectOutfitResponseDTO responseDTO = userService.getUserSelectOutfit(1L, selectSeq);

        assertEquals(selectSeq, responseDTO.getSelectSeq());
        assertEquals(outfit1.getOutfitSeq(), responseDTO.getTopSeq());
        assertEquals(outfit2.getOutfitSeq(), responseDTO.getBottomSeq());
        assertEquals(outfit3.getOutfitSeq(), responseDTO.getShoesSeq());
        assertEquals(outfit4.getOutfitSeq(), responseDTO.getOuterSeq());
        assertEquals(outfit5.getOutfitSeq(), responseDTO.getAccessorySeq().get(0));
    }

    @Test
    public void testGetUserSelectRecords() {
        User user = mock(User.class);

        when(user.getUserSeq()).thenReturn(1L);

        SelectRecord selectRecord1 = mock(SelectRecord.class);

        when(selectRecord1.getSelectSeq()).thenReturn(1L);
        when(selectRecord1.getSelectDate()).thenReturn(LocalDate.now().atStartOfDay());
        when(selectRecord1.getCustomDate()).thenReturn(LocalDate.now().atStartOfDay());
        when(selectRecord1.getCustomLocation()).thenReturn("Location 1");
        when(selectRecord1.getCurTemp()).thenReturn(25.0);

        SelectRecord selectRecord2 = mock(SelectRecord.class);

        when(selectRecord2.getSelectSeq()).thenReturn(2L);
        when(selectRecord2.getSelectDate()).thenReturn(LocalDate.now().minusDays(1).atStartOfDay());
        when(selectRecord2.getCustomDate()).thenReturn(LocalDate.now().minusDays(1).atStartOfDay());
        when(selectRecord2.getCustomLocation()).thenReturn("Location 2");
        when(selectRecord2.getCurTemp()).thenReturn(22.0);

        List<SelectRecord> selectRecords = Arrays.asList(selectRecord1, selectRecord2);

        when(selectRecordRepository.findAllByUserSeqOrderBySelectDateDesc(user.getUserSeq())).thenReturn(selectRecords);

        List<SelectRecordResponseDTO> result = userService.getUserSelectRecords(user.getUserSeq());

        assertEquals(2, result.size());

        assertEquals(selectRecord1.getSelectSeq(), result.get(0).getSelectSeq());
        assertEquals(selectRecord1.getSelectDate(), result.get(0).getSelectDate());
        assertEquals(selectRecord1.getCustomDate(), result.get(0).getCustomDate());
        assertEquals(selectRecord1.getCustomLocation(), result.get(0).getCustomLocation());
        assertEquals(selectRecord1.getCurTemp(), result.get(0).getCurTemp());

        assertEquals(selectRecord2.getSelectSeq(), result.get(1).getSelectSeq());
        assertEquals(selectRecord2.getSelectDate(), result.get(1).getSelectDate());
        assertEquals(selectRecord2.getCustomDate(), result.get(1).getCustomDate());
        assertEquals(selectRecord2.getCustomLocation(), result.get(1).getCustomLocation());
        assertEquals(selectRecord2.getCurTemp(), result.get(1).getCurTemp());
    }

}
