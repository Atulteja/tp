package command.meals;

import command.CommandResult;
import history.DailyRecord;
import history.History;
import meal.MealList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ViewMealCommandTest {

    private ViewMealCommand viewMealCommand;
    private LocalDate date;

    @Mock
    private History mockHistory;

    @Mock
    private DailyRecord mockDailyRecord;

    @Mock
    private MealList mockMealList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        date = LocalDate.now();

        // Set up History mock to return a DailyRecord for the specified date
        when(mockHistory.getRecordByDate(date)).thenReturn(mockDailyRecord);

        // Set up DailyRecord mock to return a MealList
        when(mockDailyRecord.getMealList()).thenReturn(mockMealList);

        viewMealCommand = new ViewMealCommand(date);
    }

    @Test
    public void testExecute_HappyPath() {
        // Arrange
        when(mockMealList.toString()).thenReturn("Sample Meal List");
        CommandResult expectedResult = new CommandResult("Sample Meal List");

        // Act
        CommandResult result = viewMealCommand.execute(mockHistory);

        // Assert
        verify(mockHistory).getRecordByDate(date);
        verify(mockDailyRecord).getMealList();
        assertEquals(expectedResult, result, "Execution should return a CommandResult with the correct meal list output.");
    }

    @Test
    public void testExecute_EdgeCase_NullDailyRecord() {
        // Arrange
        when(mockHistory.getRecordByDate(date)).thenReturn(null);

        // Act & Assert
        assertThrows(AssertionError.class, () -> viewMealCommand.execute(mockHistory), "Executing ViewMealCommand with a null DailyRecord should throw an AssertionError.");
    }

    @Test
    public void testConstructor_EdgeCase_NullDate() {
        // Act & Assert
        assertThrows(AssertionError.class, () -> new ViewMealCommand(null), "Creating ViewMealCommand with a null date should throw an AssertionError.");
    }
}
