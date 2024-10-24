package command;

import core.History;
import core.Ui;
import org.junit.jupiter.api.Test;
import programme.ProgrammeList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


public class HistoryCommandTest {
    @Test
    public void testIsExit_returnsFalse() {
        HistoryCommand historyCommand = new HistoryCommand();
        assertFalse(historyCommand.isExit());
    }

    @Test
    public void testExecute_doesNotThrowException() {
        Ui mockUi = mock(Ui.class);
        ProgrammeList mockPList = mock(ProgrammeList.class);
        History mockHistory = mock(History.class);

        HistoryCommand historyCommand = new HistoryCommand();

        assertDoesNotThrow(() -> historyCommand.execute(mockUi, mockPList, mockHistory));
    }

    @Test
    public void testExecute_showsHistoryWithData() {
        Ui mockUi = mock(Ui.class);
        ProgrammeList mockPList = mock(ProgrammeList.class);
        History mockHistory = mock(History.class);

        //Mock a history Data
        String historyData = "12/12/2024: ONE - Bench Press 3x12 30kg, Squat 3x12 50kg";
        when(mockHistory.toString()).thenReturn(historyData);

        HistoryCommand historyCommand = new HistoryCommand();

        historyCommand.execute(mockUi, mockPList, mockHistory);

        verify(mockUi).showMessage(anyString());
    }
}
